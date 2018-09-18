package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
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
import com.bt.openlink.type.ProfileId;

public class GetCallHistoryResult extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "get-call-history result";

    @Nullable private final Long totalRecordCount;
    @Nullable private final Long firstRecordNumber;
    @Nullable private final Long recordCountInBatch;
    @Nonnull private List<HistoricalCall> calls;

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
        if ("callhistory".equals(parser.getName())) {
            final int callHistoryDepth = parser.getDepth();
            SmackPacketUtil.getLongAttribute(parser, "total").ifPresent(builder::setTotalRecordCount);
            SmackPacketUtil.getLongAttribute(parser, "start").ifPresent(builder::setFirstRecordNumber);
            SmackPacketUtil.getLongAttribute(parser, "count").ifPresent(builder::setRecordCountInBatch);
            parser.nextTag();
            while (parser.getDepth() > callHistoryDepth) {
                while (parser.getEventType() == XmlPullParser.START_TAG && "call".equals(parser.getName())) {
                    final int callDepth = parser.getDepth();
                    parser.nextTag();
                    final HistoricalCall.Builder callBuilder = HistoricalCall.Builder.start();
                    while (parser.getDepth() > callDepth) {
                        final Optional<String> elementText = SmackPacketUtil.getElementTextString(parser);
                        switch (parser.getName()) {
                        case "id":
                            elementText.flatMap(CallId::from).ifPresent(callBuilder::setId);
                            break;
                        case "profile":
                            elementText.flatMap(ProfileId::from).ifPresent(callBuilder::setProfileId);
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
                            elementText.ifPresent(duration -> {
                                try {
                                    callBuilder.setDuration(Duration.ofMillis(Long.parseLong(duration)));
                                } catch (final NumberFormatException ignored) {
                                    parseErrors.add(String.format("Invalid %s; invalid duration '%s'; please supply an integer", STANZA_DESCRIPTION, duration));
                                }
                            });
                            break;
                        case "timestamp":
                            elementText.ifPresent(timestamp -> {
                                try {
                                    callBuilder.setStartTime(Timestamp.valueOf(timestamp).toInstant());
                                } catch (final IllegalArgumentException ignored) {
                                    parseErrors.add(String.format("Invalid %s; invalid timestamp '%s'; please supply a valid timestamp", STANZA_DESCRIPTION, timestamp));
                                }
                            });
                            break;
                        case "tsc":
                            elementText.ifPresent(callBuilder::setTsc);
                            break;
                        default:
                            parseErrors.add("Unrecognised element:" + parser.getName());
                            break;
                        }
                        ParserUtils.forwardToEndTagOfDepth(parser, callDepth + 1);
                        parser.nextTag();
                    }
                    builder.addCall(callBuilder.build(parseErrors));
                }
                parser.nextTag();
            }
        }
        ParserUtils.forwardToEndTagOfDepth(parser, inDepth + 1);
        return builder.build(parseErrors);
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
        xml.halfOpenElement("callhistory")
                .attribute("xmlns", OpenlinkXmppNamespace.OPENLINK_CALL_HISTORY.uri())
                .optLongAttribute("total", totalRecordCount)
                .optLongAttribute("start", firstRecordNumber)
                .optLongAttribute("count", recordCountInBatch)
                .rightAngleBracket();
        calls.forEach(call -> {
            xml.openElement("call");
            xml.element("id", call.getId().map(CallId::value).orElse(""));
            xml.element("profile", call.getProfileId().map(ProfileId::value).orElse(""));
            xml.element("interest", call.getInterestId().map(InterestId::value).orElse(""));
            xml.element("state", call.getState().map(CallState::getLabel).orElse(""));
            xml.element("direction", call.getDirection().map(CallDirection::getLabel).orElse(""));
            xml.element("caller", call.getCallerNumber().map(PhoneNumber::value).orElse(""));
            xml.element("callername", call.getCallerName().orElse(""));
            xml.element("called", call.getCalledNumber().map(PhoneNumber::value).orElse(""));
            xml.element("calledname", call.getCalledName().orElse(""));
            xml.element("timestamp", call.getStartTime().map(Timestamp::from).map(Timestamp::toString).orElse(""));
            xml.element("duration", call.getDuration().map(Duration::toMillis).map(String::valueOf).orElse(""));
            xml.element("tsc", call.getTsc().orElse(""));
            xml.closeElement("call");
        });
        xml.closeElement("callhistory");
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
    public List<HistoricalCall> getCalls() {
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
