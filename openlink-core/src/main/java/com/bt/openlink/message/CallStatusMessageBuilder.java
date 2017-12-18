package com.bt.openlink.message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

public abstract class CallStatusMessageBuilder<B extends CallStatusMessageBuilder, J> extends MessageBuilder<B, J> {

    @Nullable private Instant delay;
    @Nullable private PubSubNodeId pubSubNodeId;
    @Nullable private ItemId itemId;
    @Nullable private Boolean callStatusBusy;
    @Nonnull private List<Call> calls = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setDelay(@Nonnull Instant delay) {
        this.delay = delay;
        return (B) this;
    }

    @Nonnull
    public Optional<Instant> getDelay() {
        return Optional.ofNullable(delay);
    }

    @Nonnull
    public B setPubSubNodeId(@Nonnull final InterestId interestId) {
        return setPubSubNodeId(interestId.toPubSubNodeId());
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setPubSubNodeId(@Nonnull final PubSubNodeId pubSubNodeId) {
        this.pubSubNodeId = pubSubNodeId;
        return (B) this;
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setItemId(@Nonnull final ItemId itemId) {
        this.itemId = itemId;
        return (B) this;
    }

    @Nonnull
    public Optional<ItemId> getItemId() {
        return Optional.ofNullable(itemId);
    }

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
        if (pubSubNodeId == null) {
            throw new IllegalStateException("The stanza 'pubSubNodeId' has not been set");
        }
        validateUniqueness(callId -> {
            throw new IllegalStateException("Each call id must be unique - " + callId + " appears more than once");
        });
        validateCallsAreOnTheCorrectInterest(call -> {
            throw new IllegalStateException("The call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id " + pubSubNodeId);
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
        if (pubSubNodeId == null) {
            errors.add("Invalid callstatus message stanza; the 'pubSubNodeId' has not been set");
        }
        validateUniqueness(callId -> errors.add("Invalid callstatus message stanza; each call id must be unique - " + callId + " appears more than once"));
        validateCallsAreOnTheCorrectInterest(call -> errors.add("Invalid callstatus message stanza; the call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id " + pubSubNodeId));
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

    private void validateCallsAreOnTheCorrectInterest(final Consumer<Call> errorConsumer) {
        if (pubSubNodeId != null) {
            calls.forEach(call -> {
                final Optional<InterestId> interestId = call.getInterestId();
                if (interestId.isPresent() && !interestId.get().toPubSubNodeId().equals(pubSubNodeId)) {
                    errorConsumer.accept(call);
                }
            });
        }
    }

}
