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
import com.bt.openlink.iq.GetProfilesResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.DeviceId;
import com.bt.openlink.type.DeviceType;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

public class GetProfilesResult extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "get-profiles result";
    @Nonnull private final List<Profile> profiles;

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_PROFILES, OpenlinkXmppNamespace.TAG_PROFILE);

        final Builder builder = Builder.start();

        final List<String> parseErrors = new ArrayList<>();
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            parseErrors.add("Invalid get-profiles result; no profiles present");
        }
        while (OpenlinkXmppNamespace.TAG_PROFILE.equals(parser.getName())) {

            final int profileDepth = parser.getDepth();

            final Profile.Builder profileBuilder = Profile.Builder.start();
            final Optional<ProfileId> profileId = ProfileId.from(parser.getAttributeValue("", "id"));
            profileId.ifPresent(profileBuilder::setId);
            final Optional<Boolean> isDefaultProfile = SmackPacketUtil.getBooleanAttribute(parser, OpenlinkXmppNamespace.TAG_DEFAULT, STANZA_DESCRIPTION, parseErrors);
            isDefaultProfile.ifPresent(profileBuilder::setDefault);
            final Optional<String> label = SmackPacketUtil.getStringAttribute(parser, OpenlinkXmppNamespace.TAG_LABEL);
            label.ifPresent(profileBuilder::setLabel);
            final Optional<Boolean> online = SmackPacketUtil.getBooleanAttribute(parser, "online", STANZA_DESCRIPTION, parseErrors);
            online.ifPresent(profileBuilder::setOnline);
            final Optional<String> deviceType = SmackPacketUtil.getStringAttribute(parser, "device");
            deviceType.flatMap(DeviceType::from).ifPresent(profileBuilder::setDeviceType);
            final Optional<String> deviceId = SmackPacketUtil.getStringAttribute(parser, "devicenum");
            deviceId.flatMap(DeviceId::from).ifPresent(profileBuilder::setDeviceId);

            parser.nextTag();
            do {
                switch (parser.getName()) {
                case "site":
                    addSiteToBuilder(parser, parseErrors, profileBuilder);
                    break;
                case OpenlinkXmppNamespace.TAG_ACTIONS:
                    addActionsToBuilder(parser, profileBuilder);
                    break;
                default:
                    parseErrors.add("Unrecognised tag: " + parser.getName());
                    break;
                }
                ParserUtils.forwardToEndTagOfDepth(parser, profileDepth + 1);
                parser.nextTag();
            } while (profileDepth != parser.getDepth());

            builder.addProfile(profileBuilder.build(parseErrors));
            ParserUtils.forwardToEndTagOfDepth(parser, profileDepth);
            parser.nextTag();
        }
        return builder.build(parseErrors);
    }

    private static void addActionsToBuilder(final XmlPullParser parser, final Profile.Builder profileBuilder) throws XmlPullParserException, IOException {
        if (!parser.isEmptyElementTag()) {
            do {
                parser.nextTag();
                if (parser.getName().equals(OpenlinkXmppNamespace.TAG_ACTION)) {
                    SmackPacketUtil.getStringAttribute(parser, "id")
                            .flatMap(RequestAction::from)
                            .ifPresent(profileBuilder::addAction);
                }
                ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
            } while (parser.getName().equals(OpenlinkXmppNamespace.TAG_ACTION));
        }
    }

    private static void addSiteToBuilder(final XmlPullParser parser, final List<String> parseErrors, final Profile.Builder profileBuilder) throws IOException, XmlPullParserException {
        final Optional<Site> site = SmackPacketUtil.getSite(parser, parseErrors, STANZA_DESCRIPTION);
        site.ifPresent(profileBuilder::setSite);
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
            profile.isDefaultProfile().ifPresent(isDefault -> xml.attribute(OpenlinkXmppNamespace.TAG_DEFAULT, isDefault));
            profile.getDeviceType().ifPresent(deviceType -> xml.attribute("device", deviceType.value()));
            profile.getDeviceId().ifPresent(deviceId -> xml.attribute("devicenum", deviceId.value()));
            profile.getLabel().ifPresent(label -> xml.attribute(OpenlinkXmppNamespace.TAG_LABEL, label));
            profile.isOnline().ifPresent(online -> xml.attribute("online", online));
            xml.rightAngleBracket();
            profile.getSite().ifPresent(site -> SmackPacketUtil.addSiteXML(xml, site));
            final List<RequestAction> actions = profile.getActions();
            if (!actions.isEmpty()) {
                xml.openElement(OpenlinkXmppNamespace.TAG_ACTIONS);
                actions.forEach(action -> {
                    xml.halfOpenElement(OpenlinkXmppNamespace.TAG_ACTION);
                    xml.attribute("id", action.getId());
                    xml.attribute(OpenlinkXmppNamespace.TAG_LABEL, action.getLabel());
                    xml.closeEmptyElement();
                });
                xml.closeElement(OpenlinkXmppNamespace.TAG_ACTIONS);
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
            super(IQ.Type.class);
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
