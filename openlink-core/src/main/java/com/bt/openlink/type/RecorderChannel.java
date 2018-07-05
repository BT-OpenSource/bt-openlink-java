package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecorderChannel extends AbstractType<String> {

    private static final long serialVersionUID = -5548185718355299671L;

    private RecorderChannel(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<RecorderChannel> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new RecorderChannel(value));
    }

    @Nonnull
    public static RecorderChannel from(@Nonnull final AbstractType<String> type) {
        return new RecorderChannel(type.value());
    }

}
