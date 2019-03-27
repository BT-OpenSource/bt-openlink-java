package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyPageModule extends AbstractType<String> {
    private static final long serialVersionUID = 2232796305912348264L;

    private KeyPageModule(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyPageModule> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyPageModule(value));
    }

    @Nonnull
    public static KeyPageModule from(@Nonnull final AbstractType<String> type) {
        return new KeyPageModule(type.value());
    }
}
