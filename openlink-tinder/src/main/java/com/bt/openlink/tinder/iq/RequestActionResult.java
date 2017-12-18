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
import com.bt.openlink.iq.RequestActionResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;

public class RequestActionResult extends OpenlinkIQ {
    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;

    private RequestActionResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.calls = Collections.unmodifiableList(builder.getCalls());
        this.callStatusBusy = builder.isCallStatusBusy().orElse(null);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_MAKE_CALL);
        TinderPacketUtil.addCallStatusCalls(outElement, callStatusBusy, calls);
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    @Nonnull
    public Optional<Boolean> isCallStatusBusy() {
        return Optional.ofNullable(callStatusBusy);
    }

    @Nonnull
    public static RequestActionResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Builder builder = Builder.start(iq);
        final Element callStatusElement = TinderPacketUtil.getChildElement(outElement, "callstatus");
        TinderPacketUtil.getBooleanAttribute(callStatusElement, "busy", "busy attribute", parseErrors).ifPresent(builder::setCallStatusBusy);
        final List<Call> calls = TinderPacketUtil.getCalls(callStatusElement, "make call result", parseErrors);
        builder.addCalls(calls);
        final RequestActionResult result = builder.build(parseErrors);
        result.setID(iq.getID());
        return result;
    }

    public static final class Builder extends RequestActionResultBuilder<Builder, JID, Type> {

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
            super(Type.class);
        }

        @Nonnull
        public RequestActionResult build() {
            super.validate();
            return new RequestActionResult(this, null);
        }

        @Nonnull
        public RequestActionResult build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new RequestActionResult(this, errors);
        }

    }

}
