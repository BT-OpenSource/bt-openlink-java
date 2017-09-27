package com.bt.openlink.tinder.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.PubSubNodeId;

public class CallStatusMessage extends Message {

    private static final String STANZA_DESCRIPTION = "Call status message";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nonnull private final List<Call> calls;
    @Nonnull private final List<String> parseErrors;

    private CallStatusMessage(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        setTo(builder.to);
        setFrom(builder.from);
        if (builder.id != null) {
            setID(builder.id);
        }
        this.pubSubNodeId = builder.pubSubNodeId;
        this.calls = Collections.unmodifiableList(builder.calls);
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = Collections.unmodifiableList(parseErrors);
        }
        final Element eventElement = getElement().addElement("event", OpenlinkXmppNamespace.XMPP_PUBSUB_EVENT.uri());
        final Element itemsElement = eventElement.addElement("items");
        if (pubSubNodeId != null) {
            itemsElement.addAttribute("node", pubSubNodeId.value());
        }
        final Element itemElement = itemsElement.addElement("item");
        final Element callStatusElement = itemElement.addElement("callstatus", OpenlinkXmppNamespace.OPENLINK_CALL_STATUS.uri());
        calls.forEach(call -> {
            final Element callElement = callStatusElement.addElement("call");
            call.getId().ifPresent(callId -> callElement.addElement("id").setText(callId.value()));
            call.getProfileId().ifPresent(profileId -> callElement.addElement("profile").setText(profileId.value()));
            call.getInterestId().ifPresent(interestId -> callElement.addElement("interest").setText(interestId.value()));
            call.getState().ifPresent(state -> callElement.addElement("state").setText(state.name()));
            call.getDirection().ifPresent(direction -> callElement.addElement("direction").setText(direction.name()));
        });
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static CallStatusMessage from( @Nonnull final Message message) {
        final List<String> parseErrors = new ArrayList<>();
        final Element itemsElement = message.getChildElement("event", "http://jabber.org/protocol/pubsub#event").element("items");
        final Element callStatusElement = itemsElement.element("item").element("callstatus");
        final Builder builder = Builder.start()
                .setID(message.getID())
                .setFrom(message.getFrom())
                .setTo(message.getTo());
        final Optional<PubSubNodeId> node = PubSubNodeId.from(itemsElement.attributeValue("node"));
        node.ifPresent(builder::setPubSubNodeId);
        final List<Element> callElements = callStatusElement.elements("call");
        for (final Element callElement : callElements) {
            final Call.Builder callBuilder = Call.Builder.start();
            final Optional<CallId> callId = CallId.from(TinderPacketUtil.getChildElementString(callElement, "id", true, STANZA_DESCRIPTION, parseErrors));
            callId.ifPresent(callBuilder::setId);
            final Optional<ProfileId> profileId = ProfileId.from(TinderPacketUtil.getChildElementString(callElement, "profile", true, STANZA_DESCRIPTION, parseErrors));
            profileId.ifPresent(callBuilder::setProfileId);
            final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getChildElementString(callElement, "interest", true, STANZA_DESCRIPTION, parseErrors));
            interestId.ifPresent(callBuilder::setInterestId);
            final Optional<CallState> state = CallState.from(TinderPacketUtil.getChildElementString(callElement, "state", true, STANZA_DESCRIPTION, parseErrors));
            state.ifPresent(callBuilder::setState);
            final Optional<CallDirection> direction = CallDirection.from(TinderPacketUtil.getChildElementString(callElement, "direction", true, STANZA_DESCRIPTION, parseErrors));
            direction.ifPresent(callBuilder::setDirection);
            builder.addCall(callBuilder.build(parseErrors));
        }
        return builder.build(parseErrors);
    }

    public static final class Builder {

        @Nullable JID to;
        @Nullable JID from;
        @Nullable String id;
        @Nullable private PubSubNodeId pubSubNodeId;
        @Nonnull private List<Call> calls = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallStatusMessage build() {
            if (to == null) {
                throw new IllegalStateException("The stanza 'to' has not been set");
            }
            if (pubSubNodeId == null) {
                throw new IllegalStateException("The stanza 'pubSubNodeId' has not been set");
            }
            calls.forEach(call -> {
                if (call.getInterestId().isPresent() && !call.getInterestId().get().toPubSubNodeId().equals(pubSubNodeId)) {
                    throw new IllegalStateException(String.format("The call with id '%s' is not on this pubsub node", call.getId().orElse(null)));
                }
            });
            return build(null);
        }

        @Nonnull
        protected CallStatusMessage build(final List<String> parseErrors) {
            return new CallStatusMessage(this, parseErrors);
        }

        public Builder setTo(@Nullable JID to) {
            this.to = to;
            return this;
        }

        public Builder setFrom(@Nullable JID from) {
            this.from = from;
            return this;
        }

        public Builder setID(@Nullable String id) {
            this.id = id;
            return this;
        }

        @Nonnull
        public Builder setPubSubNodeId(@Nonnull final InterestId interestId) {
            return setPubSubNodeId(interestId.toPubSubNodeId());
        }

        @Nonnull
        public Builder setPubSubNodeId(@Nonnull final PubSubNodeId pubSubNodeId) {
            this.pubSubNodeId = pubSubNodeId;
            return this;
        }

        @Nonnull
        public Builder addCall(@Nonnull final Call call) {
            calls.add(call);
            return this;
        }
    }
}
