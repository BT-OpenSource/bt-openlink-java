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
import com.bt.openlink.iq.RequestActionResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.CallStatus;

public class RequestActionResult extends OpenlinkIQ {
    @Nullable private final CallStatus callStatus;

    private RequestActionResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.callStatus = builder.getCallStatus().orElse(null);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION);
        getCallStatus().ifPresent(status->TinderPacketUtil.addCallStatus(outElement, status));
    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Nonnull
    public static RequestActionResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Builder builder = Builder.start(iq);
        TinderPacketUtil.getCallStatus(outElement, "request-action result", parseErrors).ifPresent(builder::setCallStatus);
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
