package com.bt.openlink.type;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class ProfileId extends AbstractType<String> {

    private ProfileId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<ProfileId> from(final String value) {
        final String normalisedValue = value == null ? "" : value.trim();
        return normalisedValue.isEmpty() ? Optional.empty() : Optional.of(new ProfileId(normalisedValue));
    }

}
