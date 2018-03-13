package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PubSubNodeId extends AbstractType<String> {

    private static final long serialVersionUID = 4513498116446250445L;

    private PubSubNodeId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<PubSubNodeId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new PubSubNodeId(value));
    }

    @Nonnull
    public InterestId toInterestId() {
        return InterestId.from(value()).orElseThrow(() -> new IllegalStateException("Unable to convert PubSubNodeId to InterestId"));
    }

    @Nonnull
    public static PubSubNodeId from(@Nonnull final AbstractType<String> type) {
        return new PubSubNodeId(type.value());
    }

}
