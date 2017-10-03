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
    private static final String ATTRIBUTE_DEFAULT = "default";
    private static final String ATTRIBUTE_LABEL = "label";
    @Nonnull private final List<Profile> profiles;

    private GetProfilesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.profiles);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        final Element profilesElement = outElement.addElement(OpenlinkXmppNamespace.TAG_PROFILES, OpenlinkXmppNamespace.OPENLINK_PROFILES.uri());
        getProfiles().forEach(profile -> {
            final Element profileElement = profilesElement.addElement(OpenlinkXmppNamespace.TAG_PROFILE);
            profile.getId().ifPresent(profileId -> profileElement.addAttribute("id", profileId.value()));
            profile.isDefaultProfile().ifPresent(isDefault -> profileElement.addAttribute(ATTRIBUTE_DEFAULT, String.valueOf(isDefault)));
            profile.getDevice().ifPresent(device -> profileElement.addAttribute("device", device));
            profile.getLabel().ifPresent(label -> profileElement.addAttribute(ATTRIBUTE_LABEL, label));
            profile.isOnline().ifPresent(online -> profileElement.addAttribute("online", String.valueOf(online)));
            profile.getSite().ifPresent(site -> TinderPacketUtil.addSite(profileElement, site));
            final Element actionsElement = profileElement.addElement("actions");
            for (final RequestAction requestAction : profile.getActions()) {
                final Element actionElement = actionsElement.addElement("action");
                actionElement.addAttribute("id", requestAction.getId());
                actionElement.addAttribute(ATTRIBUTE_LABEL, requestAction.getLabel());
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetProfilesResult from(@Nonnull IQ iq) {
        final Builder builder = Builder.start(iq);
        final Element profilesElement = TinderPacketUtil.getChildElement(TinderPacketUtil.getIOOutElement(iq), OpenlinkXmppNamespace.TAG_PROFILES);
        final List<String> parseErrors = new ArrayList<>();
        if (profilesElement == null) {
            parseErrors.add(String.format("Invalid %s; missing 'profiles' element is mandatory", DESCRIPTION));
        } else {
            final List<Element> profileElements = profilesElement.elements(OpenlinkXmppNamespace.TAG_PROFILE);
            profileElements.forEach(profileElement -> {
                final Profile.Builder profileBuilder = Profile.Builder.start();
                final Optional<ProfileId> profileId = ProfileId.from(TinderPacketUtil.getStringAttribute(profileElement, "id", true, DESCRIPTION, parseErrors).orElse(null));
                profileId.ifPresent(profileBuilder::setId);
                final Optional<Boolean> isDefault = TinderPacketUtil.getBooleanAttribute(profileElement, ATTRIBUTE_DEFAULT, true, DESCRIPTION, parseErrors);
                isDefault.ifPresent(profileBuilder::setDefault);
                final Optional<String> device = Optional.ofNullable(TinderPacketUtil.getStringAttribute(profileElement, "device", false, DESCRIPTION, parseErrors).orElse(null));
                device.ifPresent(profileBuilder::setDevice);
                final Optional<String> label = Optional.ofNullable(TinderPacketUtil.getStringAttribute(profileElement, ATTRIBUTE_LABEL, false, DESCRIPTION, parseErrors).orElse(null));
                label.ifPresent(profileBuilder::setLabel);
                final Optional<Boolean> online = TinderPacketUtil.getBooleanAttribute(profileElement, "online", true, DESCRIPTION, parseErrors);
                online.ifPresent(profileBuilder::setOnline);
                final Optional<Site> site = TinderPacketUtil.getSite(profileElement, DESCRIPTION, parseErrors);
                site.ifPresent(profileBuilder::setSite);
                final Element actionsElement = TinderPacketUtil.getChildElement(profileElement, "actions");
                if (actionsElement != null) {
                    final List<Element> actionElements = actionsElement.elements("action");
                    for (final Element actionElement : actionElements) {
                        final Optional<RequestAction> requestAction = RequestAction.from(TinderPacketUtil.getStringAttribute(actionElement, "id", true, DESCRIPTION, parseErrors).orElse(null));
                        requestAction.ifPresent(profileBuilder::addAction);
                    }
                }
                final Profile profile = profileBuilder
                        .buildWithoutValidating();
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

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            return new Builder(iq);
        }

        @Nonnull
        public static Builder start(@Nonnull final GetProfilesRequest request) {
            return new Builder(IQ.createResultIQ(request));
        }

        @Nonnull private List<Profile> profiles = new ArrayList<>();

        private Builder() {
        }

        public Builder(@Nonnull final IQ iq) {
            super(iq);
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.result;
        }

        @Nonnull
        public GetProfilesResult build() {
            validateBuilder();
            return build(Collections.emptyList());
        }

        @Nonnull
        private GetProfilesResult build(@Nonnull final List<String> parseErrors) {
            return new GetProfilesResult(this, parseErrors);
        }

        @Nonnull
        public Builder addProfile(@Nonnull final Profile profile) {
            this.profiles.forEach(existingProfile -> {
                if (existingProfile.getId().equals(profile.getId())) {
                    throw new IllegalArgumentException("The profile id must be unique");
                }
            });
            this.profiles.add(profile);
            return this;
        }

    }

}
