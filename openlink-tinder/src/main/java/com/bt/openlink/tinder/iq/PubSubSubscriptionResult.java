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
import com.bt.openlink.iq.PubSubSubscriptionRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.PubSubNodeId;
import com.bt.openlink.type.SubscriptionState;

public class PubSubSubscriptionResult extends OpenlinkIQ {
    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final JID jid;
    @Nullable private final SubscriptionState subscriptionState;

    private PubSubSubscriptionResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.jid = builder.getJID().orElse(null);
        this.subscriptionState = builder.getSubscriptionState().orElse(null);
        final Element pubSubElement = this.getElement().addElement("pubsub", OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element subscriptionElement = pubSubElement.addElement("subscription");
        getPubSubNodeId().ifPresent(nodeId -> subscriptionElement.addAttribute("node", nodeId.value()));
        getJID().ifPresent(subscriber -> subscriptionElement.addAttribute("jid", subscriber.toString()));
        getSubscriptionState().ifPresent(subscription -> subscriptionElement.addAttribute("subscription", subscription.getId()));
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
    public static PubSubSubscriptionResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        Element actionElement = TinderPacketUtil.getChildElement(iq.getElement(), "pubsub", "subscription");
        if (actionElement != null) {
            PubSubNodeId.from(TinderPacketUtil.getNullableStringAttribute(actionElement, "node")).ifPresent(builder::setPubSubNodeId);
            TinderPacketUtil.getJID(TinderPacketUtil.getNullableStringAttribute(actionElement, "jid")).ifPresent(builder::setJID);
            SubscriptionState.from(TinderPacketUtil.getNullableStringAttribute(actionElement, "subscription")).ifPresent(builder::setSubscriptionState);
        }
        final PubSubSubscriptionResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    @Nonnull
    public Optional<SubscriptionState> getSubscriptionState() {
        return Optional.ofNullable(subscriptionState);
    }

    public static final class Builder extends PubSubSubscriptionRequestBuilder<Builder, JID, Type> {

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
            super(Type.class);
        }

        @Nonnull
        public PubSubSubscriptionResult build() {
            super.validate();
            return new PubSubSubscriptionResult(this, null);
        }

        @Nonnull
        private PubSubSubscriptionResult build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new PubSubSubscriptionResult(this, errors);
        }
    }

}
