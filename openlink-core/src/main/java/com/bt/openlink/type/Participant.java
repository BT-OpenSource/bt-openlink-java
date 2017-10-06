package com.bt.openlink.type;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Participant {
    @Nullable private final String jid;
    @Nullable private final ParticipantType participantType;
    @Nullable private final CallDirection direction;
    @Nullable private final Instant startTime;
    @Nullable private final Duration duration;

    private Participant(@Nonnull final Builder builder) {
        this.jid = builder.jid;
        this.participantType = builder.participantType;
        this.direction = builder.direction;
        this.startTime = builder.startTime;
        this.duration = builder.duration;
    }

    @Nonnull
    public Optional<String> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public Optional<ParticipantType> getType() {
        return Optional.ofNullable(participantType);
    }

    @Nonnull
    public Optional<CallDirection> getDirection() {
        return Optional.ofNullable(direction);
    }

    @Nonnull
    public Optional<Instant> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    @Nonnull
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    public static final class Builder {

        @Nullable private String jid;
        @Nullable private ParticipantType participantType;
        @Nullable private CallDirection direction;
        @Nullable private Instant startTime;
        @Nullable private Duration duration;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Participant build() {
            if (jid == null) {
                throw new IllegalStateException("The participation jid has not been set");
            }
            if (participantType == null) {
                throw new IllegalStateException("The participation type has not been set");
            }
            if (direction == null) {
                throw new IllegalStateException("The participation direction has not been set");
            }
            if (startTime == null) {
                throw new IllegalStateException("The participation start time has not been set");
            }
            if (duration == null) {
                throw new IllegalStateException("The participation duration has not been set");
            }
            return buildWithoutValidating();
        }

        @Nonnull
        public Participant buildWithoutValidating() {
            return new Participant(this);
        }

        public Builder setJID(@Nonnull final String jid) {
            this.jid = jid;
            return this;
        }

        public Builder setType(@Nonnull final ParticipantType participantType) {
            this.participantType = participantType;
            return this;
        }

        public Builder setDirection(@Nonnull final CallDirection direction) {
            this.direction = direction;
            return this;
        }

        public Builder setStartTime(@Nonnull final Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setDuration(@Nonnull final Duration duration) {
            this.duration = duration;
            return this;
        }
    }

}
