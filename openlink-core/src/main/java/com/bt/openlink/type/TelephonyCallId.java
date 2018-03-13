package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TelephonyCallId extends AbstractType<String> {

    private static final long serialVersionUID = 5315524927016409290L;

    private TelephonyCallId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<TelephonyCallId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new TelephonyCallId(value));
    }

    @Nonnull
    public static TelephonyCallId from(@Nonnull final AbstractType<String> type) {
        return new TelephonyCallId(type.value());
    }

}
