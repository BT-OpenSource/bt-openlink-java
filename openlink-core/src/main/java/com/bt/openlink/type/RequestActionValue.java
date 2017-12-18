package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RequestActionValue extends AbstractType<String> {

    private RequestActionValue(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<RequestActionValue> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new RequestActionValue(value));
    }

}
