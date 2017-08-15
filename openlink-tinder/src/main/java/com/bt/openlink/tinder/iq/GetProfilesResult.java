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
import com.bt.openlink.type.Site;

public class GetProfilesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-profiles result";
    @Nonnull private final List<Profile> profiles;

    private GetProfilesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.profiles);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        final Element profilesElement = outElement.addElement(OpenlinkXmppNamespace.TAG_PROFILES);
        getProfiles().forEach(profile -> {
            final Element profileElement = profilesElement.addElement(OpenlinkXmppNamespace.TAG_PROFILE);
            profile.profileId().ifPresent(profileId -> profileElement.addAttribute("id", profileId.value()));
            final Optional<Site> optionalSite = profile.getSite();
            if (optionalSite.isPresent()) {
                final Site site = optionalSite.get();
                final Element siteElement = profileElement.addElement("site");
                site.getId().ifPresent(id -> siteElement.addAttribute("id", String.valueOf(id)));
                site.isDefault().ifPresent(isDefault -> siteElement.addAttribute("default", String.valueOf(isDefault)));
                site.getType().ifPresent(type -> siteElement.addAttribute("type", type.name()));
                site.getName().ifPresent(siteElement::setText);
            }
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
        final Element profilesElement = TinderPacketUtil.getChildElement(TinderPacketUtil.getIOOutElement(iq), OpenlinkXmppNamespace.TAG_PROFILES);
        final List<String> parseErrors = new ArrayList<>();
        if (profilesElement == null) {
            parseErrors.add(String.format("Invalid %s; missing 'profiles' element is mandatory", DESCRIPTION));
        } else {
            final List<Element> profileElements = profilesElement.elements(OpenlinkXmppNamespace.TAG_PROFILE);
            profileElements.forEach(profileElement -> {
                final Optional<ProfileId> profileId = ProfileId.from(TinderPacketUtil.getStringAttribute(profileElement, "id", true, DESCRIPTION, parseErrors));
                final Profile.Builder profileBuilder = Profile.Builder.start();
                final Element siteElement = profileElement.element("site");
                if (siteElement != null) {
                    final Site.Builder siteBuilder = Site.Builder.start()
                            .setName(siteElement.getText());
                    final Optional<Long> id = Optional.ofNullable(TinderPacketUtil.getLongAttribute(siteElement, "id", true, DESCRIPTION, parseErrors));
                    id.ifPresent(siteBuilder::setId);
                    final Optional<Boolean> isDefault = Optional.ofNullable(TinderPacketUtil.getBooleanAttribute(siteElement, "default", false, DESCRIPTION, parseErrors));
                    isDefault.ifPresent(siteBuilder::setDefault);
                    final Optional<Site.Type> type = Site.Type.from(TinderPacketUtil.getStringAttribute(siteElement, "type", true, DESCRIPTION, parseErrors));
                    type.ifPresent(siteBuilder::setType);
                    profileBuilder.setSite(siteBuilder.build(parseErrors));
                }
                profileId.ifPresent(profileBuilder::setProfileId);
                final Profile profile = profileBuilder
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
        public static Builder start(@Nonnull final GetProfilesRequest request) {
            return new Builder()
                    .setID(request.getID())
                    .setFrom(request.getTo())
                    .setTo(request.getFrom());
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
