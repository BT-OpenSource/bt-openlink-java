package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActiveFeature extends Feature {

    private static final long serialVersionUID = 7653908969576792463L;
    @Nullable private final String value1;
    @Nullable private final String value2;

    private ActiveFeature(@Nonnull final Builder builder) {
        super(builder);
        value1 = builder.value1;
        value2 = builder.value2;
    }

    @Nonnull
    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    public static class Builder extends AbstractFeatureBuilder<Builder> {

        @Nullable private String value1;
        @Nullable private String value2;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue1(final String value1) {
            this.value1 = value1;
            return this;
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setValue2(final String value2) {
            this.value2 = value2;
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
