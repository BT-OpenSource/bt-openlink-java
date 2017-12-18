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

import com.bt.openlink.message.CallStatusMessageBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;

public class CallStatusMessage extends OpenlinkPubSubMessage {

    private static final String STANZA_DESCRIPTION = "call status";

    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;
    @Nonnull private final List<String> parseErrors;

    private CallStatusMessage(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        super(builder, parseErrors);
        this.callStatusBusy = builder.isCallStatusBusy().orElse(null);
        this.calls = Collections.unmodifiableList(builder.getCalls());
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = Collections.unmodifiableList(parseErrors);
        }
        final Element messageElement = getElement();
        final Element itemElement = TinderPacketUtil.addPubSubMetaData(messageElement, builder);
        TinderPacketUtil.addCallStatusCalls(itemElement, callStatusBusy, calls);
        TinderPacketUtil.addDelay(messageElement, builder);
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
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
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start();
        final Element itemElement = TinderPacketUtil.setPubSubMetaData(message, builder, STANZA_DESCRIPTION, parseErrors);
        final Element callStatusElement = TinderPacketUtil.getChildElement(itemElement, "callstatus");
        TinderPacketUtil.getBooleanAttribute(callStatusElement, "busy", "busy attribute", parseErrors).ifPresent(builder::setCallStatusBusy);
        builder.addCalls(TinderPacketUtil.getCalls(callStatusElement, STANZA_DESCRIPTION, parseErrors));
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
