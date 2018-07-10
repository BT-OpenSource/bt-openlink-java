package com.bt.openlink.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class CallFeatureDeviceKey extends CallFeature {
    private static final long serialVersionUID = -1837276539059039203L;
    @Nonnull private final List<DeviceKey> deviceKeys;

    protected CallFeatureDeviceKey(@Nonnull final Builder builder) {
        super(builder);
        this.deviceKeys = Collections.unmodifiableList(builder.deviceKeys);
    }

    @Nonnull
    public List<DeviceKey> getDeviceKeys() {
        return deviceKeys;
    }

    public static final class Builder extends AbstractCallFeatureBuilder<Builder> {

        @Nonnull private final List<DeviceKey> deviceKeys = new ArrayList<>();

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

        @Override
        protected void validate() {
            super.validate();
            if (deviceKeys.isEmpty()) {
                throw new IllegalStateException("At least one device key must be set");
            }
        }

        @Override
        public void validate(final List<String> errors) {
            super.validate(errors);
            if (deviceKeys.isEmpty()) {
                errors.add("Invalid feature; at least one device key must be set");
            }
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public Builder addDeviceKey(@Nonnull final DeviceKey deviceKey) {
            this.deviceKeys.add(deviceKey);
            return this;
        }
    }

}
