package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallStatus;
import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;

public abstract class PubSubPublishRequestBuilder<B extends PubSubPublishRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private PubSubNodeId pubSubNodeId;
    @Nullable private CallStatus callStatus = null;
    @Nullable private DeviceStatus deviceStatus;

    protected PubSubPublishRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setPubSubNodeId(@Nonnull final PubSubNodeId pubSubNodeId) {
        this.pubSubNodeId = pubSubNodeId;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setInterestId(@Nonnull final InterestId interestId) {
        return setPubSubNodeId(interestId.toPubSubNodeId());
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(pubSubNodeId == null ? null : pubSubNodeId.toInterestId());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setCallStatus(@Nonnull CallStatus callStatus) {
        this.callStatus = callStatus;
        return (B) this;
    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setDeviceStatus(@Nonnull final DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    @Override
    protected void validate() {
        super.validate();
        if (pubSubNodeId == null) {
            throw new IllegalStateException("The stanza 'pubSubNodeId'/'interestId' has not been set");
        }
        getCallStatus().ifPresent(cs -> validateCallsAreOnTheCorrectInterest(cs, call -> {
            throw new IllegalStateException("The call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id " + pubSubNodeId);
        }));
        if (callStatus == null && deviceStatus == null) {
            throw new IllegalStateException("Either a callstatus or a devicestatus event must be published");
        }
        if (callStatus != null && deviceStatus != null) {
            throw new IllegalStateException("A callstatus and a devicestatus event cannot be published in the same message");
        }
    }

    private void validateCallsAreOnTheCorrectInterest(final CallStatus callStatus, final Consumer<Call> errorConsumer) {
        if (pubSubNodeId != null) {
            callStatus.getCalls().forEach(call -> {
                final Optional<InterestId> interestId = call.getInterestId();
                if (interestId.isPresent() && !interestId.get().toPubSubNodeId().equals(pubSubNodeId)) {
                    errorConsumer.accept(call);
                }
            });
        }
    }

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
        if (pubSubNodeId == null) {
            errors.add("Invalid pub-sub publish request stanza; missing node id/interest id");
        }
        getCallStatus().ifPresent(
                cs -> validateCallsAreOnTheCorrectInterest(cs,
                        call -> errors.add("Invalid pub-sub publish request stanza; the call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null)
                                + " which differs from the pub-sub node id " + pubSubNodeId)));
    }
}
