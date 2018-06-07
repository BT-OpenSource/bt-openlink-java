package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum ParticipantType {

    ACTIVE("Active"),
    ALERTING("Alerting"),
    INACTIVE("Inactive");

    @Nonnull private final String id;

    ParticipantType(@Nonnull final String id) {
        this.id = id;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public static Optional<ParticipantType> from(@Nullable final String value) {
        for (final ParticipantType featureType : ParticipantType.values()) {
            if (featureType.id.equalsIgnoreCase(value)) {
                return Optional.of(featureType);
            }
        }
        return Optional.empty();
    }

}
