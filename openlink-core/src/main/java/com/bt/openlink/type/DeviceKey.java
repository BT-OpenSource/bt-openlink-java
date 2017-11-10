package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DeviceKey extends AbstractType<String> {

    private DeviceKey(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<DeviceKey> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new DeviceKey(value));
    }

}
