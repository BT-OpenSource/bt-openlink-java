package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CallFeature extends Feature {
    @Nullable private final Boolean enabled;
    @Nullable private final DeviceKey deviceKey;

    private CallFeature(@Nonnull final Builder builder) {
        super(builder);
        this.enabled = builder.enabled;
        this.deviceKey = builder.deviceKey;
    }

    @Nonnull
    public Optional<Boolean> isEnabled() {
        return Optional.ofNullable(enabled);
    }

    @Nonnull
    public Optional<DeviceKey> getDeviceKey() {
        return Optional.ofNullable(deviceKey);
    }

    public static final class Builder extends Feature.AbstractFeatureBuilder<Builder> {

        @Nullable private Boolean enabled = null;
        @Nullable private DeviceKey deviceKey = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallFeature build() {
            super.validate();
            if (enabled == null && deviceKey == null) {
                throw new IllegalStateException("Either the enabled flag or the device keys must be set");
            }
            if (enabled != null && deviceKey != null) {
                throw new IllegalStateException("The enabled flag and the device keys cannot both be set");
            }
            return new CallFeature(this);
        }

        @Nonnull
        public CallFeature build(final List<String> errors) {
            super.validate(errors);
            if (enabled == null && deviceKey == null) {
                errors.add("Invalid feature; either the enabled flag or the device keys must be set");
            }
            if (enabled != null && deviceKey != null) {
                errors.add("Invalid feature; the enabled flag and the device keys cannot both be set");
            }
            return new CallFeature(this);
        }

        @Nonnull
        public Builder setEnabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Nonnull
        public Builder setDeviceKey(@Nonnull final DeviceKey deviceKey) {
            this.deviceKey = deviceKey;
            return this;
        }
    }

}
