package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyColor extends AbstractType<String> {

    private static final long serialVersionUID = 6546932668249800431L;

    private KeyColor(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyColor> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyColor(value));
    }

    @Nonnull
    public static KeyColor from(@Nonnull final AbstractType<String> type) {
        return new KeyColor(type.value());
    }
}
