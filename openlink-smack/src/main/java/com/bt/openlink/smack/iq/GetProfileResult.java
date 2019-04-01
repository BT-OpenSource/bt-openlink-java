package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import com.bt.openlink.iq.GetProfileResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.DeviceId;
import com.bt.openlink.type.DeviceType;
import com.bt.openlink.type.Key;
import com.bt.openlink.type.KeyColor;
import com.bt.openlink.type.KeyFunction;
import com.bt.openlink.type.KeyId;
import com.bt.openlink.type.KeyInterest;
import com.bt.openlink.type.KeyLabel;
import com.bt.openlink.type.KeyModifier;
import com.bt.openlink.type.KeyPage;
import com.bt.openlink.type.KeyPageId;
import com.bt.openlink.type.KeyPageLabel;
import com.bt.openlink.type.KeyPageLocalKeyPage;
import com.bt.openlink.type.KeyPageModule;
import com.bt.openlink.type.KeyQualifier;
import com.bt.openlink.type.Profile;

public class GetProfileResult extends OpenlinkIQ {

    private static final String DESCRIPTION = "get-profile result";
    @Nullable private final Profile profile;

    private GetProfileResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profile = builder.getProfile().orElse(null);
    }

    @Nonnull
    public Optional<Profile> getProfile() {
        return Optional.ofNullable(profile);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_PROFILE);

        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        if (parser.getName().equals(OpenlinkXmppNamespace.TAG_PROFILE)) {
            final Profile.Builder profileBuilder = Profile.Builder.start();
            Optional<Boolean> onlineOpt = SmackPacketUtil.getBooleanAttribute(parser, "online", DESCRIPTION, parseErrors);
            onlineOpt.ifPresent(profileBuilder::setOnline);
            Optional<DeviceId> deviceNumOpt = SmackPacketUtil.getStringAttribute(parser, "devicenum").flatMap(DeviceId::from);
            deviceNumOpt.ifPresent(profileBuilder::setDeviceId);
            parser.nextTag(); // Gets next tag keypages.
            parser.nextTag(); // Gets next tag keypage.
            final List<KeyPage> keyPages = new ArrayList<>();
            while (OpenlinkXmppNamespace.TAG_KEYPAGE.equals(parser.getName())) {
                final int currentKeyPageDepth = parser.getDepth();
                KeyPage.Builder keyPageBuilder = KeyPage.Builder.start();
                Optional<KeyPageId> keyPageIdOpt = SmackPacketUtil.getStringAttribute(parser, "id").flatMap(KeyPageId::from);
                keyPageIdOpt.ifPresent(keyPageBuilder::setkeypageId);
                Optional<KeyPageLabel> keyPageLabelOpt = SmackPacketUtil.getStringAttribute(parser, "label").flatMap(KeyPageLabel::from);
                keyPageLabelOpt.ifPresent(keyPageBuilder::setKeypageLabel);
                Optional<KeyPageModule> keyPageModuleOpt = SmackPacketUtil.getStringAttribute(parser, "module").flatMap(KeyPageModule::from);
                keyPageModuleOpt.ifPresent(keyPageBuilder::setKeypageModule);
                Optional<KeyPageLocalKeyPage> keyPageLocalOpt = SmackPacketUtil.getStringAttribute(parser, "local_keypage").flatMap(KeyPageLocalKeyPage::from);
                keyPageLocalOpt.ifPresent(keyPageBuilder::setLocalKeypage);
                parser.nextTag(); // Gets next tag key.
                final List<Key> keys = new ArrayList<>();
                while (OpenlinkXmppNamespace.TAG_KEY.equals(parser.getName())) {
                    final int currentKeyDepth = parser.getDepth();
                    Key.Builder keyBuilder = Key.Builder.start();
                    Optional<KeyId> keyIdOpt = SmackPacketUtil.getStringAttribute(parser, "id").flatMap(KeyId::from);
                    keyIdOpt.ifPresent(keyBuilder::setId);
                    Optional<KeyLabel> keyLabelOpt = SmackPacketUtil.getStringAttribute(parser, "label").flatMap(KeyLabel::from);
                    keyLabelOpt.ifPresent(keyBuilder::setLabel);
                    Optional<KeyFunction> keyFunctionOpt = SmackPacketUtil.getStringAttribute(parser, "function").flatMap(KeyFunction::from);
                    keyFunctionOpt.ifPresent(keyBuilder::setFunction);
                    Optional<KeyQualifier> keyQualifierOpt = SmackPacketUtil.getStringAttribute(parser, "qualifier").flatMap(KeyQualifier::from);
                    keyQualifierOpt.ifPresent(keyBuilder::setQualifier);
                    Optional<KeyModifier> keyModifierOpt = SmackPacketUtil.getStringAttribute(parser, "modifier").flatMap(KeyModifier::from);
                    keyModifierOpt.ifPresent(keyBuilder::setModifier);
                    Optional<KeyColor> keyColorOpt = SmackPacketUtil.getStringAttribute(parser, "color").flatMap(KeyColor::from);
                    keyColorOpt.ifPresent(keyBuilder::setColor);
                    Optional<KeyInterest> keyInterestOpt = SmackPacketUtil.getStringAttribute(parser, "interest").flatMap(KeyInterest::from);
                    keyInterestOpt.ifPresent(keyBuilder::setInterest);
                    keys.add(keyBuilder.build(parseErrors));
                    ParserUtils.forwardToEndTagOfDepth(parser, currentKeyDepth);
                    parser.nextTag(); // Gets next tag key if any more.
                }
                keyPageBuilder.addKeys(keys);
                keyPages.add(keyPageBuilder.build(parseErrors));
                parser.nextTag(); // Gets next tag keypage if any more.
            }
            profileBuilder.addKeyPages(keyPages);
            builder.setProfile(profileBuilder.build());
        }
        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed").attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_PROFILE.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri()).attribute("type", "output")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_OUT).rightAngleBracket();
        if (profile != null) {
            String encodedDeviceType;
            try {
                encodedDeviceType = URLEncoder.encode(getProfile().flatMap(Profile::getDeviceType).map(DeviceType::value).orElse(""), StandardCharsets.UTF_8.name());
            } catch (final UnsupportedEncodingException ignored) {
                encodedDeviceType = "Unknown";
            }
            xml.halfOpenElement(OpenlinkXmppNamespace.TAG_PROFILE)
                    .attribute("xmlns", OpenlinkXmppNamespace.OPENLINK_PROFILE.uri() + encodedDeviceType);
            profile.isOnline().ifPresent(online -> xml.attribute("online", online));
            profile.getDeviceId().ifPresent(deviceId -> xml.attribute("devicenum", deviceId.value()));
            xml.rightAngleBracket();
            xml.halfOpenElement(OpenlinkXmppNamespace.TAG_KEYPAGES).rightAngleBracket();
            profile.getKeyPages().forEach(keyPage -> {
                xml.halfOpenElement(OpenlinkXmppNamespace.TAG_KEYPAGE);
                keyPage.getKeyPageId().ifPresent(keyPageId -> xml.attribute("id", keyPageId.value()));
                keyPage.getKeyPageLabel().ifPresent(keyPageLabel -> xml.attribute("label", keyPageLabel.value()));
                keyPage.getKeyPageModule().ifPresent(keyPageModule -> xml.attribute("module", keyPageModule.value()));
                keyPage.getLocalKeyPage().ifPresent(localKeyPage -> xml.attribute("local_keypage", localKeyPage.value()));
                xml.rightAngleBracket();
                keyPage.getKeys().forEach(key -> {
                    xml.halfOpenElement(OpenlinkXmppNamespace.TAG_KEY);
                    key.getId().ifPresent(keyId -> xml.attribute("id", keyId.value()));
                    key.getLabel().ifPresent(keyLabel -> xml.attribute("label", keyLabel.value()));
                    key.getFunction().ifPresent(keyFunction -> xml.attribute("function", keyFunction.value()));
                    key.getQualifier().ifPresent(keyQualifier -> xml.attribute("qualifier", keyQualifier.value()));
                    key.getModifier().ifPresent(keyModifier -> xml.attribute("modifier", keyModifier.value()));
                    key.getColor().ifPresent(keyColor -> xml.attribute("color", keyColor.value()));
                    key.getInterest().ifPresent(keyInterest -> xml.attribute("interest", keyInterest.value()));
                    xml.closeEmptyElement();
                });
                xml.closeElement(OpenlinkXmppNamespace.TAG_KEYPAGE);
            });
            xml.closeElement(OpenlinkXmppNamespace.TAG_KEYPAGES);
            xml.closeElement(OpenlinkXmppNamespace.TAG_PROFILE);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends GetProfileResultBuilder<Builder, Jid, Type> {

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

        protected Builder() {
            super(Type.class);
        }

        @Nonnull
        public GetProfileResult build() {
            validate();
            return new GetProfileResult(this, null);
        }

        @Nonnull
        private GetProfileResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetProfileResult(this, parseErrors);
        }
    }

}
