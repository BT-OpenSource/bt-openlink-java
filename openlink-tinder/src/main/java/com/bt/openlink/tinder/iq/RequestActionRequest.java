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
import com.bt.openlink.iq.RequestActionRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.RequestActionValue;

public class RequestActionRequest extends OpenlinkIQ {
    @Nullable private final InterestId interestId;
    @Nullable private final RequestAction action;
    @Nullable private final CallId callId;
    @Nullable private final RequestActionValue value1;
    @Nullable private final RequestActionValue value2;

    private RequestActionRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.interestId = builder.getInterestId().orElse(null);
        this.action = builder.getAction().orElse(null);
        this.callId = builder.getCallId().orElse(null);
        this.value1 = builder.getValue1().orElse(null);
        this.value2 = builder.getValue2().orElse(null);
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "interest", interestId);
        getAction().ifPresent(requestAction->TinderPacketUtil.addElementWithTextIfNotNull(inElement, "action", requestAction.getId()));
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "call", callId);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "value1", value1);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "value2", value2);
    }

    @Nonnull
    public static RequestActionRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        InterestId.from(TinderPacketUtil.getNullableChildElementString(inElement, "interest")).ifPresent(builder::setInterestId);
        RequestAction.from(TinderPacketUtil.getNullableChildElementString(inElement, "action")).ifPresent(builder::setAction);
        CallId.from(TinderPacketUtil.getNullableChildElementString(inElement, "call")).ifPresent(builder::setCallId);
        RequestActionValue.from(TinderPacketUtil.getNullableChildElementString(inElement, "value1")).ifPresent(builder::setValue1);
        RequestActionValue.from(TinderPacketUtil.getNullableChildElementString(inElement, "value2")).ifPresent(builder::setValue2);
        final RequestActionRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<RequestAction> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    public Optional<CallId> getCallId() {
        return Optional.ofNullable(callId);
    }

    @Nonnull
    public Optional<RequestActionValue> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    public Optional<RequestActionValue> getValue2() {
        return Optional.ofNullable(value2);
    }

    public static final class Builder extends RequestActionRequestBuilder<Builder, JID, Type> {

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
        public RequestActionRequest build() {
            super.validate();
            return new RequestActionRequest(this, null);
        }

        @Nonnull
        private RequestActionRequest build(@Nonnull final List<String> errors) {
            super.validate(errors);
            return new RequestActionRequest(this, errors);
        }

    }

}
