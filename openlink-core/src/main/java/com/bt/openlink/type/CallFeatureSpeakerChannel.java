package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CallFeatureSpeakerChannel extends CallFeature {

    private static final long serialVersionUID = -5212589376734455893L;
    @Nullable private final Long channel;
    @Nullable private final Boolean microphoneActive;
    @Nullable private final Boolean muteRequested;

    private CallFeatureSpeakerChannel(@Nonnull final Builder builder) {
        super(builder);
        this.channel = builder.channel;
        this.microphoneActive = builder.microphoneActive;
        this.muteRequested = builder.muteRequested;
    }

    @Nonnull
    public Optional<Long> getChannel() {
        return Optional.ofNullable(channel);
    }

    @Nonnull
    public Optional<Boolean> isMicrophoneActive() {
        return Optional.ofNullable(microphoneActive);
    }

    @Nonnull
    public Optional<Boolean> isMuteRequested() {
        return Optional.ofNullable(muteRequested);
    }

    public static final class Builder extends AbstractCallFeatureBuilder<Builder> {

        @Nullable private Long channel;
        @Nullable private Boolean microphoneActive;
        @Nullable private Boolean muteRequested;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public CallFeatureSpeakerChannel build() {
            setType(FeatureType.SPEAKER_CHANNEL);
            setLabel("ignored");
            super.validate();
            if (channel == null) {
                throw new IllegalStateException("The speaker channel number has not been set");
            }
            return new CallFeatureSpeakerChannel(this);
        }

        @Nonnull
        public CallFeatureSpeakerChannel build(final List<String> errors) {
            setType(FeatureType.SPEAKER_CHANNEL);
            setLabel("ignored");
            super.validate(errors);
            if (channel == null) {
                errors.add("Invalid speaker channel feature; the speaker channel number has not been set");
            }
            return new CallFeatureSpeakerChannel(this);
        }

        @Nonnull
        public Builder setChannel(final long channel) {
            this.channel = channel;
            return this;
        }

        @Nonnull
        public Builder setMicrophoneActive(final boolean microphoneActive) {
            this.microphoneActive = microphoneActive;
            return this;
        }

        @Nonnull
        public Builder setMuteRequested(final boolean muteRequested) {
            this.muteRequested = muteRequested;
            return this;
        }

    }

}
