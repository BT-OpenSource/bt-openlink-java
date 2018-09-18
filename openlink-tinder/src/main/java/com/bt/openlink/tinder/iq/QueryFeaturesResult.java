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
import com.bt.openlink.iq.QueryFeaturesResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.ActiveFeature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;

public class QueryFeaturesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "query-features result";
    @Nonnull private final List<ActiveFeature> features;

    private QueryFeaturesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.features = Collections.unmodifiableList(builder.getFeatures());
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_QUERY_FEATURES);
        final Element featuresElement = outElement.addElement("features");
        for (final ActiveFeature feature : features) {
            final Element featureElement = featuresElement.addElement("feature");
            feature.getId().ifPresent(id -> featureElement.addAttribute("id", id.value()));
            feature.getType().ifPresent(type -> featureElement.addAttribute("type", type.getId()));
            feature.getLabel().ifPresent(label -> featureElement.addAttribute("label", label));
            feature.getValue1().ifPresent(value1 -> featureElement.addAttribute("value1", value1));
            feature.getValue2().ifPresent(value2 -> featureElement.addAttribute("value2", value2));
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public List<ActiveFeature> getFeatures() {
        return features;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static QueryFeaturesResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final QueryFeaturesResult.Builder builder = QueryFeaturesResult.Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element featuresElement = TinderPacketUtil.getChildElement(outElement, "features");
        if (featuresElement == null) {
            parseErrors.add("Invalid query-features result; missing 'features' element is mandatory");
        } else {
            final List<Element> featureElements = featuresElement.elements("feature");
            for (final Element featureElement : featureElements) {
                final ActiveFeature.Builder featureBuilder = ActiveFeature.Builder.start();
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
                TinderPacketUtil.getStringAttribute(featureElement, "value1").ifPresent(featureBuilder::setValue1);
                TinderPacketUtil.getStringAttribute(featureElement, "value2").ifPresent(featureBuilder::setValue2);
                builder.addFeature(featureBuilder.build(parseErrors));
            }
        }

        final QueryFeaturesResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends QueryFeaturesResultBuilder<Builder, JID, Type> {

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
        public QueryFeaturesResult build() {
            validate();
            return new QueryFeaturesResult(this, null);
        }

        @Nonnull
        private QueryFeaturesResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new QueryFeaturesResult(this, parseErrors);
        }
    }
}
