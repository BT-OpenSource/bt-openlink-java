package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class UserId extends AbstractType<String> {

    private static final long serialVersionUID = -2773950297673325200L;

    private UserId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<UserId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new UserId(value));
    }

    @Nonnull
    public static UserId from(@Nonnull final AbstractType<String> type) {
        return new UserId(type.value());
    }

}
