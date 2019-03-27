package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public class KeyInterest extends AbstractType<String> {
    private static final long serialVersionUID = -7855027712546716837L;

    private KeyInterest(@Nonnull String value) {
        super(value);
    }

    @Nonnull
    public static Optional<KeyInterest> from(String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new KeyInterest(value));
    }

    @Nonnull
    public static KeyInterest from(@Nonnull final AbstractType<String> type) {
        return new KeyInterest(type.value());
    }
}
