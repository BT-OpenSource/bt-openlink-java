package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.CallStatus;

public abstract class MakeCallResultBuilder<B extends MakeCallResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private CallStatus callStatus = null;

    protected MakeCallResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCallStatus(final CallStatus callStatus) {
        this.callStatus = callStatus;
        return (B) this;
    }

    public Optional<CallStatus> getCallStatus() {
        return Optional.ofNullable(callStatus);
    }

    @Override
    protected void validate() {
        super.validate();
        if (callStatus == null) {
            throw new IllegalStateException("The make-call result has no callstatus");
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
            errors.add("Invalid make-call result stanza; missing or invalid callstatus");
        }
    }
}
