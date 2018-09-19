package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetCallHistoryResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.HistoricalCall;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.UserId;

public class GetCallHistoryResult extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "get-call-history result";
    private static final String ELEMENT_NAME_CALL_HISTORY = "callhistory";

    @Nullable private final Long totalRecordCount;
    @Nullable private final Long firstRecordNumber;
    @Nullable private final Long recordCountInBatch;
    @Nonnull private List<HistoricalCall<Jid>> calls;

    private GetCallHistoryResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.totalRecordCount = builder.getTotalRecordCount().orElse(null);
        this.firstRecordNumber = builder.getFirstRecordNumber().orElse(null);
        this.recordCountInBatch = builder.getRecordCountInBatch().orElse(null);
        this.calls = new ArrayList<>(builder.getCalls());
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetCallHistoryResult from(@Nonnull final XmlPullParser parser) throws IOException, XmlPullParserException {
        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);
        final int inDepth = parser.getDepth();

        parser.nextTag();
        if (ELEMENT_NAME_CALL_HISTORY.equals(parser.getName())) {
            final int callHistoryDepth = parser.getDepth();
            SmackPacketUtil.getLongAttribute(parser, "total").ifPresent(builder::setTotalRecordCount);
            SmackPacketUtil.getLongAttribute(parser, "start").ifPresent(builder::setFirstRecordNumber);
            SmackPacketUtil.getLongAttribute(parser, "count").ifPresent(builder::setRecordCountInBatch);
            parser.nextTag();
            while (parser.getDepth() > callHistoryDepth) {
                while (parser.getEventType() == XmlPullParser.START_TAG && "call".equals(parser.getName())) {
                    final int callDepth = parser.getDepth();
                    parser.nextTag();
                    final HistoricalCall.Builder<Jid> callBuilder = HistoricalCall.Builder.start();
                    while (parser.getDepth() > callDepth) {
                        parseHistoricalCall(parser, parseErrors, callDepth, callBuilder);
                    }
                    builder.addCall(callBuilder.build(parseErrors));
                }
                parser.nextTag();
            }
        }
        ParserUtils.forwardToEndTagOfDepth(parser, inDepth + 1);
        return builder.build(parseErrors);
    }

    private static void parseHistoricalCall(@Nonnull final XmlPullParser parser, final List<String> parseErrors, final int callDepth, final HistoricalCall.Builder<Jid> callBuilder)
            throws IOException, XmlPullParserException {
        final Optional<String> elementText = SmackPacketUtil.getElementTextString(parser);
        switch (parser.getName()) {
        case "id":
            elementText.flatMap(CallId::from).ifPresent(callBuilder::setId);
            break;
        case "profile":
            elementText.flatMap(UserId::from).ifPresent(callBuilder::setUserId);
            break;
        case "interest":
            elementText.flatMap(InterestId::from).ifPresent(callBuilder::setInterestId);
            break;
        case "state":
            elementText.flatMap(CallState::from).ifPresent(callBuilder::setState);
            break;
        case "direction":
            elementText.flatMap(CallDirection::from).ifPresent(callBuilder::setDirection);
            break;
        case "caller":
            elementText.flatMap(PhoneNumber::from).ifPresent(callBuilder::setCallerNumber);
            break;
        case "callername":
            elementText.ifPresent(callBuilder::setCallerName);
            break;
        case "called":
            elementText.flatMap(PhoneNumber::from).ifPresent(callBuilder::setCalledNumber);
            break;
        case "calledname":
            elementText.ifPresent(callBuilder::setCalledName);
            break;
        case "duration":
            try {
                elementText.map(Long::parseLong).map(Duration::ofMillis).ifPresent(callBuilder::setDuration);
            } catch (final NumberFormatException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid duration '%s'; please supply an integer", STANZA_DESCRIPTION, elementText.get()));
            }
            break;
        case "timestamp":
            if (callBuilder.isStartTimeNull()) {
                try {
                    elementText.map(Timestamp::valueOf).map(Timestamp::toInstant).ifPresent(callBuilder::setStartTime);
                } catch (final IllegalArgumentException ignored) {
                    parseErrors.add(String.format("Invalid %s; invalid timestamp '%s'; please supply a valid timestamp", STANZA_DESCRIPTION, elementText.get()));
                }
            }
            break;
        case "starttime":
            try {
                elementText.map(Instant::parse).ifPresent(callBuilder::setStartTime);
            } catch(final DateTimeParseException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid starttime '%s'; please supply a valid starttime", STANZA_DESCRIPTION, elementText.get()));
            }             
            break;
        case "tsc":
            elementText.flatMap(SmackPacketUtil::getSmackJid).ifPresent(callBuilder::setTsc);
            break;
        default:
            parseErrors.add("Unrecognised element:" + parser.getName());
            break;
        }
        ParserUtils.forwardToEndTagOfDepth(parser, callDepth + 1);
        parser.nextTag();
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(final IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "output")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.halfOpenElement(ELEMENT_NAME_CALL_HISTORY)
                .attribute("xmlns", OpenlinkXmppNamespace.OPENLINK_CALL_HISTORY.uri())
                .optLongAttribute("total", totalRecordCount)
                .optLongAttribute("start", firstRecordNumber)
                .optLongAttribute("count", recordCountInBatch)
                .rightAngleBracket();
        calls.forEach(call -> {
            xml.openElement("call");
            xml.optElement("id", call.getId().map(CallId::value).orElse(null));
            xml.optElement("profile", call.getUserId().map(UserId::value).orElse(null));
            xml.optElement("interest", call.getInterestId().map(InterestId::value).orElse(null));
            xml.optElement("state", call.getState().map(CallState::getLabel).orElse(null));
            xml.optElement("direction", call.getDirection().map(CallDirection::getLabel).orElse(null));
            xml.optElement("caller", call.getCallerNumber().map(PhoneNumber::value).orElse(null));
            xml.optElement("callername", call.getCallerName().orElse(null));
            xml.optElement("called", call.getCalledNumber().map(PhoneNumber::value).orElse(null));
            xml.optElement("calledname", call.getCalledName().orElse(null));
            xml.optElement("timestamp", call.getStartTime().map(Timestamp::from).map(Timestamp::toString).orElse(null));
            xml.optElement("starttime", call.getStartTime().map(startTime -> SmackPacketUtil.ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC))).orElse(null));
            xml.optElement("duration", call.getDuration().map(Duration::toMillis).map(String::valueOf).orElse(null));
            xml.optElement("tsc", call.getTsc().map(Jid::toString).orElse(null));
            xml.closeElement("call");
        });
        xml.closeElement(ELEMENT_NAME_CALL_HISTORY);
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getTotalRecordCount() {
        return Optional.ofNullable(totalRecordCount);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getFirstRecordNumber() {
        return Optional.ofNullable(firstRecordNumber);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getRecordCountInBatch() {
        return Optional.ofNullable(recordCountInBatch);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public List<HistoricalCall<Jid>> getCalls() {
        return calls;
    }

    public static final class Builder extends GetCallHistoryResultBuilder<Builder, Jid, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(IQ.Type.class);
        }

        /**
         * Convenience method to create a new {@link Builder} based
         * on a {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ. The new
         * builder will be initialized with:<ul>
         *
         *      <li>The sender set to the recipient of the originating IQ.
         *      <li>The recipient set to the sender of the originating IQ.
         *      <li>The id set to the id of the originating IQ.
         * </ul>
         *
         * @param request the {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ packet.
         * @throws IllegalArgumentException if the IQ packet does not have a type of
         *      {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set}.
         * @return a new {@link Builder} based on the originating IQ.
         */
        @SuppressWarnings("WeakerAccess")
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return SmackPacketUtil.createResultBuilder(start(), request);
        }


        @Nonnull
        public GetCallHistoryResult build() {
            super.validate();
            return new GetCallHistoryResult(this, null);
        }

        @Nonnull
        private GetCallHistoryResult build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new GetCallHistoryResult(this, errors);
        }
    }

}
