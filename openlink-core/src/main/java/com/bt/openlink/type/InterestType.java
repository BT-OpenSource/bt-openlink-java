package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public final class InterestType extends AbstractType<String> {

    private InterestType(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<InterestType> from(final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new InterestType(value));
    }

}
