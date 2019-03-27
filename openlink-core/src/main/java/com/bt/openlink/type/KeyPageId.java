package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyPageId extends AbstractType<String> {
    private static final long serialVersionUID = -2433170898425306816L;

    private KeyPageId(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyPageId> from(final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyPageId(value));
    }

    @Nonnull
    public static KeyPageId from(@Nonnull final AbstractType<String> type) {
        return new KeyPageId(type.value());
    }
}
