package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;

public class GetProfilesResult extends OpenlinkIQ {
    @Nonnull private final List<Profile> profiles;

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_PROFILES, OpenlinkXmppNamespace.TAG_PROFILE);

        final Builder builder = Builder.start();

        final List<String> parseErrors = new ArrayList<>();
        while (OpenlinkXmppNamespace.TAG_PROFILE.equals(parser.getName())) {

            final Optional<ProfileId> profileId = ProfileId.from(parser.getAttributeValue("", "id"));
            final Profile profile = Profile.Builder.start()
                    .withProfileId(profileId.orElse(null))
                    .build(parseErrors);
            parseErrors.addAll(profile.parseErrors());
            builder.addProfile(profile);
            ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
            parser.nextTag();
        }
        return builder.build(parseErrors);
    }

    private GetProfilesResult(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.profiles);
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
            profile.profileId().ifPresent(profileId -> xml.attribute("id", profileId.value()));
            xml.closeEmptyElement();
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

    public static final class Builder extends IQBuilder<Builder> {

        @Nonnull private List<Profile> profiles = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        @Override
        protected Type getExpectedType() {
            return Type.result;
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public GetProfilesResult build() {
            validateBuilder();
            return new GetProfilesResult(this, Collections.emptyList());
        }

        @Nonnull
        private GetProfilesResult build(final List<String> parseErrors) {
            return new GetProfilesResult(this, parseErrors);
        }

        @Nonnull
        public Builder addProfile(@Nonnull final Profile profile) {
            this.profiles.forEach(existingProfile -> {
                if (existingProfile.profileId().equals(profile.profileId())) {
                    throw new IllegalArgumentException("The profile id must be unique");
                }
            });
            this.profiles.add(profile);
            return this;
        }

    }
}
