package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.ProfileId;

public class GetFeaturesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-features result";
    @Nullable private final ProfileId profileId;
    @Nonnull private final List<Feature> features;

    private GetFeaturesResult(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.profileId;
        this.features = Collections.unmodifiableList(builder.features);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_FEATURES);
        final Element profileElement = outElement.addElement("profile");
        getProfileId().ifPresent(id -> profileElement.addAttribute("id", id.value()));
        final Element featuresElement = outElement.addElement("features", OpenlinkXmppNamespace.OPENLINK_FEATURES.uri());
        for (final Feature feature : features) {
            final Element featureElement = featuresElement.addElement("feature");
            feature.getId().ifPresent(id -> featureElement.addAttribute("id", id.value()));
            feature.getType().ifPresent(type -> featureElement.addAttribute("type", type.getId()));
            feature.getLabel().ifPresent(label -> featureElement.addAttribute("label", label));
        }
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public List<Feature> getFeatures() {
        return features;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetFeaturesResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final GetFeaturesResult.Builder builder = GetFeaturesResult.Builder.start()
                .setTo(iq.getTo())
                .setFrom(iq.getFrom())
                .setID(iq.getID())
                .setType(iq.getType());
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element profileElement = TinderPacketUtil.getChildElement(outElement, "profile");
        if (profileElement == null) {
            parseErrors.add(String.format("Invalid %s; missing 'profile' element is mandatory", DESCRIPTION));
        } else {
            final Optional<ProfileId> profileId = ProfileId.from(profileElement.attributeValue("id"));
            profileId.ifPresent(builder::setProfileId);
        }

        final Element featuresElement = TinderPacketUtil.getChildElement(outElement, "features");
        if (featuresElement == null) {
            parseErrors.add("Invalid get-features result; missing 'features' element is mandatory");
        } else {
            final List<Element> featureElements = featuresElement.elements("feature");
            for (final Element featureElement : featureElements) {
                final Feature.Builder featureBuilder = Feature.Builder.start();
                final Optional<FeatureId> featureId = FeatureId.from(TinderPacketUtil.getStringAttribute(featureElement, "id", true, DESCRIPTION, parseErrors).orElse(null));
                featureId.ifPresent(featureBuilder::setId);
                final Optional<FeatureType> featureType = FeatureType.from(TinderPacketUtil.getStringAttribute(featureElement, "type", true, DESCRIPTION, parseErrors).orElse(null));
                featureType.ifPresent(featureBuilder::setType);
                final Optional<String> label = Optional.ofNullable(TinderPacketUtil.getStringAttribute(featureElement, "label", true, DESCRIPTION, parseErrors).orElse(null));
                label.ifPresent(featureBuilder::setLabel);
                builder.addFeature(featureBuilder.build());
            }
        }

        final GetFeaturesResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nullable ProfileId profileId;
        @Nonnull private List<Feature> features = new ArrayList<>();

        private Builder() {
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.result;
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public GetFeaturesResult build() {
            validateBuilder();
            if (profileId == null) {
                throw new IllegalStateException("The profileId has not been set");
            }
            return new GetFeaturesResult(this, Collections.emptyList());
        }

        @Nonnull
        private GetFeaturesResult build(final List<String> parseErrors) {
            return new GetFeaturesResult(this, parseErrors);
        }

        public Builder setProfileId(@Nonnull ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        @Nonnull
        public Builder addFeature(@Nonnull final Feature feature) {
            this.features.forEach(existingFeature -> {
                if (existingFeature.getId().equals(feature.getId())) {
                    throw new IllegalArgumentException("The feature id must be unique");
                }
            });
            this.features.add(feature);
            return this;
        }

        public static Builder start(final GetFeaturesRequest request) {
            final Builder builder = new Builder()
                    .setID(request.getID())
                    .setFrom(request.getTo())
                    .setTo(request.getFrom());
            request.getProfileId().ifPresent(builder::setProfileId);
            return builder;
        }
    }

}
