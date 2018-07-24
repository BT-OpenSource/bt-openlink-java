package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DeviceId extends AbstractType<String> {

    private static final long serialVersionUID = -260244141561957509L;

    private DeviceId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<DeviceId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new DeviceId(value));
    }

    @Nonnull
    public static DeviceId from(@Nonnull final AbstractType<String> type) {
        return new DeviceId(type.value());
    }

}
