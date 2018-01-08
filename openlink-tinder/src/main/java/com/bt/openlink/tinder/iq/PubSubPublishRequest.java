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
import com.bt.openlink.iq.PubSubPublishRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.PubSubNodeId;

public class PubSubPublishRequest extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "PubSub unsubscribe request";

    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;
    @Nullable private final DeviceStatus deviceStatus;

    private PubSubPublishRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.calls = Collections.unmodifiableList(builder.getCalls());
        this.callStatusBusy = builder.isCallStatusBusy().orElse(null);
        this.deviceStatus = builder.getDeviceStatus().orElse(null);
        final Element pubSubElement = this.getElement().addElement("pubsub", OpenlinkXmppNamespace.XMPP_PUBSUB.uri());
        final Element publishElement = pubSubElement.addElement("publish");
        getPubSubNodeId().ifPresent(nodeId -> publishElement.addAttribute("node", nodeId.value()));
        final Element itemElement = publishElement.addElement("item");
        if (!calls.isEmpty()) {
            TinderPacketUtil.addCallStatusCalls(itemElement, callStatusBusy, calls);
        }
        getDeviceStatus().ifPresent(status -> TinderPacketUtil.addDeviceStatus(itemElement, status));
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
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
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static PubSubPublishRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element publishElement = TinderPacketUtil.getChildElement(iq.getElement(), "pubsub", "publish");
        final Element itemElement = TinderPacketUtil.getChildElement(publishElement, "item");
        final Element callStatusElement = TinderPacketUtil.getChildElement(itemElement, "callstatus");
        PubSubNodeId.from(TinderPacketUtil.getNullableStringAttribute(publishElement, "node")).ifPresent(builder::setPubSubNodeId);
        TinderPacketUtil.getBooleanAttribute(callStatusElement, "busy", "busy attribute", parseErrors).ifPresent(builder::setCallStatusBusy);
        builder.addCalls(TinderPacketUtil.getCalls(callStatusElement, STANZA_DESCRIPTION, parseErrors));
        final Element deviceStatusElement = TinderPacketUtil.getChildElement(itemElement, "devicestatus");
        TinderPacketUtil.getDeviceStatus(deviceStatusElement, STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setDeviceStatus);
        final PubSubPublishRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends PubSubPublishRequestBuilder<Builder, JID, Type> {

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
            super(IQ.Type.class);
        }

        @Nonnull
        public PubSubPublishRequest build() {
            super.validate();
            return new PubSubPublishRequest(this, null);
        }

        @Nonnull
        private PubSubPublishRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new PubSubPublishRequest(this, errors);
        }
    }

}
