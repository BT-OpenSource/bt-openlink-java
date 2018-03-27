package com.bt.openlink.message;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallStatus;
import com.bt.openlink.type.InterestId;

public abstract class CallStatusMessageBuilder<B extends CallStatusMessageBuilder, J> extends PubSubMessageBuilder<B, J> {

    @Nullable private CallStatus callStatus;

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCallStatus(final CallStatus callStatus) {
        this.callStatus = callStatus;
        return (B) this;
    }

    @Nonnull
    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Override
    protected void validate() {
        super.validate();
        if (callStatus == null) {
            throw new IllegalStateException("The callstatus message has no calls");
        }
        getCallStatus().ifPresent(
                status ->
                validateCallsAreOnTheCorrectInterest(status, call -> {
                    throw new IllegalStateException("The call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id "
                            + getPubSubNodeId().orElse(null));
                }));
    }

    @Override
    public void validate(final List<String> errors) {
        if (callStatus == null) {
            errors.add("Invalid callstatus message stanza; missing or invalid calls");
        }
        getCallStatus().ifPresent(
                status ->
                validateCallsAreOnTheCorrectInterest(status, call ->
                        errors.add("Invalid callstatus message stanza; the call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null)
                                + " which differs from the pub-sub node id "
                                + getPubSubNodeId().orElse(null))));
    }

    private void validateCallsAreOnTheCorrectInterest(final CallStatus callStatus, final Consumer<Call> errorConsumer) {
        getPubSubNodeId().ifPresent(pubSubNodeId ->
                callStatus.getCalls().forEach(call -> {
                    final Optional<InterestId> interestId = call.getInterestId();
                    if (interestId.isPresent() && !interestId.get().toPubSubNodeId().equals(pubSubNodeId)) {
                        errorConsumer.accept(call);
                    }
                }));
    }

}
