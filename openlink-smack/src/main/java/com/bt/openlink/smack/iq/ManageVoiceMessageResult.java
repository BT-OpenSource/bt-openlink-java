package com.bt.openlink.smack.iq;

import java.io.IOException;
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
import com.bt.openlink.iq.ManageVoiceMessageResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.DeviceStatus;

public class ManageVoiceMessageResult extends OpenlinkIQ {

    @Nullable private final DeviceStatus deviceStatus;

    private ManageVoiceMessageResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        deviceStatus = builder.getDeviceStatus().orElse(null);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {
        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);

        final ArrayList<String> parseErrors = new ArrayList<>();

        final ManageVoiceMessageResult.Builder builder = ManageVoiceMessageResult.Builder.start();
        final int inDepth = parser.getDepth();
        parser.nextTag();
        while (parser.getDepth() > inDepth) {
            switch (parser.getName()) {
                case "devicestatus":
                    SmackPacketUtil.getDeviceStatus(parser, parseErrors).ifPresent(builder::setDeviceStatus);

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
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_MANAGE_VOICE_MESSAGE.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri()).attribute("type", "output")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_OUT);

        getDeviceStatus().ifPresent(deviceStatus1 -> SmackPacketUtil.addDeviceStatus(xml, deviceStatus1));

        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }


    public static final class Builder extends ManageVoiceMessageResultBuilder<Builder, Jid, Type> {

        @Nonnull
        public static ManageVoiceMessageResult.Builder start() {
            return new ManageVoiceMessageResult.Builder();
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


        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public ManageVoiceMessageResult build() {
            super.validate();
            return new ManageVoiceMessageResult(this, null);
        }

        @Nonnull
        public ManageVoiceMessageResult build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new ManageVoiceMessageResult(this, errors);
        }

    }
}