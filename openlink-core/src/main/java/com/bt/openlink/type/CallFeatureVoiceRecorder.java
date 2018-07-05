package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CallFeatureVoiceRecorder extends CallFeature {

    private static final long serialVersionUID = 3298067290920229252L;

    @Nullable private final VoiceRecorderInfo voiceRecorderInfo;

    CallFeatureVoiceRecorder(@Nonnull final CallFeatureVoiceRecorder.Builder builder) {
        super(builder);
        this.voiceRecorderInfo = builder.voiceRecorderInfo;
    }

    @Nonnull
    public Optional<VoiceRecorderInfo> getVoiceRecorderInfo() {
        return Optional.ofNullable(voiceRecorderInfo);
    }

    public static final class Builder extends AbstractCallFeatureBuilder<CallFeatureVoiceRecorder.Builder> {

        @Nullable private VoiceRecorderInfo voiceRecorderInfo = null;

        private Builder() {
        }

        @Nonnull
        public static CallFeatureVoiceRecorder.Builder start() {
            return new CallFeatureVoiceRecorder.Builder();
        }

        @Nonnull
        public CallFeatureVoiceRecorder build() {
            setType(FeatureType.VOICE_RECORDER);
            setLabel("ignored");
            validate();
            return new CallFeatureVoiceRecorder(this);
        }

        @Nonnull
        public CallFeatureVoiceRecorder build(final List<String> errors) {
            setType(FeatureType.VOICE_RECORDER);
            setLabel("ignored");
            validate(errors);
            return new CallFeatureVoiceRecorder(this);
        }

        @Override
        protected void validate() {
            super.validate();
            if (voiceRecorderInfo == null) {
                throw new IllegalStateException("The VoiceRecorder information must be set");
            }
        }

        @Override
        public void validate(final List<String> errors) {
            super.validate(errors);
            if (voiceRecorderInfo == null) {
                errors.add("Invalid feature; the VoiceRecorder information must be set");
            }
        }

        @SuppressWarnings("unchecked")
        @Nonnull
        public CallFeatureVoiceRecorder.Builder setVoiceRecorderInfo(@Nonnull final VoiceRecorderInfo voiceRecorderInfo) {
            this.voiceRecorderInfo = voiceRecorderInfo;
            return this;
        }
    }
}
