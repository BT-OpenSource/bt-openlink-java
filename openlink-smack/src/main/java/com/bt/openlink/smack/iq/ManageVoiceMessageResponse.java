package com.bt.openlink.smack.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.ManageVoiceMessageResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.DeviceStatus;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageVoiceMessageResponse extends OpenlinkIQ {

    @Nullable private final DeviceStatus deviceStatus;

    ManageVoiceMessageResponse(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        deviceStatus = builder.getDeviceStatus().orElse(null);
    }

    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {
        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);

        final ArrayList<String> parseErrors = new ArrayList<>();

        final ManageVoiceMessageResponse.Builder builder = ManageVoiceMessageResponse.Builder.start();
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
        };

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
        public static ManageVoiceMessageResponse.Builder start() {
            return new ManageVoiceMessageResponse.Builder();
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public ManageVoiceMessageResponse build() {
            super.validate();
            return new ManageVoiceMessageResponse(this, null);
        }

        @Nonnull
        public ManageVoiceMessageResponse build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new ManageVoiceMessageResponse(this, errors);
        }

    }
}