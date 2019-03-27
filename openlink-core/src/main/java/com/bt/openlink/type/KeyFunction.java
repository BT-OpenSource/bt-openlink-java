package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyFunction extends AbstractType<String> {
    private static final long serialVersionUID = -2089005281790468854L;

    private KeyFunction(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyFunction> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyFunction(value));
    }

    @Nonnull
    public static KeyFunction from(@Nonnull final AbstractType<String> type) {
        return new KeyFunction(type.value());
    }
}
