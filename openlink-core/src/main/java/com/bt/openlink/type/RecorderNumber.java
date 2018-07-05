package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecorderNumber extends AbstractType<String> {

    private static final long serialVersionUID = 1360020429961995760L;

    private RecorderNumber(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<RecorderNumber> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new RecorderNumber(value));
    }

    @Nonnull
    public static RecorderNumber from(@Nonnull final AbstractType<String> type) {
        return new RecorderNumber(type.value());
    }

}
