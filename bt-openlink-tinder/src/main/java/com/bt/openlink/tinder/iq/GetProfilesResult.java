package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;

public class GetProfilesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-profiles result";
    @Nonnull private final List<Profile> profiles;

    private GetProfilesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.profiles);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        final Element profilesElement = outElement.addElement("profiles");
        getProfiles().forEach(profile -> {
            final Element profileElement = profilesElement.addElement("profile");
            profile.profileId().ifPresent(profileId -> profileElement.addAttribute("id", profileId.value()));
        });

    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetProfilesResult from(@Nonnull IQ iq) {
        final Builder builder = Builder.start()
                .setTo(iq.getTo())
                .setFrom(iq.getFrom())
                .setID(iq.getID())
                .setType(iq.getType());
        final Element profilesElement = TinderPacketUtil.getChildElement(TinderPacketUtil.getIOOutElement(iq), "profiles");
        final List<String> parseErrors = new ArrayList<>();
        if (profilesElement == null) {
            parseErrors.add(String.format("Invalid %s; missing 'profiles' element is mandatory", DESCRIPTION));
        } else {
            final List<Element> profileElements = profilesElement.elements("profile");
            profileElements.forEach(profileElement -> {
                final Optional<ProfileId> profileId = ProfileId.from(TinderPacketUtil.getAttributeString(profileElement, "id", true, DESCRIPTION, parseErrors));
                final Profile profile = Profile.Builder.start()
                        .withProfileId(profileId.orElse(null))
                        .build(parseErrors);
                parseErrors.addAll(profile.parseErrors());
                builder.addProfile(profile);
            });
            if (profileElements.isEmpty()) {
                parseErrors.add(String.format("Invalid %s; no 'profile' elements present", DESCRIPTION));
            }
        }
        final GetProfilesResult result = builder.build(parseErrors);
        result.setID(iq.getID());
        return result;
    }

    @Nonnull
    public List<Profile> getProfiles() {
        return profiles;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nonnull private List<Profile> profiles = new ArrayList<>();

        private Builder() {
        }

        @Override
        @Nonnull
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
            return new GetProfilesResult(this, null);
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
