package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;

public enum RequestAction {
    ANSWER_CALL("AnswerCall", "Answer a ringing call", 0, 1),
    HOLD_CALL("HoldCall", "Place a call on hold", 0),
    INTERCOM_TRANSFER("IntercomTransfer", "Transfer a call to an internal users intercom line", 1),
    JOIN_CALL("JoinCall", "Join a connected or conferenced call", 0, 1),
    RETRIEVE_CALL("RetrieveCall", "Retrieve a held call", 0, 1),
    CONSULTATION_CALL("ConsultationCall", "Start a two step transfer to another party", 0, 2),
    TRANSFER_CALL("TransferCall", "Complete a two step transfer", 0),
    CONFERENCE_CALL("ConferenceCall", "Conference both parties together", 0),
    SINGLE_STEP_TRANSFER("SingleStepTransfer", "Single step transfer a call to another party", 1),
    SEND_DIGIT("SendDigit", "Send a single DTMF digit on a connected call", 1),
    SEND_DIGITS("SendDigits", "Send the number to dial on a selected line", 1),
    CLEAR_CONFERENCE("ClearConference", "Force all participants off a conference call, disconnecting all parties", 0),
    CLEAR_CONNECTION("ClearConnection", "Remove the user from a call", 0),
    CLEAR_CALL("ClearCall", "Remove all participants from a call", 0),
    START_VOICE_DROP("StartVoiceDrop", "Start a voice message drop", 2),
    STOP_VOICE_DROP("StopVoiceDrop", "Stop a voice message drop", 2),
    PRIVATE_CALL("PrivateCall", "Make a call private", 0),
    PUBLIC_CALL("PublicCall", "Make a call public", 0),
    ADD_THIRD_PARTY("AddThirdParty", "Add a third party to a call, making it a conference if necessary", 1),
    REMOVE_THIRD_PARTY("RemoveThirdParty", "Remove a third party from a call", 1),
    CONNECT_SPEAKER("ConnectSpeaker", "Put a call on a speaker channel", 1),
    DISCONNECT_SPEAKER("DisconnectSpeaker", "Remove a call from a speaker channel", 1);

    @Nonnull private final String id;
    @Nonnull private final String label;
    private final int minValueCount;
    private final int maxValueCount;

    RequestAction(@Nonnull final String id, @Nonnull final String label, final int valueCount) {
        this(id, label, valueCount, valueCount);
    }

    RequestAction(@Nonnull final String id, @Nonnull final String label, final int minValueCount, final int maxValueCount) {
        this.id = id;
        this.label = label;
        this.minValueCount = minValueCount;
        this.maxValueCount = maxValueCount;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    public int getMinValueCount() {
        return minValueCount;
    }

    public int getMaxValueCount() {
        return maxValueCount;
    }

    public static Optional<RequestAction> from(final String value) {
        for (final RequestAction requestAction : RequestAction.values()) {
            if (requestAction.id.equalsIgnoreCase(value)) {
                return Optional.of(requestAction);
            }
        }
        return Optional.empty();
    }

}
