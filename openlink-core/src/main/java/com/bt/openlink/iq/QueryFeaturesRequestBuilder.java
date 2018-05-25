package com.bt.openlink.iq;

import com.bt.openlink.type.ProfileId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class QueryFeaturesRequestBuilder<B extends QueryFeaturesRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable
    private ProfileId profileId;

    protected QueryFeaturesRequestBuilder(final Class<T> typeClass) {
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
            throw new IllegalStateException("The query-features request 'profileId' has not been set");
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
            errors.add("Invalid query-features request stanza; missing profile id");
        }
    }
}