package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.bt.openlink.type.Call;

// TODO: (Greg 2017-10-24) Add feature support
public abstract class MakeCallResultBuilder<B extends MakeCallResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nonnull private List<Call> calls = new ArrayList<>();

    protected MakeCallResultBuilder(final Class<T> typeClass) {
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

    @Override
    protected void validate() {
        super.validate();
        if (calls.isEmpty()) {
            throw new IllegalStateException("The make-call result has no calls");
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
        if (calls.isEmpty()) {
            errors.add("Invalid make-call result stanza; missing or invalid calls");
        }
    }
}
