package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MakeCallFeature {

    @Nullable private final FeatureId featureId;
    @Nullable private final String value1;
    @Nullable private final String value2;

    MakeCallFeature(final Builder builder) {
        this.featureId = builder.featureId;
        this.value1 = builder.value1;
        this.value2 = builder.value2;
    }

    @Nonnull
    public Optional<FeatureId> getFeatureId() {
        return Optional.ofNullable(featureId);
    }

    @Nonnull
    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    public Optional<Boolean> getBooleanValue1() {
        return getValue1().flatMap(this::stringToOptionalBoolean);
    }

    private Optional<Boolean> stringToOptionalBoolean(final String val1) {
        switch (val1) {
        case "true":
            return Optional.of(Boolean.TRUE);
        case "false":
            return Optional.of(Boolean.FALSE);
        default:
            return Optional.empty();
        }
    }

    @Nonnull
    public Optional<Long> getLongValue1() {
        try {
            return getValue1().map(Long::valueOf);
        } catch (final NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    @Nonnull
    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    @Nonnull
    public Optional<Boolean> getBooleanValue2() {
        return getValue2().flatMap(this::stringToOptionalBoolean);
    }

    @Nonnull
    public Optional<PhoneNumber> getPhoneNumberValue2() {
        return getValue2().flatMap(PhoneNumber::from);
    }

    public static final class Builder {

        @Nullable private FeatureId featureId;
        @Nullable private String value1;
        @Nullable private String value2;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        public Builder setFeatureId(@Nonnull final FeatureId featureId) {
            this.featureId = featureId;
            return this;
        }

        public Builder setValue1(@Nonnull final String value1) {
            this.value1 = value1.isEmpty() ? null : value1;
            return this;
        }

        public Builder setValue1(final boolean value1) {
            this.value1 = String.valueOf(value1);
            return this;
        }

        public Builder setValue1(final long value1) {
            this.value1 = String.valueOf(value1);
            return this;
        }

        public Builder setValue2(@Nonnull final String value2) {
            this.value2 = value2.isEmpty() ? null : value2;
            return this;
        }

        public Builder setValue2(final boolean value2) {
            this.value2 = String.valueOf(value2);
            return this;
        }

        public Builder setValue2(@Nonnull final PhoneNumber value2) {
            this.value2 = value2.value();
            return this;
        }

        @Nonnull
        public MakeCallFeature build() {
            if (featureId == null) {
                throw new IllegalStateException("The feature id has not been set");
            }

            return new MakeCallFeature(this);
        }

        @Nonnull
        public MakeCallFeature build(final List<String> errors) {
            if (featureId == null) {
                errors.add("The feature id has not been set");
            }
            return new MakeCallFeature(this);
        }

    }
}
