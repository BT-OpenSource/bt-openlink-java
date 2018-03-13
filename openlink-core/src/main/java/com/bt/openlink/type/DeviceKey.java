package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DeviceKey extends AbstractType<String> {

    private static final long serialVersionUID = 4457941703354039912L;

    private DeviceKey(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<DeviceKey> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new DeviceKey(value));
    }

    @Nonnull
    public static DeviceKey from(@Nonnull final AbstractType<String> type) {
        return new DeviceKey(type.value());
    }

}
