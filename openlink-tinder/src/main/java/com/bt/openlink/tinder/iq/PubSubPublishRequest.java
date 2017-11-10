package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.PubSubPublishRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.PubSubNodeId;

public class PubSubPublishRequest extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "PubSub unsubscribe request";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nonnull private final Collection<Call> calls;

    private PubSubPublishRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.calls = Collections.unmodifiableCollection(builder.getCalls());
        final Element pubSubElement = this.getElement().addElement("pubsub", OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element publishElement = pubSubElement.addElement("publish");
        getPubSubNodeId().ifPresent(nodeId -> publishElement.addAttribute("node", nodeId.value()));
        final Element itemElement = publishElement.addElement("item");
        TinderPacketUtil.addItemCallStatusCalls(itemElement, null, calls);
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public Collection<Call> getCalls() {
        return calls;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static PubSubPublishRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element publishElement = TinderPacketUtil.getChildElement(iq.getElement(), "pubsub", "publish");
        if (publishElement != null) {
            final Optional<PubSubNodeId> pubSubNodeId = PubSubNodeId.from(TinderPacketUtil.getStringAttribute(publishElement,
                    "node", false, STANZA_DESCRIPTION, parseErrors).orElse(null));
            pubSubNodeId.ifPresent(builder::setPubSubNodeId);
            final Element itemElement = publishElement.element("item");
            builder.addCalls(TinderPacketUtil.getCalls(itemElement, STANZA_DESCRIPTION, parseErrors));
        }
        final PubSubPublishRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends PubSubPublishRequestBuilder<Builder, JID, Type> {

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
        public PubSubPublishRequest build() {
            super.validate();
            return new PubSubPublishRequest(this, null);
        }

        @Nonnull
        private PubSubPublishRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new PubSubPublishRequest(this, errors);
        }
    }

}
