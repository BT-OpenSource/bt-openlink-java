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
import com.bt.openlink.iq.MakeCallRequestBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.MakeCallFeature;
import com.bt.openlink.type.OriginatorReference;
import com.bt.openlink.type.PhoneNumber;

public class MakeCallRequest extends OpenlinkIQ {
    @Nullable private final Jid jid;
    @Nullable private final InterestId interestId;
    @Nullable private final PhoneNumber destination;
    @Nonnull private final List<MakeCallFeature> features;
    @Nonnull private final List<OriginatorReference> originatorReferences;

    private MakeCallRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        this.interestId = builder.getInterestId().orElse(null);
        this.destination = builder.getDestination().orElse(null);
        this.features = Collections.unmodifiableList(builder.getFeatures());
        this.originatorReferences = Collections.unmodifiableList(builder.getOriginatorReferences());
    }

    @Nonnull
    public Optional<Jid> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<PhoneNumber> getDestination() {
        return Optional.ofNullable(destination);
    }

    @Nonnull
    public List<MakeCallFeature> getFeatures() {
        return features;
    }

    @Nonnull
    public List<OriginatorReference> getOriginatorReferences() {
        return originatorReferences;
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);

        final ArrayList<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start();
        final int inDepth = parser.getDepth();
        parser.nextTag();
        while (parser.getDepth() > inDepth) {
            switch (parser.getName()) {
            case "jid":
                SmackPacketUtil.getSmackJid(parser.nextText()).ifPresent(builder::setJID);
                break;
            case OpenlinkXmppNamespace.TAG_INTEREST:
                InterestId.from(parser.nextText()).ifPresent(builder::setInterestId);
                break;
            case "destination":
                PhoneNumber.from(parser.nextText()).ifPresent(builder::setDestination);
                break;
            case OpenlinkXmppNamespace.TAG_FEATURES:
                getFeatures(parser, parseErrors, builder);
                break;
            case "originator-ref":
                SmackPacketUtil.getOriginatorRefs(parser).forEach(builder::addOriginatorReference);
                break;
            default:
                parseErrors.add("Unrecognised element:" + parser.getName());
                break;
            }
            ParserUtils.forwardToEndTagOfDepth(parser, inDepth + 1);
            parser.nextTag();
        }

        return builder.build(parseErrors);
    }

    private static void getFeatures(final XmlPullParser parser, final ArrayList<String> parseErrors, final Builder builder) throws XmlPullParserException, IOException {
        parser.nextTag();
        final int featureDepth = parser.getDepth();
        while (parser.getName().equals(OpenlinkXmppNamespace.TAG_FEATURE)) {
            final MakeCallFeature.Builder featureBuilder = MakeCallFeature.Builder.start();
            parser.nextTag();
            while (parser.getDepth() > featureDepth) {
                switch (parser.getName()) {
                    case "id":
                        FeatureId.from(parser.nextText()).ifPresent(featureBuilder::setFeatureId);
                        break;
                    case "value1":
                        SmackPacketUtil.getElementTextString(parser).ifPresent(featureBuilder::setValue1);
                        break;
                    case "value2":
                        SmackPacketUtil.getElementTextString(parser).ifPresent(featureBuilder::setValue2);
                        break;
                    default:
                        parseErrors.add("Unrecognised feature element:" + parser.getName());
                        break;
                }
                ParserUtils.forwardToEndTagOfDepth(parser, featureDepth + 1);
                parser.nextTag();
            }
            builder.addFeature(featureBuilder.build(parseErrors));
            parser.nextTag();
        }
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_MAKE_CALL.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_IN);
        xml.optElement("jid", jid);
        xml.optElement("interest", interestId);
        xml.optElement("destination", destination);
        SmackPacketUtil.addOriginatorReferences(xml, originatorReferences);

        if (!features.isEmpty()) {
            xml.openElement(OpenlinkXmppNamespace.TAG_FEATURES);

            for (final MakeCallFeature makeCallFeature : features)
            {
                xml.openElement(OpenlinkXmppNamespace.TAG_FEATURE);
                makeCallFeature.getFeatureId().ifPresent(id->xml.element("id", id.value()));
                makeCallFeature.getValue1().ifPresent(value1->xml.element("value1", value1));
                makeCallFeature.getValue2().ifPresent(value2->xml.element("value2", value2));
                xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURE);
            }

            xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURES);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends MakeCallRequestBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public MakeCallRequest build() {
            super.validate();
            return new MakeCallRequest(this, null);
        }

        @Nonnull
        private MakeCallRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new MakeCallRequest(this, errors);
        }

    }

}
