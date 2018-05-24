package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * List of statuses of a VoiceMessage.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.10.2
 */
public enum  VoiceMessageStatus {

    OK("ok"),
    ERROR("error"),
    WARN("warn"),
    UNKNOWN("unknown");

    @Nonnull private final String label;

    VoiceMessageStatus(@Nonnull final String label) {
        this.label = label;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    @Nonnull
    public static Optional<VoiceMessageStatus> from(@Nullable final String value) {
        for (final VoiceMessageStatus status : VoiceMessageStatus.values()) {
            if (status.label.equalsIgnoreCase(value)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
