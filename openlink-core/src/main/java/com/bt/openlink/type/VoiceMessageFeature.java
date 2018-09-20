package com.bt.openlink.type;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoiceMessageFeature implements Serializable {

    private static final long serialVersionUID = -6035782720503631606L;

    @Nullable private final FeatureId featureId;
    @Nullable private final VoiceMessage voiceMessage;

    private VoiceMessageFeature(@Nonnull final Builder builder) {

        this.featureId = builder.featureId;
        this.voiceMessage = builder.voiceMessage;
    }

    @Nonnull
    public Optional<FeatureId> getId() {
        return Optional.ofNullable(featureId);
    }

    @Nonnull
    public Optional<VoiceMessage> getVoiceMessage() {
        return Optional.ofNullable(voiceMessage);
    }

    public static final class Builder {

        private Builder() {
        }

        @Nullable private FeatureId featureId = null;
        @Nullable private VoiceMessage voiceMessage;

        protected void validate() {
            if (featureId == null) {
                throw new IllegalStateException("The feature id has not been set");
            }
        }

        public void validate(final List<String> errors) {
            if (featureId == null) {
                errors.add("Invalid feature; missing feature id is mandatory");
            }
        }

        public Builder setVoiceMessage(@Nonnull final VoiceMessage voiceMessage) {
            this.voiceMessage = voiceMessage;
            return this;
        }

        public Builder setId(@Nonnull final FeatureId featureId) {
            this.featureId = featureId;
            return this;
        }

        @Nonnull
        public static VoiceMessageFeature.Builder start() {
            return new VoiceMessageFeature.Builder();
        }

        @Nonnull
        public VoiceMessageFeature build() {
            validate();
            return new VoiceMessageFeature(this);
        }

        @Nonnull
        public VoiceMessageFeature build(final List<String> errors) {
            validate(errors);
            return new VoiceMessageFeature(this);
        }
    }
}
