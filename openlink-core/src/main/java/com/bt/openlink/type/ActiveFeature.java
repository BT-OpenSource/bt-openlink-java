package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActiveFeature extends Feature {

    private static final long serialVersionUID = 7653908969576792463L;
    @Nullable private final String value1;
    @Nullable private final String value2;
    @Nullable private final String value3;

    private ActiveFeature(@Nonnull final Builder builder) {
        super(builder);
        value1 = builder.value1;
        value2 = builder.value2;
        value3 = builder.value3;
    }

    @Nonnull
    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    @Nonnull
    public Optional<String> getValue3() {
        return Optional.ofNullable(value3);
    }

    public static class Builder extends AbstractFeatureBuilder<Builder> {

        @Nullable private String value1;
        @Nullable private String value2;
        @Nullable private String value3;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue1(@Nonnull final AbstractType value1) {
            this.value1 = String.valueOf(value1);
            return this;
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue1(@Nonnull final String value1) {
            this.value1 = value1;
            return this;
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue2(@Nonnull final AbstractType value2) {
            this.value2 = String.valueOf(value2);
            return this;
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue2(@Nonnull final String value2) {
            this.value2 = value2;
            return this;
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue3(@Nonnull final AbstractType value3) {
            this.value3 = String.valueOf(value3);
            return this;
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue3(@Nonnull final String value3) {
            this.value3 = value3;
            return this;
        }

        @Nonnull
        public ActiveFeature build() {
            validate();
            return new ActiveFeature(this);
        }

        @Nonnull
        public ActiveFeature build(final List<String> errors) {
            validate(errors);
            return new ActiveFeature(this);
        }
    }

}
