package com.bt.openlink.type;

import java.util.Optional;

public enum CallDirection {

    Outgoing,
    Incoming;

    public static Optional<CallDirection> from(final String value) {
        for (CallDirection callDirection : CallDirection.values()) {
            if (callDirection.name().equalsIgnoreCase(value)) {
                return Optional.of(callDirection);
            }
        }
        return Optional.empty();
    }

}
