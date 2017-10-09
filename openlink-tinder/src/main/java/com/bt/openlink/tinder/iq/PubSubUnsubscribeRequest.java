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
import com.bt.openlink.iq.PubSubUnsubscribeRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.PubSubNodeId;

public class PubSubUnsubscribeRequest extends OpenlinkIQ2 {
    private static final String STANZA_DESCRIPTION = "PubSub unsubscribe request";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final JID jid;

    private PubSubUnsubscribeRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.jid = builder.getJID().orElse(null);
        final Element pubSubElement = this.getElement().addElement("pubsub", OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element unsubscribeElement = pubSubElement.addElement("unsubscribe");
        if (pubSubNodeId != null) {
            unsubscribeElement.addAttribute("node", pubSubNodeId.value());
        }
        if (jid != null) {
            unsubscribeElement.addAttribute("jid", jid.toString());
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
    public static PubSubUnsubscribeRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element unsubscribeElement = TinderPacketUtil.getChildElement(iq.getElement(), "pubsub", "unsubscribe");
        if (unsubscribeElement == null) {
            parseErrors.add("Invalid " + STANZA_DESCRIPTION + "; missing 'unsubscribe' tag is mandatory");
        } else {
            final Optional<PubSubNodeId> pubSubNodeId = PubSubNodeId.from(TinderPacketUtil.getStringAttribute(unsubscribeElement,
                    "node", false, STANZA_DESCRIPTION, parseErrors).orElse(null));
            pubSubNodeId.ifPresent(builder::setPubSubNodeId);
            final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getStringAttribute(unsubscribeElement,
                    "jid", false, STANZA_DESCRIPTION, parseErrors).orElse(null));
            jid.ifPresent(builder::setJID);
        }

        final PubSubUnsubscribeRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends PubSubUnsubscribeRequestBuilder<Builder, JID, Type> {

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
        public PubSubUnsubscribeRequest build() {
            super.validate();
            return new PubSubUnsubscribeRequest(this, null);
        }

        @Nonnull
        private PubSubUnsubscribeRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new PubSubUnsubscribeRequest(this, errors);
        }
    }

}
