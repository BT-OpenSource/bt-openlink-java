package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.bt.openlink.iq.GetCallHistoryRequestBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.CallType;

public class GetCallHistoryRequest extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "get-call-history request";
    private static final String DATE_PATTERN = "MM/dd/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Nullable private final Jid jid;
    @Nullable private final String caller;
    @Nullable private final String called;
    @Nullable private final CallType callType;
    @Nullable private final LocalDate fromDate;
    @Nullable private final LocalDate upToDate;
    @Nullable private final Long start;
    @Nullable private final Long count;

    private GetCallHistoryRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        this.caller = builder.getCaller().orElse(null);
        this.called = builder.getCalled().orElse(null);
        this.callType = builder.getCallType().orElse(null);
        this.fromDate = builder.getFromDate().orElse(null);
        this.upToDate = builder.getUpToDate().orElse(null);
        this.start = builder.getStart().orElse(null);
        this.count = builder.getCount().orElse(null);
    }

    @Nonnull
    public static GetCallHistoryRequest from(@Nonnull final XmlPullParser parser) throws IOException, XmlPullParserException {

        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);
        final int inDepth = parser.getDepth();

        parser.nextTag();
        while (parser.getDepth() > inDepth) {
            final Optional<String> elementText = SmackPacketUtil.getElementTextString(parser);
            switch (parser.getName()) {
            case "jid":
                elementText.flatMap(SmackPacketUtil::getSmackJid).ifPresent(builder::setJID);
                break;
            case "caller":
                elementText.ifPresent(builder::setCaller);
                break;
            case "called":
                elementText.ifPresent(builder::setCalled);
                break;
            case "calltype":
                elementText.ifPresent(calltype -> {
                    final Optional<CallType> optionalCallType = CallType.from(calltype);
                    if(optionalCallType.isPresent()) {
                        builder.setCallType(optionalCallType.get());
                    } else {
                        parseErrors.add("Invalid get-call-history request; invalid calltype - 'not-a-call-type' should be 'in', 'out' or 'missed'");
                    }
                });
                break;
            case "fromdate":
                try {
                    elementText.map(fromdate -> LocalDate.parse(fromdate, DATE_FORMATTER)).ifPresent(builder::setFromDate);
                } catch (final DateTimeParseException ignored) {
                    parseErrors.add(String.format("Invalid %s; invalid fromdate '%s'; date format is '%s'", STANZA_DESCRIPTION, elementText.get(), DATE_PATTERN));
                }
                break;
            case "uptodate":
                try {
                    elementText.map(todate -> LocalDate.parse(todate, DATE_FORMATTER)).ifPresent(builder::setUpToDate);
                } catch (final DateTimeParseException ignored) {
                    parseErrors.add(String.format("Invalid %s; invalid uptodate '%s'; date format is '%s'", STANZA_DESCRIPTION, elementText.get(), DATE_PATTERN));
                }
                break;
            case "start":
                try {
                    elementText.map(Long::valueOf).ifPresent(builder::setStart);
                } catch (final NumberFormatException ignored) {
                    parseErrors.add(String.format("Invalid %s; invalid start '%s'; please supply an integer", STANZA_DESCRIPTION, elementText.get()));
                }
                break;
            case "count":
                try {
                    elementText.map(Long::valueOf).ifPresent(builder::setCount);
                } catch (final NumberFormatException ignored) {
                    parseErrors.add(String.format("Invalid %s; invalid count '%s'; please supply an integer", STANZA_DESCRIPTION, elementText.get()));
                }
                break;
            default:
                parseErrors.add("Unrecognised element:" + parser.getName());
                break;
            }
            ParserUtils.forwardToEndTagOfDepth(parser, inDepth + 1);
            parser.nextTag();
        }

        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(final IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();
        xml.optElement("jid", jid);
        xml.optElement("caller", caller);
        xml.optElement("called", called);
        getCallType().map(CallType::getLabel).ifPresent(calltype -> xml.optElement("calltype", calltype));
        getFromDate().map(DATE_FORMATTER::format).ifPresent(fromdate -> xml.optElement("fromdate", fromdate));
        getUpToDate().map(DATE_FORMATTER::format).ifPresent(uptodate -> xml.optElement("uptodate", uptodate));
        xml.optElement("start", start);
        xml.optElement("count", count);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Jid> getJID() {
        return Optional.ofNullable(jid);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<String> getCaller() {
        return Optional.ofNullable(caller);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<String> getCalled() {
        return Optional.ofNullable(called);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<CallType> getCallType() {
        return Optional.ofNullable(callType);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<LocalDate> getFromDate() {
        return Optional.ofNullable(fromDate);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<LocalDate> getUpToDate() {
        return Optional.ofNullable(upToDate);
    }

    @Nonnull
    public Optional<Long> getStart() {
        return Optional.ofNullable(start);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getCount() {
        return Optional.ofNullable(count);
    }

    public static final class Builder extends GetCallHistoryRequestBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetCallHistoryRequest build() {
            super.validate();
            return new GetCallHistoryRequest(this, null);
        }

        @Nonnull
        private GetCallHistoryRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new GetCallHistoryRequest(this, errors);
        }

    }

}
