package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.MakeCallResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.CallStatus;

public class MakeCallResult extends OpenlinkIQ {
    private static final String ELEMENT_CALLSTATUS = "callstatus";
    @Nullable private final CallStatus callStatus;

    private MakeCallResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.callStatus = builder.getCallStatus().orElse(null);
    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, ELEMENT_CALLSTATUS);

        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        SmackPacketUtil.getCallStatus(parser, "make-call result", parseErrors).ifPresent(builder::setCallStatus);
        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_MAKE_CALL.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri()).attribute("type", "output")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_OUT);
        getCallStatus().ifPresent(status->SmackPacketUtil.addCallStatus(xml, status));
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends MakeCallResultBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public MakeCallResult build() {
            super.validate();
            return new MakeCallResult(this, null);
        }

        @Nonnull
        public MakeCallResult build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new MakeCallResult(this, errors);
        }

    }

}
