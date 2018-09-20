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
import com.bt.openlink.iq.ManageVoiceMessageResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.DeviceStatus;

public class ManageVoiceMessageResult extends OpenlinkIQ {
    @Nullable private final DeviceStatus deviceStatus;

    private ManageVoiceMessageResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        deviceStatus = builder.getDeviceStatus().orElse(null);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_MANAGE_VOICE_MESSAGE);
        getDeviceStatus().ifPresent(status->TinderPacketUtil.addDeviceStatus(outElement, status));
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    @Nonnull
    public static ManageVoiceMessageResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element deviceStatusElement = TinderPacketUtil.getChildElement(outElement, "devicestatus");
        final Builder builder = Builder.start(iq);
        TinderPacketUtil.getDeviceStatus(deviceStatusElement, "manage-voice-message result", parseErrors).ifPresent(builder::setDeviceStatus);
        final ManageVoiceMessageResult result = builder.build(parseErrors);
        result.setID(iq.getID());
        return result;
    }

    public static final class Builder extends ManageVoiceMessageResultBuilder<Builder, JID, Type> {

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

        /**
         * Convenience method to create a new {@link Builder} based on a {@link Type#get IQ.Type.get} or {@link Type#set
         * IQ.Type.set} IQ. The new builder will be initialized with:
         * <ul>
         *
         * <li>The sender set to the recipient of the originating IQ.
         * <li>The recipient set to the sender of the originating IQ.
         * <li>The id set to the id of the originating IQ.
         * </ul>
         *
         * @param request
         *            the {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ packet.
         * @throws IllegalArgumentException
         *             if the IQ packet does not have a type of {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set}.
         * @return a new {@link Builder} based on the originating IQ.
         */
        @SuppressWarnings("WeakerAccess")
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return start(IQ.createResultIQ(request));
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public ManageVoiceMessageResult build() {
            super.validate();
            return new ManageVoiceMessageResult(this, null);
        }

        @Nonnull
        public ManageVoiceMessageResult build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new ManageVoiceMessageResult(this, errors);
        }

    }

}
