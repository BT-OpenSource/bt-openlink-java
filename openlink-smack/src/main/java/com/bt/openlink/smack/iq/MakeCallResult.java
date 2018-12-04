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

public final class MakeCallResult extends OpenlinkIQ {
    private static final String ELEMENT_CALLSTATUS = "callstatus";
    @Nullable private final CallStatus callStatus;

    private MakeCallResult(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.callStatus = builder.getCallStatus().orElse(null);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Nonnull
    static IQ from(final XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, ELEMENT_CALLSTATUS);

        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        SmackPacketUtil.getCallStatus(parser, "make-call result", parseErrors).ifPresent(builder::setCallStatus);
        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(final IQChildElementXmlStringBuilder xml) {
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

        /**
         * Convenience method to create a new {@link Builder} based on a {@link Type#get IQ.Type.get} or {@link Type#set
         * IQ.Type.set} IQ. The new builder will be initialized with:
         * <ul>
         *
         * <li>The sender set to the recipient of the originating IQ.
         * <li>The recipient set to the sender of the originating IQ.
         * <li>The id set to the id of the originating IQ.
         * </ul>
         *
         * @param request
         *            the {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ packet.
         * @throws IllegalArgumentException
         *             if the IQ packet does not have a type of {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set}.
         * @return a new {@link Builder} based on the originating IQ.
         */
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return SmackPacketUtil.createResultBuilder(start(), request);
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
