package com.bt.openlink.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;

public abstract class CallStatusMessageBuilder<B extends CallStatusMessageBuilder, J> extends PubSubMessageBuilder<B, J> {

    @Nullable private Boolean callStatusBusy;
    @Nonnull private List<Call> calls = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCallStatusBusy(final boolean callStatusBusy) {
        this.callStatusBusy = callStatusBusy;
        return (B) this;
    }

    @Nonnull
    public Optional<Boolean> isCallStatusBusy() {
        return Optional.ofNullable(callStatusBusy);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addCall(@Nonnull final Call call) {
        calls.add(call);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addCalls(final List<Call> calls) {
        this.calls.addAll(calls);
        return (B) this;
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    @Override
    protected void validate() {
        super.validate();
        if (calls.isEmpty()) {
            throw new IllegalStateException("The callstatus message has no calls");
        }
        validateUniqueness(callId -> {
            throw new IllegalStateException("Each call id must be unique - " + callId + " appears more than once");
        });
        final PubSubNodeId nodeId = getPubSubNodeId().orElse(null);
        validateCallsAreOnTheCorrectInterest(nodeId, call -> {
            throw new IllegalStateException("The call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id " + nodeId);
        });
        Call.oneOrMoreCallsIsBusy(calls).ifPresent(this::setCallStatusBusy);
    }

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
        if (calls.isEmpty()) {
            errors.add("Invalid callstatus message stanza; missing or invalid calls");
        }
        validateUniqueness(callId -> errors.add("Invalid callstatus message stanza; each call id must be unique - " + callId + " appears more than once"));
        final PubSubNodeId nodeId = getPubSubNodeId().orElse(null);
        validateCallsAreOnTheCorrectInterest(nodeId, call -> errors.add("Invalid callstatus message stanza; the call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id " + nodeId));
    }

    private void validateUniqueness(final Consumer<CallId> errorConsumer) {
        for (int i = 0; i < calls.size(); i++) {
            final Optional<CallId> callIdOptional = calls.get(i).getId();
            if (callIdOptional.isPresent()) {
                for (int j = i + 1; j < calls.size(); j++) {
                    if (callIdOptional.equals(calls.get(j).getId())) {
                        errorConsumer.accept(callIdOptional.get());
                    }
                }
            }
        }
    }

    private void validateCallsAreOnTheCorrectInterest(final PubSubNodeId nodeId, final Consumer<Call> errorConsumer) {
        if (nodeId != null) {
            calls.forEach(call -> {
                final Optional<InterestId> interestId = call.getInterestId();
                if (interestId.isPresent() && !interestId.get().toPubSubNodeId().equals(nodeId)) {
                    errorConsumer.accept(call);
                }
            });
        }
    }

}
