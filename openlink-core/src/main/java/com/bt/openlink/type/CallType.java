package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of call types.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.16.1.2
 */
public enum CallType {

    INBOUND("in"),
    OUTBOUND("out"),
    MISSED("missed");

    @Nonnull private final String label;

    CallType(@Nonnull final String label) {
        this.label = label;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    @Nonnull
    public static Optional<CallType> from(@Nullable final String value) {
        for (final CallType featureType : CallType.values()) {
            if (featureType.label.equalsIgnoreCase(value)) {
                return Optional.of(featureType);
            }
        }
        return Optional.empty();
    }

}
