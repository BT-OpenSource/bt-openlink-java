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
import com.bt.openlink.iq.GetFeaturesResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.ProfileId;

public class GetFeaturesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-features result";
    @Nullable private final ProfileId profileId;
    @Nonnull private final List<Feature> features;

    private GetFeaturesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
        this.features = Collections.unmodifiableList(builder.getFeatures());
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

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public List<Feature> getFeatures() {
        return features;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetFeaturesResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final GetFeaturesResult.Builder builder = GetFeaturesResult.Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element profileElement = TinderPacketUtil.getChildElement(outElement, "profile");
        ProfileId.from(TinderPacketUtil.getNullableStringAttribute(profileElement, "id")).ifPresent(builder::setProfileId);
        final Element featuresElement = TinderPacketUtil.getChildElement(outElement, "features");
        if (featuresElement == null) {
            parseErrors.add("Invalid get-features result; missing 'features' element is mandatory");
        } else {
            final List<Element> featureElements = featuresElement.elements("feature");
            for (final Element featureElement : featureElements) {
                final Feature.Builder featureBuilder = Feature.Builder.start();
                FeatureId.from(TinderPacketUtil.getNullableStringAttribute(featureElement, "id", true, DESCRIPTION, parseErrors)).ifPresent(featureBuilder::setId);
                final Optional<String> featureTypeString = TinderPacketUtil.getStringAttribute(featureElement, "type", true, DESCRIPTION, parseErrors);
                featureTypeString.ifPresent(featureType -> {
                    final Optional<FeatureType> type = FeatureType.from(featureType);
                    if (type.isPresent()) {
                        featureBuilder.setType(type.get());
                    } else {
                        parseErrors.add(String.format("Invalid %s; invalid feature type - '%s'", DESCRIPTION, featureType));
                    }
                });
                TinderPacketUtil.getStringAttribute(featureElement, "label", true, DESCRIPTION, parseErrors).ifPresent(featureBuilder::setLabel);
                builder.addFeature(featureBuilder.build(parseErrors));
            }
        }

        final GetFeaturesResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends GetFeaturesResultBuilder<Builder, JID, Type> {

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
         * IQ.Type.set} IQ. The new packet will be initialized with:
         * <ul>
         *
         * <li>The sender set to the recipient of the originating IQ.
         * <li>The recipient set to the sender of the originating IQ.
         * <li>The type set to {@link Type#result IQ.Type.result}.
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
        public static Builder createResultBuilder(@Nonnull final GetFeaturesRequest request) {
            final Builder builder = start(IQ.createResultIQ(request));
            request.getProfileId().ifPresent(builder::setProfileId);
            return builder;
        }

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetFeaturesResult build() {
            validate();
            return new GetFeaturesResult(this, null);
        }

        @Nonnull
        private GetFeaturesResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetFeaturesResult(this, parseErrors);
        }
    }
}
