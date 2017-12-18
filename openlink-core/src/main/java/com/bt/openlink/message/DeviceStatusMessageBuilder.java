package com.bt.openlink.message;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.DeviceStatus;

public abstract class DeviceStatusMessageBuilder<B extends DeviceStatusMessageBuilder, J> extends PubSubMessageBuilder<B, J> {

    @Nullable private DeviceStatus deviceStatus;

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setDeviceStatus(@Nonnull final DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
        return (B) this;
    }

    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

    @Override
    protected void validate() {
        super.validate();
        if (deviceStatus == null) {
            throw new IllegalStateException("The stanza 'deviceStatus' has not been set");
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
        if (deviceStatus == null) {
            errors.add("Invalid devicestatus message stanza; the 'deviceStatus' has not been set");
        }
    }

}
