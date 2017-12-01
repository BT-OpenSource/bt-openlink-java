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
import com.bt.openlink.iq.GetFeaturesRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.ProfileId;

public class GetFeaturesRequest extends OpenlinkIQ {
    @Nullable private final ProfileId profileId;

    private GetFeaturesRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
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
        final Builder builder = Builder.start(iq);
        ProfileId.from(TinderPacketUtil.getNullableChildElementString(inElement, "profile")).ifPresent(builder::setProfileId);
        final GetFeaturesRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends GetFeaturesRequestBuilder<Builder, JID, IQ.Type> {

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
        public GetFeaturesRequest build() {
            super.validate();
            return new GetFeaturesRequest(this, null);
        }

        @Nonnull
        private GetFeaturesRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new GetFeaturesRequest(this, errors);
        }
    }

}
