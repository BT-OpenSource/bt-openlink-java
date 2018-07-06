package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CallFeatureTextValue extends CallFeature {

    private static final long serialVersionUID = -558833800030593971L;

    @Nullable private final String value;

    protected CallFeatureTextValue(@Nonnull final CallFeatureTextValue.Builder builder) {
        super(builder);
        this.value = builder.getValue();
    }

    @Nonnull
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    public static final class Builder extends CallFeature.AbstractCallFeatureBuilder<CallFeatureTextValue.Builder> {

        @Nullable
        private String value = null;

        private Builder() {
        }

        @Nonnull
        public static CallFeatureTextValue.Builder start() {
            return new CallFeatureTextValue.Builder();
        }

        @Nonnull
        public CallFeatureTextValue build() {
            validate();
            return new CallFeatureTextValue(this);
        }

        @Nonnull
        public CallFeatureTextValue build(final List<String> errors) {
            validate(errors);
            return new CallFeatureTextValue(this);
        }

        @Nullable
        public String getValue() {
            return value;
        }

        @Override
        protected void validate() {
            super.validate();
            if (value == null) {
                throw new IllegalStateException("The value flag must be set");
            }
        }

        @Override
        public void validate(final List<String> errors) {
            super.validate(errors);
            if (value == null ) {
                errors.add("Invalid feature; the value flag must be set");
            }
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public CallFeatureTextValue.Builder setValue(final String value) {
            this.value = value;
            return this;
        }

    }
}
