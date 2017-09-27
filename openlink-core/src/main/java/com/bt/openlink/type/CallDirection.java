package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public enum CallDirection {

    OUTGOING("Outgoing"),
    INCOMING("Incoming");

    @Nonnull private final String label;

    CallDirection(@Nonnull final String label) {
        this.label = label;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    public static Optional<CallDirection> from(final String value) {
        for (CallDirection callDirection : CallDirection.values()) {
            if (callDirection.label.equalsIgnoreCase(value)) {
                return Optional.of(callDirection);
            }
        }
        return Optional.empty();
    }

}
