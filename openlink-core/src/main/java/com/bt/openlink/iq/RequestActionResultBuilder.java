package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.CallStatus;

public abstract class RequestActionResultBuilder<B extends RequestActionResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private CallStatus callStatus = null;

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
    public B setCallStatus(@Nonnull final CallStatus callStatus) {
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
            throw new IllegalStateException("The request-action result has no calls");
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
        if (callStatus == null) {
            errors.add("Invalid request-action result stanza; missing or invalid calls");
        }
    }
}
