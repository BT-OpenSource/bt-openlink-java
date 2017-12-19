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
import com.bt.openlink.iq.PubSubSubscriptionRequestResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.PubSubNodeId;
import com.bt.openlink.type.SubscriptionState;

public class PubSubSubscriptionRequest extends OpenlinkIQ {
    private static final String ELEMENT_PUBSUB = "pubsub";
    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final JID jid;
    @Nullable private final SubscriptionState subscriptionState;

    private PubSubSubscriptionRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.jid = builder.getJID().orElse(null);
        this.subscriptionState = builder.getSubscriptionState().orElse(null);
        final Element pubSubElement = this.getElement().addElement(ELEMENT_PUBSUB, OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element actionElement;
        if (SubscriptionState.SUBSCRIBED == subscriptionState) {
            actionElement = pubSubElement.addElement("subscribe");
        } else {
            actionElement = pubSubElement.addElement("unsubscribe");
        }
        getPubSubNodeId().ifPresent(nodeId -> actionElement.addAttribute("node", nodeId.value()));
        getJID().ifPresent(subscriber -> actionElement.addAttribute("jid", subscriber.toString()));
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
    public static PubSubSubscriptionRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        Element actionElement = TinderPacketUtil.getChildElement(iq.getElement(), ELEMENT_PUBSUB, "subscribe");
        if (actionElement != null) {
            builder.setSubscriptionState(SubscriptionState.SUBSCRIBED);
        } else {
            actionElement = TinderPacketUtil.getChildElement(iq.getElement(), ELEMENT_PUBSUB, "unsubscribe");
            if (actionElement != null) {
                builder.setSubscriptionState(SubscriptionState.NONE);
            }
        }
        if (actionElement != null) {
            PubSubNodeId.from(TinderPacketUtil.getNullableStringAttribute(actionElement, "node")).ifPresent(builder::setPubSubNodeId);
            TinderPacketUtil.getJID(TinderPacketUtil.getNullableStringAttribute(actionElement, "jid")).ifPresent(builder::setJID);
        }
        final PubSubSubscriptionRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    @Nonnull
    public Optional<SubscriptionState> getSubscriptionState() {
        return Optional.ofNullable(subscriptionState);
    }

    public static final class Builder extends PubSubSubscriptionRequestResultBuilder<Builder, JID, Type> {

        @Nonnull
        @Override
        public String getExpectedIQType() {
            return "set";
        }

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
        public PubSubSubscriptionRequest build() {
            super.validate();
            return new PubSubSubscriptionRequest(this, null);
        }

        @Nonnull
        private PubSubSubscriptionRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new PubSubSubscriptionRequest(this, errors);
        }
    }

}
