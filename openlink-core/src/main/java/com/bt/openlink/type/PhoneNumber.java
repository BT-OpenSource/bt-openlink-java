package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PhoneNumber extends AbstractType<String> {

    private static final long serialVersionUID = -3276853895053218217L;

    private PhoneNumber(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<PhoneNumber> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new PhoneNumber(value));
    }

    @Nonnull
    public static PhoneNumber from(@Nonnull final AbstractType<String> type) {
        return new PhoneNumber(type.value());
    }

}
