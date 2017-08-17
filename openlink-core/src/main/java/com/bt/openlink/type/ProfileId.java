package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public final class ProfileId extends AbstractType<String> {

    private ProfileId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<ProfileId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new ProfileId(value));
    }

}
