package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetProfilesResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.DeviceId;
import com.bt.openlink.type.DeviceType;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;

public class GetProfilesResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-profiles result";
    private static final String ATTRIBUTE_DEFAULT = "default";
    private static final String ATTRIBUTE_LABEL = "label";
    @Nonnull private final List<Profile> profiles;

    private GetProfilesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profiles = Collections.unmodifiableList(builder.getProfiles());
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        final Element profilesElement = outElement.addElement(OpenlinkXmppNamespace.TAG_PROFILES, OpenlinkXmppNamespace.OPENLINK_PROFILES.uri());
        getProfiles().forEach(profile -> {
            final Element profileElement = profilesElement.addElement(OpenlinkXmppNamespace.TAG_PROFILE);
            profile.getId().ifPresent(profileId -> profileElement.addAttribute("id", profileId.value()));
            profile.isDefaultProfile().ifPresent(isDefault -> profileElement.addAttribute(ATTRIBUTE_DEFAULT, String.valueOf(isDefault)));
            profile.getDeviceType().ifPresent(deviceType -> profileElement.addAttribute("device", deviceType.value()));
            profile.getDeviceId().ifPresent(deviceId -> profileElement.addAttribute("devicenum", deviceId.value()));
            profile.getLabel().ifPresent(label -> profileElement.addAttribute(ATTRIBUTE_LABEL, label));
            profile.isOnline().ifPresent(online -> profileElement.addAttribute("online", String.valueOf(online)));
            profile.getSite().ifPresent(site -> TinderPacketUtil.addSite(profileElement, site));
            final List<RequestAction> actions = profile.getActions();
            if (!actions.isEmpty()) {
                final Element actionsElement = profileElement.addElement("actions");
                for (final RequestAction requestAction : actions) {
                    final Element actionElement = actionsElement.addElement("action");
                    actionElement.addAttribute("id", requestAction.getId());
                    actionElement.addAttribute(ATTRIBUTE_LABEL, requestAction.getLabel());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetProfilesResult from(@Nonnull IQ iq) {
        final Builder builder = Builder.start(iq);
        final Element profilesElement = TinderPacketUtil.getChildElement(TinderPacketUtil.getIOOutElement(iq), OpenlinkXmppNamespace.TAG_PROFILES);
        final List<String> parseErrors = new ArrayList<>();
        final AtomicBoolean profileFound = new AtomicBoolean(false);
        if (profilesElement != null) {
            final List<Element> profileElements = profilesElement.elements(OpenlinkXmppNamespace.TAG_PROFILE);
            profileElements.forEach(profileElement -> {
                final Profile.Builder profileBuilder = Profile.Builder.start();
                ProfileId.from(TinderPacketUtil.getNullableStringAttribute(profileElement, "id")).ifPresent(profileBuilder::setId);
                TinderPacketUtil.getBooleanAttribute(profileElement, ATTRIBUTE_DEFAULT, DESCRIPTION, parseErrors).ifPresent(profileBuilder::setDefault);
                TinderPacketUtil.getStringAttribute(profileElement, "device", false, DESCRIPTION, parseErrors).flatMap(DeviceType::from).ifPresent(profileBuilder::setDeviceType);
                TinderPacketUtil.getStringAttribute(profileElement, "devicenum", false, DESCRIPTION, parseErrors).flatMap(DeviceId::from).ifPresent(profileBuilder::setDeviceId);
                TinderPacketUtil.getStringAttribute(profileElement, ATTRIBUTE_LABEL).ifPresent(profileBuilder::setLabel);
                TinderPacketUtil.getBooleanAttribute(profileElement, "online", DESCRIPTION, parseErrors).ifPresent(profileBuilder::setOnline);
                TinderPacketUtil.getSite(profileElement, DESCRIPTION, parseErrors).ifPresent(profileBuilder::setSite);
                final Element actionsElement = TinderPacketUtil.getChildElement(profileElement, OpenlinkXmppNamespace.TAG_ACTIONS);
                if (actionsElement != null) {
                    final List<Element> actionElements = actionsElement.elements(OpenlinkXmppNamespace.TAG_ACTION);
                    for (final Element actionElement : actionElements) {
                        RequestAction.from(TinderPacketUtil.getNullableStringAttribute(actionElement, "id")).ifPresent(profileBuilder::addAction);
                    }
                }
                builder.addProfile(profileBuilder.build(parseErrors));
                profileFound.set(true);
            });
        }
        if (!profileFound.get()) {
            parseErrors.add("Invalid get-profiles result; no profiles present");
        }
        final GetProfilesResult result = builder.build(parseErrors);
        result.setID(iq.getID());
        return result;
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public List<Profile> getProfiles() {
        return profiles;
    }

    public static final class Builder extends GetProfilesResultBuilder<Builder, JID, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            final Builder builder = start();
            TinderIQBuilder.setIQBuilder(builder, iq);
            return builder;
        }

        /**
         * Convenience method to create a new {@link Builder} based on a {@link Type#get IQ.Type.get} or {@link Type#set
         * IQ.Type.set} IQ. The new builder will be initialized with:
         * <ul>
         *
         * <li>The sender set to the recipient of the originating IQ.
         * <li>The recipient set to the sender of the originating IQ.
         * <li>The id set to the id of the originating IQ.
         * </ul>
         *
         * @param request
         *            the {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ packet.
         * @throws IllegalArgumentException
         *             if the IQ packet does not have a type of {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set}.
         * @return a new {@link Builder} based on the originating IQ.
         */
        @SuppressWarnings("WeakerAccess")
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return start(IQ.createResultIQ(request));
        }

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetProfilesResult build() {
            super.validate();
            return new GetProfilesResult(this, null);
        }

        @Nonnull
        private GetProfilesResult build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new GetProfilesResult(this, errors);
        }
    }
}
