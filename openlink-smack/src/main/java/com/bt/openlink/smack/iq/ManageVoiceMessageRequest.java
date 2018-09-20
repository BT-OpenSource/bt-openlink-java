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
import com.bt.openlink.iq.ManageVoiceMessageRequestBuilder;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;
import com.bt.openlink.type.ProfileId;

public class ManageVoiceMessageRequest extends OpenlinkIQ {

    @Nullable private ProfileId profileId;
    @Nullable private ManageVoiceMessageAction action;
    @Nullable private String label;
    @Nonnull private final List<FeatureId> features;


    private ManageVoiceMessageRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
        this.label = builder.getLabel().orElse(null);
        this.action = builder.getAction().orElse(null);
        this.features = Collections.unmodifiableList(builder.getFeatures());
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute(OpenlinkXmppNamespace.TAG_ACTION, "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_MANAGE_VOICE_MESSAGE.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_IN);

        xml.optElement("profile", profileId);
        xml.optElement(OpenlinkXmppNamespace.TAG_ACTION, Optional.ofNullable(action).map(ManageVoiceMessageAction::getId).orElse(null));
        xml.optElement("label", label);
        if (!features.isEmpty()) {
            xml.openElement(OpenlinkXmppNamespace.TAG_FEATURES);

            for (FeatureId feature : features) {
                xml.openElement(OpenlinkXmppNamespace.TAG_FEATURE);
                xml.element("id", feature.value());
                xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURE);
            }


            xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURES);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);

        final ArrayList<String> parseErrors = new ArrayList<>();
        final ManageVoiceMessageRequest.Builder builder = ManageVoiceMessageRequest.Builder.start();
        final int inDepth = parser.getDepth();
        parser.nextTag();
        while (parser.getDepth() > inDepth) {

            switch (parser.getName()) {
                case OpenlinkXmppNamespace.TAG_PROFILE:
                    ProfileId.from(parser.nextText()).ifPresent(builder::setProfileId);
                    break;
                case OpenlinkXmppNamespace.TAG_ACTION:
                    ManageVoiceMessageAction.from(parser.nextText()).ifPresent(builder::setAction);
                    break;
                case "label":
                    builder.setLabel(parser.nextText());
                    break;
                case OpenlinkXmppNamespace.TAG_FEATURES:
                    getFeatures(parser, parseErrors, builder);
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

    private static void getFeatures(final XmlPullParser parser,
                                    final ArrayList<String> parseErrors,
                                    final ManageVoiceMessageRequest.Builder builder) throws XmlPullParserException, IOException {
        parser.nextTag();
        final int featureDepth = parser.getDepth();
        while (parser.getName().equals(OpenlinkXmppNamespace.TAG_FEATURE)) {
            parser.nextTag();
            while (parser.getDepth() > featureDepth) {
                if (parser.getName().equals("id")) {
                    FeatureId.from(parser.nextText()).ifPresent(builder::addFeature);
                } else {
                    parseErrors.add("Unrecognised feature element:" + parser.getName());
                }
                ParserUtils.forwardToEndTagOfDepth(parser, featureDepth + 1);
                parser.nextTag();
            }
            parser.nextTag();
        }
    }

    public static final class Builder extends ManageVoiceMessageRequestBuilder<Builder, Jid, Type> {

        @Nonnull
        public static ManageVoiceMessageRequest.Builder start() {
            return new ManageVoiceMessageRequest.Builder();
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

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

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
}
