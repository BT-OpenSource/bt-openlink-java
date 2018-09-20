package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.SetFeaturesRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ProfileId;

public class SetFeaturesRequest extends OpenlinkIQ {
    @Nullable private ProfileId profileId;
    @Nullable private FeatureId featureId;
    @Nullable private String value1;
    @Nullable private String value2;
    @Nullable private String value3;

    private SetFeaturesRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
        this.featureId = builder.getFeatureId().orElse(null);
        this.value1 = builder.getValue1().orElse(null);
        this.value2 = builder.getValue2().orElse(null);
        this.value3 = builder.getValue3().orElse(null);
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_SET_FEATURES);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "profile", profileId);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "feature", featureId);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "value1", value1);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "value2", value2);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "value3", value3);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<FeatureId> getFeatureId() {
        return Optional.ofNullable(featureId);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<String> getValue3() {
        return Optional.ofNullable(value3);
    }

    @Nonnull
    public static SetFeaturesRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        ProfileId.from(TinderPacketUtil.getNullableChildElementString(inElement, "profile")).ifPresent(builder::setProfileId);
        FeatureId.from(TinderPacketUtil.getNullableChildElementString(inElement, "feature")).ifPresent(builder::setFeatureId);
        TinderPacketUtil.getOptionalChildElementString(inElement, "value1").ifPresent(builder::setValue1);
        TinderPacketUtil.getOptionalChildElementString(inElement, "value2").ifPresent(builder::setValue2);
        TinderPacketUtil.getOptionalChildElementString(inElement, "value3").ifPresent(builder::setValue3);
        final SetFeaturesRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends SetFeaturesRequestBuilder<Builder, JID, Type> {

        protected Builder() {
            super(IQ.Type.class);
        }

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

        @Nonnull
        public SetFeaturesRequest build() {
            super.validate();
            return new SetFeaturesRequest(this, null);
        }

        @Nonnull
        private SetFeaturesRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new SetFeaturesRequest(this, errors);
        }
    }

}
