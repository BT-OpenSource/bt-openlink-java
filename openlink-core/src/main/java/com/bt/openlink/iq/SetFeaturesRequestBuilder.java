package com.bt.openlink.iq;

import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.ProfileId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SetFeaturesRequestBuilder<B extends SetFeaturesRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    protected SetFeaturesRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Nullable private ProfileId profileId;
    @Nullable private FeatureId featureId;
    @Nullable private String value1;
    @Nullable private String value2;
    @Nullable private String value3;

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setProfileId(@Nonnull final ProfileId profileId) {
        this.profileId = profileId;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setFeatureId(@Nonnull final FeatureId featureId) {
        this.featureId = featureId;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Optional<FeatureId> getFeatureId() {
        return Optional.ofNullable(featureId);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Optional<String> getValue3() {
        return Optional.ofNullable(value3);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue1(@Nonnull final String value1) {
        this.value1 = value1.isEmpty() ? null : value1;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue1(final boolean value1) {
        this.value1 = String.valueOf(value1);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue1(final long value1) {
        this.value1 = String.valueOf(value1);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue2(@Nonnull final String value2) {
        this.value2 = value2.isEmpty() ? null : value2;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue2(final boolean value2) {
        this.value2 = String.valueOf(value2);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue3(@Nonnull final PhoneNumber value2) {
        this.value2 = value2.value();
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue3(@Nonnull final String value3) {
        this.value3 = value3.isEmpty() ? null : value3;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue3(final boolean value3) {
        this.value3 = String.valueOf(value3);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue2(@Nonnull final PhoneNumber value3) {
        this.value3 = value3.value();
        return (B) this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (featureId == null) {
            throw new IllegalStateException("The set-features request 'featureId' has not been set");
        }
        if (value1 == null) {
            throw new IllegalStateException("The set-features request 'value1' has not been set");
        }
        if (profileId == null) {
            throw new IllegalStateException("The set-features request 'profileId' has not been set");
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
        if (featureId == null) {
            errors.add("Invalid set-features request stanza; missing featureId");
        }
        if (value1 == null) {
            errors.add("Invalid set-features request stanza; missing value1");
        }
        if (profileId == null) {
            errors.add("Invalid set-features request stanza; missing profileId");
        }
    }

}
