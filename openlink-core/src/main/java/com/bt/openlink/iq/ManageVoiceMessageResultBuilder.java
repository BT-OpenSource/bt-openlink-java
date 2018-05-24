package com.bt.openlink.iq;


import com.bt.openlink.type.DeviceStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ManageVoiceMessageResultBuilder <B extends ManageVoiceMessageResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    protected ManageVoiceMessageResultBuilder(Class<T> typeClass) {
        super(typeClass);
    }

    @Nullable private DeviceStatus deviceStatus;

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
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

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setDeviceStatus(@Nonnull final DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public Optional<DeviceStatus> getDeviceStatus() {
        return Optional.ofNullable(deviceStatus);
    }

}

