package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetInterestsRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.ProfileId;

public class GetInterestsRequest extends OpenlinkIQ {
    @Nullable private final ProfileId profileId;

    private GetInterestsRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
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
        final Optional<ProfileId> profile = ProfileId.from(TinderPacketUtil.getChildElementString(inElement, "profile"));
        profile.ifPresent(builder::setProfileId);
        final GetInterestsRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends GetInterestsRequestBuilder<Builder, JID, Type> {

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

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetInterestsRequest build() {
            validate();
            return new GetInterestsRequest(this, null);
        }

        @Nonnull
        private GetInterestsRequest build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetInterestsRequest(this, parseErrors);
        }
    }

}
