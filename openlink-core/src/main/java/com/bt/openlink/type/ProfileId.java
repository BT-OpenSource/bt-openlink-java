package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ProfileId extends AbstractType<String> {

    private static final long serialVersionUID = 8709200515112918238L;

    private ProfileId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<ProfileId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new ProfileId(value));
    }

    @Nonnull
    public static ProfileId from(@Nonnull final AbstractType<String> type) {
        return new ProfileId(type.value());
    }

}
