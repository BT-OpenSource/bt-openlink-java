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
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

public class GetProfilesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-profiles result";
    @Nonnull private final List<Profile> profiles;

    private GetProfilesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.profiles);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        final Element profilesElement = outElement.addElement(OpenlinkXmppNamespace.TAG_PROFILES, OpenlinkXmppNamespace.OPENLINK_PROFILES.uri());
        getProfiles().forEach(profile -> {
            final Element profileElement = profilesElement.addElement(OpenlinkXmppNamespace.TAG_PROFILE);
            profile.getProfileId().ifPresent(profileId -> profileElement.addAttribute("id", profileId.value()));
            profile.isDefault().ifPresent(isDefault -> profileElement.addAttribute("default", String.valueOf(isDefault)));
            profile.getDevice().ifPresent(device -> profileElement.addAttribute("device", device));
            profile.getLabel().ifPresent(label -> profileElement.addAttribute("label", label));
            profile.isOnline().ifPresent(online -> profileElement.addAttribute("online", String.valueOf(online)));
            final Optional<Site> optionalSite = profile.getSite();
            if (optionalSite.isPresent()) {
                final Site site = optionalSite.get();
                final Element siteElement = profileElement.addElement("site");
                site.getId().ifPresent(id -> siteElement.addAttribute("id", String.valueOf(id)));
                site.isDefault().ifPresent(isDefault -> siteElement.addAttribute("default", String.valueOf(isDefault)));
                site.getType().ifPresent(type -> siteElement.addAttribute("type", type.name()));
                site.getName().ifPresent(siteElement::setText);
            }
            final Element actionsElement = profileElement.addElement("actions");
            for (final RequestAction requestAction : profile.getActions()) {
                final Element actionElement = actionsElement.addElement("action");
                actionElement.addAttribute("id", requestAction.getId());
                actionElement.addAttribute("label", requestAction.getLabel());
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
                final Profile.Builder profileBuilder = Profile.Builder.start();
                final Optional<ProfileId> profileId = ProfileId.from(TinderPacketUtil.getStringAttribute(profileElement, "id", true, DESCRIPTION, parseErrors));
                profileId.ifPresent(profileBuilder::setProfileId);
                final Optional<Boolean> isDefault = Optional.ofNullable(TinderPacketUtil.getBooleanAttribute(profileElement, "default", true, DESCRIPTION, parseErrors));
                isDefault.ifPresent(profileBuilder::setDefault);
                final Optional<String> device = Optional.ofNullable(TinderPacketUtil.getStringAttribute(profileElement, "device", false, DESCRIPTION, parseErrors));
                device.ifPresent(profileBuilder::setDevice);
                final Optional<String> label = Optional.ofNullable(TinderPacketUtil.getStringAttribute(profileElement, "label", false, DESCRIPTION, parseErrors));
                label.ifPresent(profileBuilder::setLabel);
                final Optional<Boolean> online = Optional.ofNullable(TinderPacketUtil.getBooleanAttribute(profileElement, "online", true, DESCRIPTION, parseErrors));
                online.ifPresent(profileBuilder::setOnline);
                final Element siteElement = profileElement.element("site");
                if (siteElement != null) {
                    final Site.Builder siteBuilder = Site.Builder.start()
                            .setName(siteElement.getText());
                    final Optional<Long> id = Optional.ofNullable(TinderPacketUtil.getLongAttribute(siteElement, "id", true, DESCRIPTION, parseErrors));
                    id.ifPresent(siteBuilder::setId);
                    final Optional<Boolean> isDefaultSite = Optional.ofNullable(TinderPacketUtil.getBooleanAttribute(siteElement, "default", false, DESCRIPTION, parseErrors));
                    isDefaultSite.ifPresent(siteBuilder::setDefault);
                    final Optional<Site.Type> type = Site.Type.from(TinderPacketUtil.getStringAttribute(siteElement, "type", true, DESCRIPTION, parseErrors));
                    type.ifPresent(siteBuilder::setType);
                    profileBuilder.setSite(siteBuilder.build(parseErrors));
                }
                final Element actionsElement = TinderPacketUtil.getChildElement(profileElement, "actions");
                if (actionsElement != null) {
                    final List<Element> actionElements = actionsElement.elements("action");
                    for (final Element actionElement : actionElements) {
                        final Optional<RequestAction> requestAction = RequestAction.from(TinderPacketUtil.getStringAttribute(actionElement, "id", true, DESCRIPTION, parseErrors));
                        requestAction.ifPresent(profileBuilder::addAction);
                    }
                }
                final Profile profile = profileBuilder
                        .build(parseErrors);
                parseErrors.addAll(profile.getParseErrors());
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
                if (existingProfile.getProfileId().equals(profile.getProfileId())) {
                    throw new IllegalArgumentException("The profile id must be unique");
                }
            });
            this.profiles.add(profile);
            return this;
        }

    }

}
