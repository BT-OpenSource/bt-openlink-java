package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum ParticipantCategory {

    CONF("CONF"),
    ADD3P("ADD3P"),
    BARGE("BARGE");

    @Nonnull private final String id;

    ParticipantCategory(@Nonnull final String id) {
        this.id = id;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public static Optional<ParticipantCategory> from(@Nullable final String value) {
        for (final ParticipantCategory participantCategory : ParticipantCategory.values()) {
            if (participantCategory.id.equalsIgnoreCase(value)) {
                return Optional.of(participantCategory);
            }
        }
        return Optional.empty();
    }

}
