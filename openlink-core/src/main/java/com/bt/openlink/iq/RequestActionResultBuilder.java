package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Call;

public abstract class RequestActionResultBuilder<B extends RequestActionResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private Boolean callStatusBusy = null;
    @Nonnull private List<Call> calls = new ArrayList<>();

    protected RequestActionResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addCall(@Nonnull final Call call) {
        this.calls.add(call);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addCalls(@Nonnull final Collection<Call> calls) {
        this.calls.addAll(calls);
        return (B) this;
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
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

    @Override
    protected void validate() {
        super.validate();
        if (calls.isEmpty()) {
            throw new IllegalStateException("The make-call result has no calls");
        }
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
            errors.add("Invalid make-call result stanza; missing or invalid calls");
        }
    }
}
