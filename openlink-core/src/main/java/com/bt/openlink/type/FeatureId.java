package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FeatureId extends AbstractType<String> {

    private static final long serialVersionUID = 7800997081484600725L;

    private FeatureId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<FeatureId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new FeatureId(value));
    }

    @Nonnull
    public static FeatureId from(@Nonnull final AbstractType<String> type) {
        return new FeatureId(type.value());
    }

}
