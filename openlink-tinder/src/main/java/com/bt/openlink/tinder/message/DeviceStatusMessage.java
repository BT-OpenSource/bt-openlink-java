package com.bt.openlink.tinder.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.message.DeviceStatusMessageBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.DeviceStatus;

public class DeviceStatusMessage extends OpenlinkPubSubMessage {

    private static final String STANZA_DESCRIPTION = "device status";

    @Nullable private final DeviceStatus deviceStatus;

    private DeviceStatusMessage(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        super(builder, parseErrors);
        this.deviceStatus = builder.getDeviceStatus().orElse(null);
        final Element messageElement = getElement();
        final Element itemElement = TinderPacketUtil.addPubSubMetaData(messageElement, builder);
        getDeviceStatus().ifPresent(status -> TinderPacketUtil.addDeviceStatus(itemElement, status));
        TinderPacketUtil.addDelay(messageElement, builder);
    }

    @Nonnull
    public static DeviceStatusMessage from(@Nonnull final Message message) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start();
        final Element itemElement = TinderPacketUtil.setPubSubMetaData(message, builder, STANZA_DESCRIPTION, parseErrors);
        final Element deviceStatusElement = TinderPacketUtil.getChildElement(itemElement, "devicestatus");
        TinderPacketUtil.getDeviceStatus(deviceStatusElement, STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setDeviceStatus);
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
