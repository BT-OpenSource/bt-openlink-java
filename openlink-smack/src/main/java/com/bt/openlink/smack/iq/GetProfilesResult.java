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
import com.bt.openlink.IQ.GetProfilesResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

public class GetProfilesResult extends OpenlinkIQ {
    @Nonnull private final List<Profile> profiles;

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_PROFILES);

        final Builder builder = Builder.start();

        final List<String> parseErrors = new ArrayList<>();
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            parseErrors.add("Invalid get-profiles result; missing 'profiles' element is mandatory");
        } else {
            moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_PROFILE);
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parseErrors.add("Invalid get-profiles result; no 'profile' elements present");
            }
        }
        while (OpenlinkXmppNamespace.TAG_PROFILE.equals(parser.getName())) {

            final int currentDepth = parser.getDepth();

            final Profile.Builder profileBuilder = Profile.Builder.start();
            final Optional<ProfileId> profileId = ProfileId.from(parser.getAttributeValue("", "id"));
            profileId.ifPresent(profileBuilder::setId);
            final Optional<Boolean> isDefaultProfile = SmackPacketUtil.getBooleanAttribute(parser, "default");
            isDefaultProfile.ifPresent(profileBuilder::setDefault);
            final Optional<String> label = SmackPacketUtil.getStringAttribute(parser, "label");
            label.ifPresent(profileBuilder::setLabel);
            final Optional<Boolean> online = SmackPacketUtil.getBooleanAttribute(parser, "online");
            online.ifPresent(profileBuilder::setOnline);
            final Optional<String> device = SmackPacketUtil.getStringAttribute(parser, "device");
            device.ifPresent(profileBuilder::setDevice);
            parser.nextTag();
            if (parser.getName().equals("site")) {
                final Site.Builder siteBuilder = Site.Builder.start();
                final Optional<Long> siteId = SmackPacketUtil.getLongAttribute(parser, "id");
                siteId.ifPresent(siteBuilder::setId);
                final Optional<Boolean> isDefaultSite = SmackPacketUtil.getBooleanAttribute(parser, "default");
                isDefaultSite.ifPresent(siteBuilder::setDefault);
                final Optional<Site.Type> siteType = Site.Type.from(SmackPacketUtil.getStringAttribute(parser, "type").orElse(null));
                siteType.ifPresent(siteBuilder::setType);
                parser.next();
                final Optional<String> siteName = Optional.ofNullable(parser.getText());
                siteName.ifPresent(siteBuilder::setName);
                profileBuilder.setSite(siteBuilder.buildWithoutValidating());
                ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
                parser.nextTag();
            }
            if (parser.getName().equals("actions") && !parser.isEmptyElementTag()) {
                do {
                    parser.nextTag();
                    if (parser.getName().equals("action")) {
                        final Optional<RequestAction> requestAction = RequestAction.from(SmackPacketUtil.getStringAttribute(parser, "id").orElse(null));
                        requestAction.ifPresent(profileBuilder::addAction);
                    }
                    ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
                } while (parser.getName().equals("action"));
                ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
                parser.nextTag();
            }

            builder.addProfile(profileBuilder.build(parseErrors));
            ParserUtils.forwardToEndTagOfDepth(parser, currentDepth);
            parser.nextTag();
        }
        return builder.build(parseErrors);
    }

    private GetProfilesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.getProfiles());
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_PROFILES.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "output")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_OUT).rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_PROFILES).attribute("xmlns", "http://xmpp.org/protocol/openlink:01:00:00/profiles").rightAngleBracket();
        for (final Profile profile : profiles) {
            xml.halfOpenElement(OpenlinkXmppNamespace.TAG_PROFILE);
            profile.getId().ifPresent(profileId -> xml.attribute("id", profileId.value()));
            profile.isDefaultProfile().ifPresent(isDefault -> xml.attribute("default", isDefault));
            profile.getDevice().ifPresent(device -> xml.attribute("device", device));
            profile.getLabel().ifPresent(label -> xml.attribute("label", label));
            profile.isOnline().ifPresent(online -> xml.attribute("online", online));
            xml.rightAngleBracket();
            profile.getSite().ifPresent(site -> {
                xml.halfOpenElement("site");
                site.getId().ifPresent(id -> xml.attribute("id", String.valueOf(id)));
                site.isDefault().ifPresent(isDefault -> xml.attribute("default", String.valueOf(isDefault)));
                site.getType().ifPresent(type -> xml.attribute("type", type.name()));
                xml.rightAngleBracket();
                site.getName().ifPresent(xml::escape);
                xml.closeElement("site");

            });
            final List<RequestAction> actions = profile.getActions();
            if (!actions.isEmpty()) {
                xml.openElement("actions");
                actions.forEach(action -> {
                    xml.halfOpenElement("action");
                    xml.attribute("id", action.getId());
                    xml.attribute("label", action.getLabel());
                    xml.closeEmptyElement();
                });
                xml.closeElement("actions");
            }
            xml.closeElement(OpenlinkXmppNamespace.TAG_PROFILE);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_PROFILES);
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @Nonnull
    public List<Profile> getProfiles() {
        return profiles;
    }

    public static final class Builder extends GetProfilesResultBuilder<Builder, Jid, IQ.Type> {

        private Builder() {
        }

        @Nonnull
        @Override
        public IQ.Type getExpectedIQType() {
            return Type.result;
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public static Builder start(@Nonnull final GetProfilesRequest request) {
            return start().setId(request.getStanzaId())
                    .setFrom(request.getTo())
                    .setTo(request.getFrom());
        }

        @Nonnull
        public GetProfilesResult build() {
            super.validate();
            return new GetProfilesResult(this, null);
        }

        @Nonnull
        private GetProfilesResult build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new GetProfilesResult(this, errors);
        }

    }
}
