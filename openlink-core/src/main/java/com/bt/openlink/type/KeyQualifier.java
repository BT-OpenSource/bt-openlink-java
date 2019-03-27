package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyQualifier extends AbstractType<String> {
    private static final long serialVersionUID = -4885867972468101018L;

    private KeyQualifier(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyQualifier> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyQualifier(value));
    }

    @Nonnull
    public static KeyQualifier from(@Nonnull final AbstractType<String> type) {
        return new KeyQualifier(type.value());
    }
}
