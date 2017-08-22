package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List of recommended feature types.<br>
 * Reference: Openlink Specification xep-xxx-openlink_15-12, section 4.5.2
 */
public enum FeatureType {

    MessageWaiting,
    MicrophoneGain,
    MicrophoneMute,
    RingerStatus,
    SpeakerMute,
    SpeedDial,
    GroupIntercom,
    SpeakerChannel,
    VoiceMessage,
    VoiceMessagePlaylist,
    VoiceRecorder,
    VoiceBridge,
    Privacy,
    Handset,
    DoNotDisturb,
    CallForward,
    CallBack,
    Conference,
    MediaStream,
    DeviceKeys;

    @Nonnull
    public static Optional<FeatureType> from(@Nullable final String value) {
        for (final FeatureType featureType : FeatureType.values()) {
            if (featureType.name().equalsIgnoreCase(value)) {
                return Optional.of(featureType);
            }
        }
        return Optional.empty();
    }

}
