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
import com.bt.openlink.iq.RequestActionResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.CallStatus;

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
        getCallStatus().ifPresent(status -> SmackPacketUtil.addCallStatus(xml, status));
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

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    public static final class Builder extends RequestActionResultBuilder<Builder, Jid, Type> {

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public static RequestActionResult.Builder start() {
            return new RequestActionResult.Builder();
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
        @SuppressWarnings("WeakerAccess")
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return SmackPacketUtil.createResultBuilder(start(), request);
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

}