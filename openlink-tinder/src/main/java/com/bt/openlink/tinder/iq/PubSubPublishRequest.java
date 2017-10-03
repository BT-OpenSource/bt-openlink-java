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

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;

public class PubSubPublishRequest extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "PubSub unsubscribe request";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nonnull private final Collection<Call> calls;

    private PubSubPublishRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.pubSubNodeId;
        this.calls = Collections.unmodifiableCollection(builder.calls);
        final Element pubSubElement = this.getElement().addElement("pubsub", OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element publishElement = pubSubElement.addElement("publish");
        getPubSubNodeId().ifPresent(nodeId -> publishElement.addAttribute("node", nodeId.value()));
        TinderPacketUtil.addItemCallStatusCalls(publishElement, calls);
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
        if (publishElement == null) {
            parseErrors.add("Invalid " + STANZA_DESCRIPTION + "; missing 'publish' tag is mandatory");
        } else {
            final Optional<PubSubNodeId> pubSubNodeId = PubSubNodeId.from(TinderPacketUtil.getStringAttribute(publishElement,
                    "node", true, STANZA_DESCRIPTION, parseErrors).orElse(null));
            pubSubNodeId.ifPresent(builder::setPubSubNodeId);
            builder.addCalls(TinderPacketUtil.getCalls(publishElement, STANZA_DESCRIPTION, parseErrors));
        }
        final PubSubPublishRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<PubSubPublishRequest.Builder> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            return new Builder(iq);
        }

        @Nullable private PubSubNodeId pubSubNodeId;
        @Nonnull private final List<Call> calls = new ArrayList<>();

        private Builder() {
        }

        private Builder(@Nonnull final IQ iq) {
            super(iq);
        }

        @Override
        @Nonnull
        protected IQ.Type getExpectedType() {
            return Type.set;
        }

        @Nonnull
        public PubSubPublishRequest build() {
            validateBuilder();
            if (pubSubNodeId == null) {
                throw new IllegalStateException("The stanza 'pubSubNodeId' has not been set");
            }

            calls.forEach(call -> {
                final Optional<InterestId> interestIdOptional = call.getInterestId();
                if (interestIdOptional.isPresent()) {
                    final InterestId interestId = interestIdOptional.get();
                    if (!interestId.equals(pubSubNodeId.toInterestId())) {
                        throw new IllegalStateException("A call on interest id '" + interestId + "' cannot be published on PubSub nodeId '" + pubSubNodeId + '\'');
                    }
                }
            });

            return build(Collections.emptyList());
        }

        @Nonnull
        private PubSubPublishRequest build(@Nonnull final List<String> parseErrors) {
            return new PubSubPublishRequest(this, parseErrors);
        }

        public Builder setPubSubNodeId(@Nonnull PubSubNodeId pubSubNodeId) {
            this.pubSubNodeId = pubSubNodeId;
            return this;
        }

        public Builder setInterestId(@Nonnull InterestId interestId) {
            return setPubSubNodeId(interestId.toPubSubNodeId());
        }

        public Builder addCall(@Nonnull Call call) {
            this.calls.add(call);
            return this;
        }

        public Builder addCalls(@Nonnull Collection<Call> calls) {
            this.calls.addAll(calls);
            return this;
        }

    }

}
