package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public final class InterestId extends AbstractType<String> {

    private static final long serialVersionUID = 943539393503149322L;

    private InterestId(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<InterestId> from(final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new InterestId(value));
    }

    @Nonnull
    public PubSubNodeId toPubSubNodeId() {
        return PubSubNodeId.from(value()).orElseThrow(()->new IllegalStateException("Unable to convert InterestId to PubSubNodeId"));
    }

}
