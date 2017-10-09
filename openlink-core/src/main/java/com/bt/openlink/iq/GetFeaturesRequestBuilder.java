package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.ProfileId;

public abstract class GetFeaturesRequestBuilder<B extends GetFeaturesRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private ProfileId profileId;

    protected GetFeaturesRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setProfileId(@Nonnull final ProfileId profileId) {
        this.profileId = profileId;
        return (B) this;
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Override
    protected void validate() {
        super.validate();
        if (profileId == null) {
            throw new IllegalStateException("The get-features request 'profileId' has not been set");
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
        if (profileId == null) {
            errors.add("Invalid get-features request stanza; missing profile id");
        }
    }
}
