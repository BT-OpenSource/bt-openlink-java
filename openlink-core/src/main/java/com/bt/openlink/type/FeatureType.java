package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of recommended feature types.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.5.2<br>
 * Note; FeatureType.HEADSET is not part of the spec. Refer to bt-openlink-extensions<br>
 */
public enum FeatureType {

    MESSAGE_WAITING("MessageWaiting"),
    MICROPHONE_GAIN("MicrophoneGain"),
    MICROPHONE_MUTE("MicrophoneMute"),
    RINGER_STATUS("RingerStatus"),
    SPEAKER_MUTE("SpeakerMute"),
    SPEED_DIAL("SpeedDial"),
    GROUP_INTERCOM("GroupIntercom"),
    SPEAKER_CHANNEL("SpeakerChannel"),
    VOICE_MESSAGE("VoiceMessage"),
    VOICE_MESSAGE_PLAYLIST("VoiceMessagePlaylist"),
    VOICE_RECORDER("VoiceRecorder"),
    VOICE_BRIDGE("VoiceBridge"),
    PRIVACY("Privacy"),
    HANDSET("Handset"),
    HEADSET("Headset"),
    DO_NOT_DISTURN("DoNotDisturb"),
    CALL_FORWARD("CallForward"),
    CALL_BACK("CallBack"),
    CONFERENCE("Conference"),
    MEDIA_STREAM("MediaStream"),
    DEVICE_KEYS("DeviceKeys");

    @Nonnull private final String id;

    FeatureType(@Nonnull final String id) {
        this.id = id;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public static Optional<FeatureType> from(@Nullable final String value) {
        for (final FeatureType featureType : FeatureType.values()) {
            if (featureType.id.equalsIgnoreCase(value)) {
                return Optional.of(featureType);
            }
        }
        return Optional.empty();
    }

}
