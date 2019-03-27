package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyModifier extends AbstractType<String> {
    private static final long serialVersionUID = 2817685305903967229L;

    private KeyModifier(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyModifier> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyModifier(value));
    }

    @Nonnull
    public static KeyModifier from(@Nonnull final AbstractType<String> type) {
        return new KeyModifier(type.value());
    }
}
