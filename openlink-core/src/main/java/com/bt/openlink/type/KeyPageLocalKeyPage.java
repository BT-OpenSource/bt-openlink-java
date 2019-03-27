package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyPageLocalKeyPage extends AbstractType<String> {
    private static final long serialVersionUID = -2180529406470205738L;

    private KeyPageLocalKeyPage(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyPageLocalKeyPage> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyPageLocalKeyPage(value));
    }

    @Nonnull
    public static KeyPageLocalKeyPage from(@Nonnull final AbstractType<String> type) {
        return new KeyPageLocalKeyPage(type.value());
    }
}
