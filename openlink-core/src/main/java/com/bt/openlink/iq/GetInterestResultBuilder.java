package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Interest;

public abstract class GetInterestResultBuilder<B extends GetInterestResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private Interest interest;

    protected GetInterestResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setInterest(@Nonnull final Interest interest) {
        this.interest = interest;
        return (B) this;
    }

    @Nonnull
    public Optional<Interest> getInterest() {
        return Optional.ofNullable(interest);
    }

    @Override
    protected void validate() {
        super.validate();
        if (interest == null) {
            throw new IllegalStateException("The get-interest result 'interest' has not been set");
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
        if (interest == null) {
            errors.add("Invalid get-interest result stanza; missing 'interest'");
        }
    }
}
