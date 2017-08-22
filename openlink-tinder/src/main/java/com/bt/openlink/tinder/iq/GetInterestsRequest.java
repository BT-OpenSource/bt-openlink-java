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
import com.bt.openlink.type.ProfileId;

public class GetInterestsRequest extends OpenlinkIQ {
    @Nullable private final ProfileId profileId;

    private GetInterestsRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.profileId;
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_INTERESTS);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "profile", profileId);

    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public static GetInterestsRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        final Optional<ProfileId> profile = ProfileId.from(TinderPacketUtil.getChildElementString(inElement,
                "profile",
                true,
                "get-interests request",
                parseErrors));
        profile.ifPresent(builder::setProfileId);
        final GetInterestsRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
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

        @Nullable ProfileId profileId;

        private Builder() {
        }

        private Builder(@Nonnull final IQ iq) {
            super(iq);
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.set;
        }

        @Nonnull
        public GetInterestsRequest build() {
            validateBuilder();
            if (profileId == null) {
                throw new IllegalStateException("The profileId has not been set");
            }
            return new GetInterestsRequest(this, Collections.emptyList());
        }

        @Nonnull
        private GetInterestsRequest build(final List<String> parseErrors) {
            return new GetInterestsRequest(this, parseErrors);
        }

        public Builder setProfileId(@Nonnull ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

    }

}
