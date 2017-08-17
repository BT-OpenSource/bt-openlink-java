package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public final class InterestId extends AbstractType<String> {

    private InterestId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<InterestId> from(final String value) {
        final String normalisedValue = value == null ? "" : value.trim();
        return normalisedValue.isEmpty() ? Optional.empty() : Optional.of(new InterestId(normalisedValue));
    }

}
