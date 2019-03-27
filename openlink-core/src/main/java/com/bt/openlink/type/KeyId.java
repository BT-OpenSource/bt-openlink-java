package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyId extends AbstractType<String> {
    private static final long serialVersionUID = -7321320906553419756L;

    private KeyId(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyId> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyId(value));
    }

    @Nonnull
    public static KeyId from(@Nonnull final AbstractType<String> type) {
        return new KeyId(type.value());
    }
}
