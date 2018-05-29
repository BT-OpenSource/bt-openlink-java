package com.bt.openlink.smack.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.SetFeaturesRequestBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ProfileId;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SetFeaturesRequest extends OpenlinkIQ {

    @Nullable private ProfileId profileId;
    @Nullable private FeatureId featureId;
    @Nullable private String value1;
    @Nullable private String value2;
    @Nullable private String value3;

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<FeatureId> getFeatureId() {
        return Optional.ofNullable(featureId);
    }

    @Nonnull
    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    @Nonnull
    public Optional<String> getValue3() {
        return Optional.ofNullable(value3);
    }

    private SetFeaturesRequest(@Nonnull SetFeaturesRequest.Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
        this.featureId = builder.getFeatureId().orElse(null);
        this.value1 = builder.getValue1().orElse(null);
        this.value2 = builder.getValue2().orElse(null);
        this.value3 = builder.getValue3().orElse(null);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_SET_FEATURES.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();

        xml.optElement("profile", profileId);
        xml.optElement("feature", featureId);
        xml.optElement("value1", value1);
        xml.optElement("value2", value2);
        xml.optElement("value3", value3);

        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);

        final ArrayList<String> parseErrors = new ArrayList<>();
        final SetFeaturesRequest.Builder builder = SetFeaturesRequest.Builder.start();
        final int inDepth = parser.getDepth();
        parser.nextTag();
        while (parser.getDepth() > inDepth) {

            switch (parser.getName()) {
            case OpenlinkXmppNamespace.TAG_PROFILE:
                ProfileId.from(parser.nextText()).ifPresent(builder::setProfileId);
                break;
            case OpenlinkXmppNamespace.TAG_FEATURE:
                FeatureId.from(parser.nextText()).ifPresent(builder::setFeatureId);
                break;
            case "value1":
                SmackPacketUtil.getElementTextString(parser).ifPresent(builder::setValue1);
                break;
            case "value2":
                SmackPacketUtil.getElementTextString(parser).ifPresent(builder::setValue1);
                break;
            case "value3":
                SmackPacketUtil.getElementTextString(parser).ifPresent(builder::setValue1);
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

    public static final class Builder extends SetFeaturesRequestBuilder<Builder, Jid, Type> {

        protected Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public SetFeaturesRequest build() {
            super.validate();
            return new SetFeaturesRequest(this, null);
        }

        @Nonnull
        private SetFeaturesRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new SetFeaturesRequest(this, errors);
        }
    }
}
