package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.ManageVoiceMessageRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;
import com.bt.openlink.type.ProfileId;

public class ManageVoiceMessageRequest extends OpenlinkIQ {
    @Nullable private ProfileId profileId;
    @Nullable private ManageVoiceMessageAction action;
    @Nullable private String label;
    @Nonnull private final List<FeatureId> features;

    private ManageVoiceMessageRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
        this.label = builder.getLabel().orElse(null);
        this.action = builder.getAction().orElse(null);
        this.features = Collections.unmodifiableList(builder.getFeatures());
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_MANAGE_VOICE_MESSAGE);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "profile", profileId);
        getAction().map(ManageVoiceMessageAction::getId).ifPresent(action -> TinderPacketUtil.addElementWithTextIfNotNull(inElement, "action", action));
        if (!features.isEmpty()) {
            final Element featuresElement = inElement.addElement("features");
            features.forEach(feature -> featuresElement.addElement("feature").addElement("id").setText(feature.value()));
        }
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<ManageVoiceMessageAction> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public List<FeatureId> getFeatures() {
        return features;
    }

    @Nonnull
    public static ManageVoiceMessageRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        ProfileId.from(TinderPacketUtil.getNullableChildElementString(inElement, "profile")).ifPresent(builder::setProfileId);
        ManageVoiceMessageAction.from(TinderPacketUtil.getNullableChildElementString(inElement, "action")).ifPresent(builder::setAction);
        final Element featuresElement = TinderPacketUtil.getChildElement(inElement, "features");
        if (featuresElement != null) {
            final List featureElements = featuresElement.elements("feature");
            for (final Object featureElement : featureElements) {
                TinderPacketUtil.getOptionalChildElementString((Element) featureElement, "id").flatMap(FeatureId::from).ifPresent(builder::addFeature);
            }
        }
        final ManageVoiceMessageRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends ManageVoiceMessageRequestBuilder<Builder, JID, Type> {

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

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public ManageVoiceMessageRequest build() {
            super.validate();
            return new ManageVoiceMessageRequest(this, null);
        }

        @Nonnull
        private ManageVoiceMessageRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new ManageVoiceMessageRequest(this, errors);
        }

    }

}
