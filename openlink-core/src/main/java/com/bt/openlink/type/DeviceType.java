package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DeviceType extends AbstractType<String> {

    private static final long serialVersionUID = -4735119745606815552L;

    private DeviceType(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<DeviceType> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new DeviceType(value));
    }

    @Nonnull
    public static DeviceType from(@Nonnull final AbstractType<String> type) {
        return new DeviceType(type.value());
    }

}
