package com.bt.openlink.smack.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.RequestActionResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.CallStatus;
import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestActionResult extends OpenlinkIQ {


    @Nullable private final CallStatus callStatus;
    private static final String ELEMENT_CALLSTATUS = "callstatus";

    private RequestActionResult(@Nonnull final RequestActionResult.Builder builder,
                                @Nullable final List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.callStatus = builder.getCallStatus().orElse(null);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri()).attribute("type", "output")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_OUT);
        getCallStatus().ifPresent(status-> SmackPacketUtil.addCallStatus(xml, status));
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static IQ from(@Nonnull final XmlPullParser parser) throws IOException, XmlPullParserException {
        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, ELEMENT_CALLSTATUS);

        final RequestActionResult.Builder builder = RequestActionResult.Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        SmackPacketUtil.getCallStatus(parser, "request-action result", parseErrors).ifPresent(builder::setCallStatus);
        return builder.build(parseErrors);
    }

    public static final class Builder extends RequestActionResultBuilder<Builder, Jid, Type> {

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public static RequestActionResult.Builder start() {
            return new RequestActionResult.Builder();
        }

        @Nonnull
        public RequestActionResult build() {
            super.validate();
            return new RequestActionResult(this, null);
        }

        @Nonnull
        private RequestActionResult build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new RequestActionResult(this, errors);
        }

    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }
}