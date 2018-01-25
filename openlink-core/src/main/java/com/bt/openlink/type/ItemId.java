package com.bt.openlink.type;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ItemId extends AbstractType<String> {

    private static final long serialVersionUID = -7254784162238934557L;

    private ItemId(final String value) {
        super(value);
    }

    @Nonnull
    public static ItemId random() {
        return new ItemId(UUID.randomUUID().toString());
    }


    @Nonnull
    public static Optional<ItemId> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new ItemId(value));
    }

}
