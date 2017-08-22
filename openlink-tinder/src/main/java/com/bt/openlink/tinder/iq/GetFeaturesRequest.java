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

public class GetFeaturesRequest extends OpenlinkIQ {
    @Nullable private final ProfileId profileId;

    private GetFeaturesRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.profileId;
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_FEATURES);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "profile", profileId);

    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public static GetFeaturesRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start()
                .setTo(iq.getTo())
                .setFrom(iq.getFrom())
                .setID(iq.getID())
                .setType(iq.getType())
                .setProfileId(TinderPacketUtil.getChildElementString(inElement,
                        "profile",
                        true,
                        "get-features request",
                        parseErrors));

        final GetFeaturesRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nullable ProfileId profileId;

        private Builder() {
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.set;
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public GetFeaturesRequest build() {
            validateBuilder();
            if (profileId == null) {
                throw new IllegalStateException("The profileId has not been set");
            }
            return new GetFeaturesRequest(this, Collections.emptyList());
        }

        @Nonnull
        private GetFeaturesRequest build(final List<String> parseErrors) {
            return new GetFeaturesRequest(this, parseErrors);
        }

        private Builder setProfileId(@Nullable String profileId) {
            this.profileId = ProfileId.from(profileId).orElse(null);
            return this;
        }

        public Builder setProfileId(@Nonnull ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

    }

}
