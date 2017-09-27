package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of recommended call state names.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.7.2, Table 3.
 */
public enum CallState {

    CallOriginated(false, true),
    CallDelivered(false, true),
    CallEstablished(true, true),
    CallFailed(false, false),
    CallConferenced(true, true),
    CallBusy(false, false),
    CallHeld(false, false),
    CallHeldElsewhere(false, false),
    CallTransferring(true, true),
    CallTransferred(true, true),
    /**
     * TransferCompleted is undocumented, currently only used by ITSCall, and should not be used. Eventually the only
     * use of it will be removed.
     */
    @Deprecated TransferCompleted(false, false),
    ConnectionBusy(false, false),
    ConnectionCleared(false, false),
    CallMissed(false, false);

    private final boolean inboundCallParticipant;
    private final boolean outboundCallParticipant;

    CallState(final boolean inboundCallParticipant, final boolean outboundCallParticipant) {
        this.inboundCallParticipant = inboundCallParticipant;
        this.outboundCallParticipant = outboundCallParticipant;
    }


    @Nonnull
    public static Optional<CallState> from(@Nullable final String value) {
        for (final CallState callState : CallState.values()) {
            if (callState.name().equals(value)) {
                return Optional.of(callState);
            }
        }
        return Optional.empty();
    }

    /**
     * Determine if a user is participating in a call with a particular callDirection
     *
     * @param callDirection
     *            The direction of the call
     * @return true if the user is participating in that call
     */
    public boolean isParticipating(@Nonnull final CallDirection callDirection) {
        switch (callDirection) {
            case Incoming:
                return inboundCallParticipant;
            case Outgoing:
                return outboundCallParticipant;
            default:
                return false;
        }
    }


}
