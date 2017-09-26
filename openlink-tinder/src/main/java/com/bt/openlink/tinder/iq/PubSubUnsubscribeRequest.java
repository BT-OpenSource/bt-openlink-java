package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;

public class PubSubUnsubscribeRequest extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "PubSub unsubscribe request";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final JID jid;

    private PubSubUnsubscribeRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.pubSubNodeId;
        this.jid = builder.jid;
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
                    "node", true, STANZA_DESCRIPTION, parseErrors).orElse(null));
            pubSubNodeId.ifPresent(builder::setPubSubNodeId);
            final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getStringAttribute(unsubscribeElement,
                    "jid", true, STANZA_DESCRIPTION, parseErrors).orElse(null));
            jid.ifPresent(builder::setJID);
        }

        final PubSubUnsubscribeRequest request = builder.build(parseErrors);
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

        @Nullable private PubSubNodeId pubSubNodeId;
        @Nullable private JID jid;

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
        public PubSubUnsubscribeRequest build() {
            validateBuilder();
            if (pubSubNodeId == null) {
                throw new IllegalStateException("The stanza 'pubSubNodeId' has not been set");
            }
            if (jid == null) {
                throw new IllegalStateException("The stanza 'jid' has not been set");
            }
            return build(Collections.emptyList());
        }

        @Nonnull
        private PubSubUnsubscribeRequest build(@Nonnull final List<String> parseErrors) {
            return new PubSubUnsubscribeRequest(this, parseErrors);
        }

        public Builder setPubSubNodeId(@Nonnull PubSubNodeId pubSubNodeId) {
            this.pubSubNodeId = pubSubNodeId;
            return this;
        }

        public Builder setInterestId(@Nonnull InterestId interestId) {
            return setPubSubNodeId(interestId.toPubSubNodeId());
        }

        public Builder setJID(@Nonnull JID jid) {
            this.jid = jid;
            return this;
        }

    }

}
