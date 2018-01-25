package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FeatureId extends AbstractType<String> {

    private static final long serialVersionUID = 5497927667925440552L;

    private FeatureId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<FeatureId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new FeatureId(value));
    }

}
