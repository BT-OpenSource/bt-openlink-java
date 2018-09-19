package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.bt.openlink.iq.GetFeaturesResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.ProfileId;

public class GetFeaturesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-features result";
    @Nullable private final ProfileId profileId;
    @Nonnull private final List<Feature> features;

    private GetFeaturesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
        this.features = Collections.unmodifiableList(builder.getFeatures());
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public List<Feature> getFeatures() {
        return features;
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_PROFILE);
        final Builder builder = Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        if (!parser.getName().equals(OpenlinkXmppNamespace.TAG_PROFILE)) {
            parseErrors.add(" Invalid get-features result; missing 'features' element is mandatory");
        } else {
            final Optional<ProfileId> profileId = ProfileId.from(parser.getAttributeValue("", "id"));
            profileId.ifPresent(builder::setProfileId);

            endPreviousTagAndStartNextTag(parser);

            if (!parser.getName().equals(OpenlinkXmppNamespace.TAG_FEATURES)) {
                parseErrors.add("Invalid get-features result; missing 'features' element is mandatory");
            } else {
                parser.nextTag();
                while (OpenlinkXmppNamespace.TAG_FEATURE.equals(parser.getName())) {

                    final int currentDepth = parser.getDepth();

                    final Feature.Builder featureBuilder = Feature.Builder.start();
                    final Optional<FeatureId> featureId = FeatureId.from(parser.getAttributeValue("", "id"));
                    featureId.ifPresent(featureBuilder::setId);
                    final Optional<String> featureTypeString = SmackPacketUtil.getStringAttribute(parser, "type");
                    featureTypeString.ifPresent(featureType -> {
                        final Optional<FeatureType> type = FeatureType.from(featureType);
                        if (type.isPresent()) {
                            featureBuilder.setType(type.get());
                        } else {
                            parseErrors.add(String.format("Invalid %s; invalid feature type - '%s'", DESCRIPTION, featureType));
                        }
                    });
                    final Optional<String> label = SmackPacketUtil.getStringAttribute(parser,
                            OpenlinkXmppNamespace.TAG_LABEL);
                    label.ifPresent(featureBuilder::setLabel);

                    builder.addFeature(featureBuilder.build(parseErrors));
                    ParserUtils.forwardToEndTagOfDepth(parser, currentDepth);
                    parser.nextTag();
                }
            }
        }
        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_FEATURES.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "output")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_OUT).rightAngleBracket();
        getProfileId().ifPresent(id -> xml.halfOpenElement(OpenlinkXmppNamespace.TAG_PROFILE).attribute("id", id.value()));
        xml.rightAngleBracket();
        xml.closeElement(OpenlinkXmppNamespace.TAG_PROFILE);
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_FEATURES).attribute("xmlns", "http://xmpp.org/protocol/openlink:01:00:00/features").rightAngleBracket();
        for (final Feature feature : features) {
            xml.halfOpenElement(OpenlinkXmppNamespace.TAG_FEATURE);
            feature.getId().ifPresent(id -> xml.attribute("id", id.value()));
            feature.getLabel().ifPresent(label -> xml.attribute(OpenlinkXmppNamespace.TAG_LABEL, label));
            feature.getType().ifPresent(type -> xml.attribute("type", type.getId()));
            xml.rightAngleBracket();

            xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURE);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURES);
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends GetFeaturesResultBuilder<Builder, Jid, Type> {

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
         * <li>If the request is a {@link GetFeaturesRequest} the profileId set to the profileId in the request</li>
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
            final Builder builder = SmackPacketUtil.createResultBuilder(start(), request);
            if (request instanceof GetFeaturesRequest) {
                ((GetFeaturesRequest) request).getProfileId().ifPresent(builder::setProfileId);
            }
            return builder;
        }

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetFeaturesResult build() {
            super.validate();
            return new GetFeaturesResult(this, null);
        }

        @Nonnull
        private GetFeaturesResult build(@Nonnull final List<String> parseErrors) {
            super.validate(parseErrors, false);
            return new GetFeaturesResult(this, parseErrors);
        }
    }
}
