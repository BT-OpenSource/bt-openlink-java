package com.bt.openlink.type;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Participant implements Serializable {
    private static final long serialVersionUID = 4608389094208243328L;
    @Nullable private final String jid;
    @Nullable private final PhoneNumber number;
    @Nonnull private final List<PhoneNumber> e164Numbers;
    @Nullable private final PhoneNumber destinationNumber;
    @Nullable private final ParticipantType participantType;
    @Nullable private final CallDirection direction;
    @SuppressWarnings("squid:S3437") @Nullable private final Instant startTime;
    @SuppressWarnings("squid:S3437") @Nullable private final Duration duration;

    private Participant(@Nonnull final Builder builder) {
        this.jid = builder.jid;
        this.number = builder.number;
        this.e164Numbers = new ArrayList<>(builder.e164Numbers);
        this.destinationNumber = builder.destinationNumber;
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
    public Optional<PhoneNumber> getNumber() {
        return Optional.ofNullable(number);
    }

    @Nonnull
    public List<PhoneNumber> getE164Numbers() {
        return e164Numbers;
    }

    @Nonnull
    public Optional<PhoneNumber> getDestinationNumber() {
        return Optional.ofNullable(destinationNumber);
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
        @Nullable private PhoneNumber number;
        @Nonnull private final List<PhoneNumber> e164Numbers = new ArrayList<>();
        @Nullable private PhoneNumber destinationNumber;
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
            if (jid == null && number == null) {
                throw new IllegalStateException("Either the participation jid or number must be set");
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
            return new Participant(this);
        }

        @Nonnull
        public Participant build(final List<String> errors) {
            if (jid == null && number == null) {
                errors.add("Invalid participant; either the participation jid or number must be set");
            }
            if (participantType == null) {
                errors.add("Invalid participant; missing participation type is mandatory");
            }
            if (direction == null) {
                errors.add("Invalid participant; missing participation direction is mandatory");
            }
            if (startTime == null) {
                errors.add("Invalid participant; missing participation start time is mandatory");
            }
            if (duration == null) {
                errors.add("Invalid participant; missing participation duration is mandatory");
            }
            return new Participant(this);
        }

        public Builder setJID(@Nonnull final String jid) {
            this.jid = jid;
            return this;
        }

        public Builder setNumber(@Nonnull final PhoneNumber number) {
            this.number = number;
            return this;
        }

        public Builder addE164Number(@Nonnull final PhoneNumber e164Number) {
            this.e164Numbers.add(e164Number);
            return this;
        }

        public Builder addE164Numbers(@Nonnull final List<PhoneNumber> e164Numbers) {
            this.e164Numbers.addAll(e164Numbers);
            return this;
        }

        public Builder setDestinationNumber(@Nonnull final PhoneNumber destinationNumber) {
            this.destinationNumber = destinationNumber;
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
