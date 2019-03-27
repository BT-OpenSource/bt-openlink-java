package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyPageLabel extends AbstractType<String> {
    private static final long serialVersionUID = 355142283377979642L;

    private KeyPageLabel(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyPageLabel> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyPageLabel(value));
    }

    @Nonnull
    public static KeyPageLabel from(@Nonnull final AbstractType<String> type) {
        return new KeyPageLabel(type.value());
    }
}
