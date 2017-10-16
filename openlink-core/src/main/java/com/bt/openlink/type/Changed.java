package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of recommended "Changed" values.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.7.2<br>
 * Note 1: Changed.Microphone is not part of the spec. Refer to bt-openlink-extensions<br>
 * Note 2: the order of the enum is as listed in the specification<br>
 * Note 3: The lower the number, the higher the priority to publish. So for example, if a call changes state
 * (CallEstablished to CallConferenced) and changes participant (the new conference participant), then the Changed.State
 * (priority 1) has preference over the Changed.Participant (priority 2).
 */
public enum Changed {

    STATE("State", 1),
    ACTIONS("Actions", 6),
    PARTICIPANT("Participant", 5),
    CALLER("Caller", 2),
    CALLED("Called", 3),
    PRIVACY("Privacy", 4),
    VOICE_MESSAGE("VoiceMessage", 7),
    MICROPHONE("Microphone", 8);

    @Nonnull private final String id;
    private final int priority;

    Changed(@Nonnull final String id, final int priority) {
        this.id = id;
        this.priority = priority;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public static Optional<Changed> from(@Nullable final String value) {
        for (final Changed featureType : Changed.values()) {
            if (featureType.id.equalsIgnoreCase(value)) {
                return Optional.of(featureType);
            }
        }
        return Optional.empty();
    }

    /**
     * Selects the value with the highest precedence
     *
     * @param otherValue
     *            the value to oompare against
     * @return the value with with the highest precedence
     */
    @Nonnull
    public Changed or(@Nullable final Changed otherValue) {
        if (otherValue == null || otherValue.priority > this.priority) {
            return this;
        } else {
            return otherValue;
        }
    }

}
