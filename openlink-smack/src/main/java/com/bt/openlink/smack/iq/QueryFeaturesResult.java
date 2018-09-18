package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.QueryFeaturesResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.ActiveFeature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;

public class QueryFeaturesResult extends OpenlinkIQ {

    @Nonnull private final List<ActiveFeature> features;

    private QueryFeaturesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.features = Collections.unmodifiableList(builder.getFeatures());
    }

    @Nonnull
    public List<ActiveFeature> getFeatures() {
        return features;
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        final ArrayList<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start();
        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_FEATURES);
        final int featuresDepth = parser.getDepth();
        parser.nextTag();
        while (parser.getDepth() > featuresDepth) {
            while (parser.getEventType() == XmlPullParser.START_TAG && "feature".equals(parser.getName())) {
                final ActiveFeature.Builder featureBuilder = ActiveFeature.Builder.start();
                SmackPacketUtil.getStringAttribute(parser, "id").flatMap(FeatureId::from).ifPresent(featureBuilder::setId);
                SmackPacketUtil.getStringAttribute(parser, "type").flatMap(FeatureType::from).ifPresent(featureBuilder::setType);
                SmackPacketUtil.getStringAttribute(parser, "label").ifPresent(featureBuilder::setLabel);
                SmackPacketUtil.getStringAttribute(parser, "value1").ifPresent(featureBuilder::setValue1);
                SmackPacketUtil.getStringAttribute(parser, "value2").ifPresent(featureBuilder::setValue2);
                builder.addFeature(featureBuilder.build(parseErrors));
                ParserUtils.forwardToEndTagOfDepth(parser, featuresDepth + 1);
            }
            parser.nextTag();
        }

        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_QUERY_FEATURES.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "output")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.openElement(OpenlinkXmppNamespace.TAG_FEATURES);
        features.forEach(feature -> xml.halfOpenElement(OpenlinkXmppNamespace.TAG_FEATURE)
                .optAttribute("id", feature.getId().map(FeatureId::value).orElse(null))
                .optAttribute("type", feature.getType().map(FeatureType::getId).orElse(null))
                .optAttribute("label", feature.getLabel().orElse(null))
                .optAttribute("value1", feature.getValue1().orElse(null))
                .optAttribute("value2", feature.getValue2().orElse(null))
                .closeEmptyElement());
        xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURES);
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends QueryFeaturesResultBuilder<Builder, Jid, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public QueryFeaturesResult build() {
            super.validate();
            return new QueryFeaturesResult(this, null);
        }

        @Nonnull
        private QueryFeaturesResult build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new QueryFeaturesResult(this, errors);
        }
    }

}
