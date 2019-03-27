package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Profile;

public abstract class GetProfileResultBuilder<B extends GetProfileResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private Profile profile;

    protected GetProfileResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setProfile(@Nonnull final Profile profile) {
        this.profile = profile;
        return (B) this;
    }

    @Nonnull
    public Optional<Profile> getProfile() {
        return Optional.ofNullable(profile);
    }

    @Override
    protected void validate() {
        super.validate();
        if (profile == null) {
            throw new IllegalStateException("The get-profile result 'profile' has not been set");
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
        if (profile == null) {
            errors.add("Invalid get-profile result stanza; missing 'profile'");
        }
    }
}
