package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.bt.openlink.type.Call;

public class MakeCallResult extends OpenlinkIQ {
    private static final String ELEMENT_CALLSTATUS = "callstatus";
    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;

    private MakeCallResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.calls = Collections.unmodifiableList(builder.getCalls());
        this.callStatusBusy = builder.isCallStatusBusy().orElse(null);
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    @Nonnull
    public Optional<Boolean> isCallStatusBusy() {
        return Optional.ofNullable(callStatusBusy);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, ELEMENT_CALLSTATUS);

        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        if (parser.getName().equals(ELEMENT_CALLSTATUS)) {
            final Optional<Boolean> callBusy = SmackPacketUtil.getBooleanAttribute(parser, "busy", "make-call result", parseErrors);
            callBusy.ifPresent(builder::setCallStatusBusy);
            parser.nextTag();
            final List<Call> calls = SmackPacketUtil.getCalls(parser, "make call result", parseErrors);
            builder.addCalls(calls);
        }
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
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_OUT)
                .rightAngleBracket();
        xml.halfOpenElement(ELEMENT_CALLSTATUS)
                .attribute("xmlns", "http://xmpp.org/protocol/openlink:01:00:00#call-status")
                .attribute("busy", String.valueOf(callStatusBusy))
                .rightAngleBracket();

        return SmackPacketUtil.addCalls(xml, calls);
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
