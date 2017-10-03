package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of recommended call state names.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.7.2, Table 3.
 */
public enum CallState {

    CALL_ORIGINATED("CallOriginated", false, true),
    CALL_DELIVERED("CallDelivered", false, true),
    CALL_ESTABLISHED("CallEstablished", true, true),
    CALL_FAILED("CallFailed", false, false),
    CALL_CONFERENCED("CallConferenced", true, true),
    CALL_BUSY("CallBusy", false, false),
    CALL_HELD("CallHeld", false, false),
    CALL_HELD_ELSEWHERE("CallHeldElsewhere", false, false),
    CALL_TRANSFERRING("CallTransferring", true, true),
    CALL_TRANSFERRED("CallTransferred", true, true),
    /**
     * TransferCompleted is undocumented, currently only used by ITSCall, and should not be used. Eventually the only
     * use of it will be removed.
     */
    @Deprecated TRANSFER_COMPLETED("TransferCompleted", false, false),
    CONNECTION_BUSY("ConnectionBusy", false, false),
    CONNECTION_CLEARED("ConnectionCleared", false, false),
    CALL_MISSED("CallMissed", false, false);

    @Nonnull
    private final String label;
    private final boolean inboundCallParticipant;
    private final boolean outboundCallParticipant;

    CallState(@Nonnull final String label, final boolean inboundCallParticipant, final boolean outboundCallParticipant) {
        this.label = label;
        this.inboundCallParticipant = inboundCallParticipant;
        this.outboundCallParticipant = outboundCallParticipant;
    }


    @Nonnull
    public String getLabel() {
        return label;
    }

    @Nonnull
    public static Optional<CallState> from(@Nullable final String value) {
        for (final CallState callState : CallState.values()) {
            if (callState.label.equalsIgnoreCase(value)) {
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
            case INCOMING:
                return inboundCallParticipant;
            case OUTGOING:
                return outboundCallParticipant;
        }
        throw new IllegalStateException("Unable to determine the participation state for call direction " + callDirection);
    }


}
