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
import com.bt.openlink.iq.GetProfilesRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;

public class GetProfilesRequest extends OpenlinkIQ {
    @Nullable private final JID jid;

    private GetProfilesRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "jid", jid);
    }

    @Nonnull
    public Optional<JID> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public static GetProfilesRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        TinderPacketUtil.getJID(TinderPacketUtil.getChildElementString(inElement, "jid")).ifPresent(builder::setJID);
        final GetProfilesRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends GetProfilesRequestBuilder<Builder, JID, IQ.Type> {

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
        public GetProfilesRequest build() {
            super.validate();
            return new GetProfilesRequest(this, null);
        }

        @Nonnull
        private GetProfilesRequest build(@Nonnull final List<String> errors) {
            super.validate(errors);
            return new GetProfilesRequest(this, errors);
        }

    }

}
