package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyLabel extends AbstractType<String> {
    private static final long serialVersionUID = 6991362505475284760L;

    private KeyLabel(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyLabel> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyLabel(value));
    }

    @Nonnull
    public static KeyLabel from(@Nonnull final AbstractType<String> type) {
        return new KeyLabel(type.value());
    }
}
