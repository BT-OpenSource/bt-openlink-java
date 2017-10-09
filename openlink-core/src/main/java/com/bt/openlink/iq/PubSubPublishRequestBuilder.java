package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;

public abstract class PubSubPublishRequestBuilder<B extends PubSubPublishRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private PubSubNodeId pubSubNodeId;
    @Nonnull private final List<Call> calls = new ArrayList<>();

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
    public B addCall(@Nonnull Call call) {
        this.calls.add(call);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addCalls(@Nonnull Collection<Call> calls) {
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
        if (pubSubNodeId == null) {
            throw new IllegalStateException("The stanza 'pubSubNodeId'/'interestId' has not been set");
        }
        validateUniqueness(callId -> {
            throw new IllegalStateException("Each call id must be unique - " + callId + " appears more than once");
        });
        validateCallsAreOnTheCorrectInterest(call -> {
            throw new IllegalStateException("The call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null) + " which differs from the pub-sub node id " + pubSubNodeId);
        });

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

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
        if (pubSubNodeId == null) {
            errors.add("Invalid pub-sub request stanza; missing node id/interest id");
        }
        validateUniqueness(callId -> errors.add("Invalid pub-sub request stanza; each call id must be unique - " + callId + " appears more than once"));
        validateCallsAreOnTheCorrectInterest(call -> errors.add("Invalid pub-sub request stanza; the call with id " + call.getId().orElse(null) + " is on interest " + call.getInterestId().orElse(null)
                + " which differs from the pub-sub node id " + pubSubNodeId));
    }
}
