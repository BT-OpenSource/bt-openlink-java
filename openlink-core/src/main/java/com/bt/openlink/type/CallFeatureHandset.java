package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CallFeatureHandset extends CallFeature {
    private static final long serialVersionUID = -6739347586737595986L;
    @Nullable private final Boolean enabled;
    @Nullable private final Boolean microphoneEnabled;

    private CallFeatureHandset(@Nonnull final Builder builder) {
        super(builder);
        this.enabled = builder.enabled;
        this.microphoneEnabled = builder.microphoneEnabled;
    }

    @Nonnull
    public Optional<Boolean> isEnabled() {
        return Optional.ofNullable(enabled);
    }

    @Nonnull
    public Optional<Boolean> isMicrophoneEnabled() {
        return Optional.ofNullable(microphoneEnabled);
    }

    public static final class Builder extends AbstractCallFeatureBuilder<Builder> {

        @Nullable private Boolean enabled = null;
        @Nullable private Boolean microphoneEnabled = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallFeatureHandset build() {
            setType(FeatureType.HANDSET);
            validate();
            return new CallFeatureHandset(this);
        }

        @Nonnull
        public CallFeatureHandset build(final List<String> errors) {
            setType(FeatureType.HANDSET);
            validate(errors);
            return new CallFeatureHandset(this);
        }

        @Override
        protected void validate() {
            super.validate();
            if (enabled == null) {
                throw new IllegalStateException("The handset enabled flag has not been set");
            }
        }

        @Override
        public void validate(final List<String> errors) {
            super.validate(errors);
            if (enabled == null ) {
                errors.add("Invalid feature; the handset enabled flag has not been set");
            }
        }

        @Nonnull
        public Builder setEnabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Nonnull
        public Builder setMicrophoneEnabled(final boolean microphoneEnabled) {
            this.microphoneEnabled = microphoneEnabled;
            return this;
        }
    }

}
