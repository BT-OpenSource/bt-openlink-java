package com.bt.openlink.tinder.iq;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetProfileResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
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
        super(builder, parseErrors);
        this.profile = builder.getProfile().orElse(null);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILE);
        String encodedDeviceType;
        try {
            encodedDeviceType = URLEncoder.encode(getProfile().flatMap(Profile::getDeviceType).map(DeviceType::value).orElse(""), StandardCharsets.UTF_8.name());
        } catch (final UnsupportedEncodingException ignored) {
            encodedDeviceType = "Unknown";
        }

        final Element profileElement = outElement.addElement("profile", OpenlinkXmppNamespace.OPENLINK_PROFILE.uri() + encodedDeviceType);
        if (this.profile != null) {
            this.profile.isOnline().ifPresent(online -> profileElement.addAttribute("online", String.valueOf(online)));
            this.profile.getDeviceId().ifPresent(deviceId -> profileElement.addAttribute("devicenum", deviceId.value()));
            final Element keyPagesElement = profileElement.addElement(OpenlinkXmppNamespace.TAG_KEYPAGES);
            profile.getKeyPages().forEach(keyPage -> {
                final Element keyPageElement = keyPagesElement.addElement(OpenlinkXmppNamespace.TAG_KEYPAGE);
                keyPage.getKeyPageId().ifPresent(keyPageId -> keyPageElement.addAttribute("id", keyPageId.value()));
                keyPage.getKeyPageLabel().ifPresent(keyPageLabel -> keyPageElement.addAttribute("label", keyPageLabel.value()));
                keyPage.getKeyPageModule().ifPresent(keyPageModule -> keyPageElement.addAttribute("module", keyPageModule.value()));
                keyPage.getLocalKeyPage().ifPresent(localKeyPage -> keyPageElement.addAttribute("local_keypage", localKeyPage.value()));
                keyPage.getKeys().forEach(key -> {
                    final Element keyElement = keyPageElement.addElement("key");
                    key.getId().ifPresent(keyId -> keyElement.addAttribute("id", keyId.value()));
                    key.getLabel().ifPresent(keyLabel -> keyElement.addAttribute("label", keyLabel.value()));
                    key.getFunction().ifPresent(keyFunction -> keyElement.addAttribute("function", keyFunction.value()));
                    key.getQualifier().ifPresent(keyQualifier -> keyElement.addAttribute("qualifier", keyQualifier.value()));
                    key.getModifier().ifPresent(keyModifier -> keyElement.addAttribute("modifier", keyModifier.value()));
                    key.getColor().ifPresent(keyColor -> keyElement.addAttribute("color", keyColor.value()));
                    key.getInterest().ifPresent(keyInterest -> keyElement.addAttribute("interest", keyInterest.value()));
                });
            });

        }
    }

    @Nonnull
    public Optional<Profile> getProfile() {
        return Optional.ofNullable(profile);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetProfileResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element profileElement = TinderPacketUtil.getChildElement(outElement, "profile");
        if (profileElement != null) {
            final Profile.Builder profileBuilder = Profile.Builder.start();
            String decodedNamespace;
            try {
                decodedNamespace = URLDecoder.decode(profileElement.getNamespace().getStringValue(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                decodedNamespace = "Unknown";
            }
            if (decodedNamespace.startsWith(OpenlinkXmppNamespace.OPENLINK_PROFILE.uri())) {
                DeviceType.from(decodedNamespace.substring(OpenlinkXmppNamespace.OPENLINK_PROFILE.uri().length())).ifPresent(profileBuilder::setDeviceType);
            }
            TinderPacketUtil.getBooleanAttribute(profileElement, "online", DESCRIPTION, parseErrors).ifPresent(profileBuilder::setOnline);
            DeviceId.from(TinderPacketUtil.getNullableStringAttribute(profileElement, "devicenum")).ifPresent(profileBuilder::setDeviceId);
            final Element keyPagesElement = TinderPacketUtil.getChildElement(profileElement, OpenlinkXmppNamespace.TAG_KEYPAGES);
            if (null != keyPagesElement) {
                final List<Element> keyPageElements = keyPagesElement.elements(OpenlinkXmppNamespace.TAG_KEYPAGE);
                profileBuilder.addKeyPages(keyPageElements.stream()
                        .map(keyPageElement -> {
                            final KeyPage.Builder keyPageBuilder = KeyPage.Builder.start();
                            KeyPageId.from(TinderPacketUtil.getNullableStringAttribute(keyPageElement, "id")).ifPresent(keyPageBuilder::setkeypageId);
                            KeyPageLabel.from(TinderPacketUtil.getNullableStringAttribute(keyPageElement, "label")).ifPresent(keyPageBuilder::setKeypageLabel);
                            KeyPageModule.from(TinderPacketUtil.getNullableStringAttribute(keyPageElement, "module")).ifPresent(keyPageBuilder::setKeypageModule);
                            KeyPageLocalKeyPage.from(TinderPacketUtil.getNullableStringAttribute(keyPageElement, "local_keypage")).ifPresent(keyPageBuilder::setLocalKeypage);
                            final List<Element> keyElements = keyPageElement.elements("key");
                            keyPageBuilder.addKeys(keyElements.stream()
                                    .map(keyElement -> {
                                        Key.Builder keyBuilder = Key.Builder.start();
                                        KeyId.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "id")).ifPresent(keyBuilder::setId);
                                        KeyLabel.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "label")).ifPresent(keyBuilder::setLabel);
                                        KeyFunction.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "function")).ifPresent(keyBuilder::setFunction);
                                        KeyQualifier.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "qualifier")).ifPresent(keyBuilder::setQualifier);
                                        KeyModifier.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "modifier")).ifPresent(keyBuilder::setModifier);
                                        KeyColor.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "color")).ifPresent(keyBuilder::setColor);
                                        KeyInterest.from(TinderPacketUtil.getNullableStringAttribute(keyElement, "interest")).ifPresent(keyBuilder::setInterest);
                                        keyBuilder.build(parseErrors);
                                        return keyBuilder.build(parseErrors);
                                    })
                                    .collect(Collectors.toList()));
                            return keyPageBuilder.build(parseErrors);
                        })
                        .collect(Collectors.toList()));
            }
            builder.setProfile((profileBuilder.build(parseErrors)));
        }
        final GetProfileResult result = builder.build(parseErrors);
        result.setID(iq.getID());
        return result;
    }

    public static final class Builder extends GetProfileResultBuilder<Builder, JID, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            final Builder builder = start();
            TinderIQBuilder.setIQBuilder(builder, iq);
            return builder;
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
            return start(IQ.createResultIQ(request));
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
