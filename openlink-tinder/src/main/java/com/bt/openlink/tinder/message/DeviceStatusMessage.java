package com.bt.openlink.tinder.message;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.message.DeviceStatusMessageBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

public class DeviceStatusMessage extends OpenlinkPubSubMessage {

    private static final String STANZA_DESCRIPTION = "device status";

    @Nullable private final DeviceStatus deviceStatus;

    private DeviceStatusMessage(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        super(builder, parseErrors);
        this.deviceStatus = builder.getDeviceStatus().orElse(null);
        final Element messageElement = getElement();
        final Element eventElement = messageElement.addElement("event", OpenlinkXmppNamespace.XMPP_PUBSUB_EVENT.uri());
        final Element itemsElement = eventElement.addElement("items");
        getPubSubNodeId().ifPresent(nodeId -> itemsElement.addAttribute("node", nodeId.value()));
        final Element itemElement = itemsElement.addElement("item");
        getItemId().ifPresent(id -> itemElement.addAttribute("id", id.value()));
        getDeviceStatus().ifPresent(status -> TinderPacketUtil.addDeviceStatus(itemElement, status));
        getDelay().ifPresent(stamp -> messageElement.addElement("delay", "urn:xmpp:delay").addAttribute("stamp", stamp.toString()));
    }

    @Nonnull
    public static DeviceStatusMessage from(@Nonnull final Message message) {
        final Builder builder = Builder.start()
                .setId(message.getID())
                .setFrom(message.getFrom())
                .setTo(message.getTo());
        final List<String> parseErrors = new ArrayList<>();
        final Element itemsElement = message.getChildElement("event", "http://jabber.org/protocol/pubsub#event").element("items");
        final Element itemElement = itemsElement.element("item");
        final Element deviceStatusElement = TinderPacketUtil.getChildElement(itemElement, "devicestatus");
        final Element delayElement = message.getChildElement("delay", "urn:xmpp:delay");
        PubSubNodeId.from(itemsElement.attributeValue("node")).ifPresent(builder::setPubSubNodeId);
        ItemId.from(TinderPacketUtil.getNullableStringAttribute(itemElement, "id")).ifPresent(builder::setItemId);
        TinderPacketUtil.getDeviceStatus(deviceStatusElement, STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setDeviceStatus);
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

    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    public static final class Builder extends DeviceStatusMessageBuilder<Builder, JID> {

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public DeviceStatusMessage build() {
            super.validate();
            return new DeviceStatusMessage(this, null);
        }

        @Nonnull
        protected DeviceStatusMessage build(final List<String> parseErrors) {
            super.validate(parseErrors, true);
            return new DeviceStatusMessage(this, parseErrors);
        }
    }
}
