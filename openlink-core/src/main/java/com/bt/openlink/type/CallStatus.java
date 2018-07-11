package com.bt.openlink.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CallStatus implements Serializable {
    private static final long serialVersionUID = 1042623536248308079L;
    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;

    public CallStatus(@Nonnull final Builder builder) {
        this.callStatusBusy = builder.callStatusBusy;
        this.calls = Collections.unmodifiableList(builder.calls);
    }

    @Nonnull
    public Optional<Boolean> isCallStatusBusy() {
        return Optional.ofNullable(callStatusBusy);
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    public static class Builder {

        @Nullable private Boolean callStatusBusy;
        @Nonnull private final List<Call> calls = new ArrayList<>();

        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Builder setCallStatusBusy(final boolean callStatusBusy) {
            this.callStatusBusy = callStatusBusy;
            return this;
        }

        @Nonnull
        public Builder addCall(@Nonnull final Call call) {
            calls.add(call);
            return this;
        }

        @Nonnull
        public Builder addCalls(@Nonnull final List<Call> calls) {
            this.calls.addAll(calls);
            return this;
        }

        @Nonnull
        public CallStatus build() {
            if (calls.isEmpty()) {
                throw new IllegalStateException("The callstatus has no calls");
            }

            validateUniqueness(callId -> {
                throw new IllegalStateException("Each call id must be unique - " + callId + " appears more than once");
            });

            return new CallStatus(this);
        }

        @Nonnull
        public CallStatus build(List<String> errors) {
            if (calls.isEmpty()) {
                errors.add("Invalid callstatus; missing or invalid calls");
            }

            validateUniqueness(callId -> errors.add("Invalid callstatus; each call id must be unique - " + callId + " appears more than once"));

            return new CallStatus(this);
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

    }

}
