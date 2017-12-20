package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConferenceId extends AbstractType<String> {

    private ConferenceId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<ConferenceId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new ConferenceId(value));
    }

}
