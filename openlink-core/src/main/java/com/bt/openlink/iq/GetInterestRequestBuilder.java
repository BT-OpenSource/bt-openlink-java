package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.InterestId;

public abstract class GetInterestRequestBuilder<B extends GetInterestRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private InterestId interestId;

    protected GetInterestRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setInterestId(@Nonnull final InterestId interestId) {
        this.interestId = interestId;
        return (B) this;
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Override
    protected void validate() {
        super.validate();
        if (interestId == null) {
            throw new IllegalStateException("The get-interest request 'interestId' has not been set");
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
        if (interestId == null) {
            errors.add("Invalid get-interest request stanza; missing 'interest'");
        }
    }
}
