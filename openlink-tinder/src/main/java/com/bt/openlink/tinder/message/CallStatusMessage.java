package com.bt.openlink.tinder.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.message.CallStatusMessageBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.CallStatus;

public class CallStatusMessage extends OpenlinkPubSubMessage {

    private static final String STANZA_DESCRIPTION = "call status";

    @Nullable private final CallStatus callStatus;

    private CallStatusMessage(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        super(builder, parseErrors);
        this.callStatus = builder.getCallStatus().orElse(null);
        final Element messageElement = getElement();
        final Element itemElement = TinderPacketUtil.addPubSubMetaData(messageElement, builder);
        getCallStatus().ifPresent(status->TinderPacketUtil.addCallStatus(itemElement, status));
        TinderPacketUtil.addDelay(messageElement, builder);
    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Nonnull
    public static CallStatusMessage from(@Nonnull final Message message) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start();
        final Element itemElement = TinderPacketUtil.setPubSubMetaData(message, builder, STANZA_DESCRIPTION, parseErrors);
        TinderPacketUtil.getCallStatus(itemElement, "callstatus message", parseErrors).ifPresent(builder::setCallStatus);
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
            super.validate(parseErrors);
            return new CallStatusMessage(this, parseErrors);
        }
    }
}
