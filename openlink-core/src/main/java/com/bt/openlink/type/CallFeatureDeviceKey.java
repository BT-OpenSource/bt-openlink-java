package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CallFeatureDeviceKey extends CallFeature {
    private static final long serialVersionUID = -1837276539059039203L;
    @Nullable private final DeviceKey deviceKey;

    protected CallFeatureDeviceKey(@Nonnull final Builder builder) {
        super(builder);
        this.deviceKey = builder.getDeviceKey();
    }

    @Nonnull
    public Optional<DeviceKey> getDeviceKey() {
        return Optional.ofNullable(deviceKey);
    }

    public static final class Builder extends AbstractCallFeatureBuilder<Builder> {

        @Nullable private DeviceKey deviceKey = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallFeatureDeviceKey build() {
            validate();
            return new CallFeatureDeviceKey(this);
        }

        @Nonnull
        public CallFeatureDeviceKey build(final List<String> errors) {
            validate(errors);
            return new CallFeatureDeviceKey(this);
        }

        @Nullable
        public DeviceKey getDeviceKey() {
            return deviceKey;
        }

        @Override
        protected void validate() {
            super.validate();
            if (deviceKey == null) {
                throw new IllegalStateException("The device key must be set");
            }
        }

        @Override
        public void validate(final List<String> errors) {
            super.validate(errors);
            if (deviceKey == null) {
                errors.add("Invalid feature; the device key must be set");
            }
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder setDeviceKey(@Nonnull final DeviceKey deviceKey) {
            this.deviceKey = deviceKey;
            return this;
        }
    }

}
