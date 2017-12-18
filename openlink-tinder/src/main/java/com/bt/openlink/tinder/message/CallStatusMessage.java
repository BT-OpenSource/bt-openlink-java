package com.bt.openlink.tinder.message;

import java.time.Instant;
import java.time.format.DateTimeParseException;
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
import com.bt.openlink.message.CallStatusMessageBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

public class CallStatusMessage extends OpenlinkMessage {

    private static final String STANZA_DESCRIPTION = "call status";

    @Nullable private final Instant delay;
    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final ItemId itemId;
    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;
    @Nonnull private final List<String> parseErrors;

    private CallStatusMessage(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        super(builder, parseErrors);
        this.delay = builder.getDelay().orElse(null);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.itemId = builder.getItemId().orElse(null);
        this.callStatusBusy = builder.isCallStatusBusy().orElse(null);
        this.calls = Collections.unmodifiableList(builder.getCalls());
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = Collections.unmodifiableList(parseErrors);
        }
        final Element messageElement = getElement();
        final Element eventElement = messageElement.addElement("event", OpenlinkXmppNamespace.XMPP_PUBSUB_EVENT.uri());
        final Element itemsElement = eventElement.addElement("items");
        getPubSubNodeId().ifPresent(nodeId -> itemsElement.addAttribute("node", nodeId.value()));
        final Element itemElement = itemsElement.addElement("item");
        getItemId().ifPresent(id -> itemElement.addAttribute("id", id.value()));
        TinderPacketUtil.addCallStatusCalls(itemElement, callStatusBusy, calls);
        getDelay().ifPresent(stamp -> messageElement.addElement("delay", "urn:xmpp:delay").addAttribute("stamp", stamp.toString()));
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
    }

    @Nonnull
    public Optional<Instant> getDelay() {
        return Optional.ofNullable(delay);
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public Optional<ItemId> getItemId() {
        return Optional.ofNullable(itemId);
    }

    @Nonnull
    public Optional<Boolean> isCallStatusBusy() {
        return Optional.ofNullable(callStatusBusy);
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    @Nonnull
    public static CallStatusMessage from(@Nonnull final Message message) {
        final Builder builder = Builder.start()
                .setId(message.getID())
                .setFrom(message.getFrom())
                .setTo(message.getTo());
        final List<String> parseErrors = new ArrayList<>();
        final Element itemsElement = message.getChildElement("event", "http://jabber.org/protocol/pubsub#event").element("items");
        final Element itemElement = itemsElement.element("item");
        final Element callStatusElement = TinderPacketUtil.getChildElement(itemElement, "callstatus");
        final Element delayElement = message.getChildElement("delay", "urn:xmpp:delay");
        PubSubNodeId.from(itemsElement.attributeValue("node")).ifPresent(builder::setPubSubNodeId);
        ItemId.from(TinderPacketUtil.getNullableStringAttribute(itemElement, "id")).ifPresent(builder::setItemId);
        TinderPacketUtil.getBooleanAttribute(callStatusElement, "busy", "busy attribute", parseErrors).ifPresent(builder::setCallStatusBusy);
        builder.addCalls(TinderPacketUtil.getCalls(callStatusElement, STANZA_DESCRIPTION, parseErrors));
        final Optional<String> stampOptional = TinderPacketUtil.getStringAttribute(delayElement, "stamp");
        if (stampOptional.isPresent()) {
            final String stamp = stampOptional.get();
            try {
                builder.setDelay(Instant.parse(stamp));
            } catch (final DateTimeParseException e) {
                parseErrors.add(String.format("Invalid %s; invalid timestamp '%s'; format should be compliant with XEP-0082", STANZA_DESCRIPTION, stamp));
            }
        }
        return builder.build(parseErrors);
    }

    public static final class Builder extends CallStatusMessageBuilder<Builder, JID> {

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallStatusMessage build() {
            super.validate();
            return new CallStatusMessage(this, null);
        }

        @Nonnull
        protected CallStatusMessage build(final List<String> parseErrors) {
            super.validate(parseErrors, true);
            return new CallStatusMessage(this, parseErrors);
        }
    }
}
