package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PubSubNodeId extends AbstractType<String> {

    private PubSubNodeId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<PubSubNodeId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new PubSubNodeId(value));
    }

}
