package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public final class InterestType extends AbstractType<String> {

    private InterestType(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<InterestType> from(final String value) {
        final String normalisedValue = value == null ? "" : value.trim();
        return normalisedValue.isEmpty() ? Optional.empty() : Optional.of(new InterestType(normalisedValue));
    }

}
