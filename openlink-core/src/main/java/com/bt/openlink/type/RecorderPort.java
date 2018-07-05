package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecorderPort extends AbstractType<String> {

    private static final long serialVersionUID = 2142418275707680800L;

    private RecorderPort(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<RecorderPort> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new RecorderPort(value));
    }

    @Nonnull
    public static RecorderPort from(@Nonnull final AbstractType<String> type) {
        return new RecorderPort(type.value());
    }

}
