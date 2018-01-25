package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CallFeatureBoolean extends CallFeature {
    private static final long serialVersionUID = -4379867654145924237L;
    @Nullable private final Boolean enabled;

    protected CallFeatureBoolean(@Nonnull final Builder builder) {
        super(builder);
        this.enabled = builder.isEnabled();
    }

    @Nonnull
    public Optional<Boolean> isEnabled() {
        return Optional.ofNullable(enabled);
    }

    public static final class Builder extends CallFeature.AbstractCallFeatureBuilder<Builder> {

        @Nullable private Boolean enabled = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallFeatureBoolean build() {
            validate();
            return new CallFeatureBoolean(this);
        }

        @Nonnull
        public CallFeatureBoolean build(final List<String> errors) {
            validate(errors);
            return new CallFeatureBoolean(this);
        }

        @Nullable
        public Boolean isEnabled() {
            return enabled;
        }

        @Override
        protected void validate() {
            super.validate();
            if (enabled == null) {
                throw new IllegalStateException("The enabled flag must be set");
            }
        }

        @Override
        public void validate(final List<String> errors) {
            super.validate(errors);
            if (enabled == null ) {
                errors.add("Invalid feature; the enabled flag must be set");
            }
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setEnabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }
    }

}
