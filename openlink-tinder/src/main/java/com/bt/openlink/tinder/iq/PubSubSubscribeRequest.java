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
import com.bt.openlink.iq.PubSubSubscribeUnSubscribeRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.PubSubNodeId;

public class PubSubSubscribeRequest extends OpenlinkIQ2 {
    private static final String STANZA_DESCRIPTION = "PubSub unsubscribe request";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final JID jid;

    private PubSubSubscribeRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.jid = builder.getJID().orElse(null);
        final Element pubSubElement = this.getElement().addElement("pubsub", OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element subscribeElement = pubSubElement.addElement("subscribe");
        if (pubSubNodeId != null) {
            subscribeElement.addAttribute("node", pubSubNodeId.value());
        }
        if (jid != null) {
            subscribeElement.addAttribute("jid", jid.toString());
        }
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public Optional<JID> getJID() {
        return Optional.ofNullable(jid);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static PubSubSubscribeRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element subscribeElement = TinderPacketUtil.getChildElement(iq.getElement(), "pubsub", "subscribe");
        if (subscribeElement != null) {
            final Optional<PubSubNodeId> pubSubNodeId = PubSubNodeId.from(TinderPacketUtil.getStringAttribute(subscribeElement,
                    "node", false, STANZA_DESCRIPTION, parseErrors).orElse(null));
            pubSubNodeId.ifPresent(builder::setPubSubNodeId);
            final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getStringAttribute(subscribeElement,
                    "jid", false, STANZA_DESCRIPTION, parseErrors).orElse(null));
            jid.ifPresent(builder::setJID);
        }

        final PubSubSubscribeRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends PubSubSubscribeUnSubscribeRequestBuilder<Builder, JID, Type> {

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
        public PubSubSubscribeRequest build() {
            super.validate();
            return new PubSubSubscribeRequest(this, null);
        }

        @Nonnull
        private PubSubSubscribeRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new PubSubSubscribeRequest(this, errors);
        }
    }

}
