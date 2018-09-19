package com.bt.openlink.type;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HistoricalCall<J extends Serializable> implements Serializable {

    private static final long serialVersionUID = 6147895706303266956L;
    @Nullable private final CallId callId;
    @Nullable private final UserId userId;
    @Nullable private final InterestId interestId;
    @Nullable private final CallState state;
    @Nullable private final CallDirection direction;
    @Nullable private final PhoneNumber callerNumber;
    @Nullable private final String callerName;
    @Nullable private final PhoneNumber calledNumber;
    @Nullable private final String calledName;
    @Nullable private final Instant startTime;
    @Nullable private final Duration duration;
    @Nullable private final J tsc;

    private HistoricalCall(@Nonnull final Builder<J> builder) {
        this.callId = builder.callId;
        this.userId = builder.userId;
        this.interestId = builder.interestId;
        this.state = builder.state;
        this.direction = builder.direction;
        this.callerNumber = builder.callerNumber;
        this.callerName = builder.callerName;
        this.calledNumber = builder.calledNumber;
        this.calledName = builder.calledName;
        this.startTime = builder.startTime;
        this.duration = builder.duration;
        this.tsc = builder.tsc;
    }

    @Nonnull
    public Optional<CallId> getId() {
        return Optional.ofNullable(callId);
    }

    @Nonnull
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(userId);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<CallState> getState() {
        return Optional.ofNullable(state);
    }

    @Nonnull
    public Optional<CallDirection> getDirection() {
        return Optional.ofNullable(direction);
    }

    @Nonnull
    public Optional<PhoneNumber> getCallerNumber() {
        return Optional.ofNullable(callerNumber);
    }

    @Nonnull
    public Optional<String> getCallerName() {
        return Optional.ofNullable(callerName);
    }

    @Nonnull
    public Optional<PhoneNumber> getCalledNumber() {
        return Optional.ofNullable(calledNumber);
    }

    @Nonnull
    public Optional<String> getCalledName() {
        return Optional.ofNullable(calledName);
    }

    @Nonnull
    public Optional<Instant> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    @Nonnull
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    @Nonnull
    public Optional<J> getTsc() {
        return Optional.ofNullable(tsc);
    }

    public static final class Builder<J extends Serializable> {
        @Nullable private CallId callId;
        @Nullable private UserId userId;
        @Nullable private InterestId interestId;
        @Nullable private CallState state;
        @Nullable private CallDirection direction;
        @Nullable private PhoneNumber callerNumber;
        @Nullable private String callerName;
        @Nullable private PhoneNumber calledNumber;
        @Nullable private String calledName;
        @Nullable private Instant startTime;
        @Nullable private Duration duration;
        @Nullable private J tsc;

        private Builder() {
        }

        @Nonnull
        public static <J extends Serializable> Builder<J> start() {
            return new Builder<>();
        }

        @Nonnull
        public HistoricalCall<J> build() {
            if (callId == null) {
                throw new IllegalStateException("The call id has not been set");
            }
            if (userId == null) {
                throw new IllegalStateException("The user id has not been set");
            }
            if (interestId == null) {
                throw new IllegalStateException("The call interest has not been set");
            }
            if (state == null) {
                throw new IllegalStateException("The call state has not been set");
            }
            if (direction == null) {
                throw new IllegalStateException("The call direction has not been set");
            }
            if (callerNumber == null) {
                throw new IllegalStateException("The caller number has not been set");
            }
            if (callerName == null) {
                throw new IllegalStateException("The caller name has not been set");
            }
            if (calledNumber == null) {
                throw new IllegalStateException("The called number has not been set");
            }
            if (calledName == null) {
                throw new IllegalStateException("The called name has not been set");
            }
            if (startTime == null) {
                throw new IllegalStateException("The call start time has not been set");
            }
            if (duration == null) {
                throw new IllegalStateException("The call duration has not been set");
            }
            if (tsc == null) {
                throw new IllegalStateException("The call tsc has not been set");
            }

            return new HistoricalCall<>(this);
        }

        @Nonnull
        public HistoricalCall<J> build(@Nonnull final List<String> errors) {
            if (callId == null) {
                errors.add("Invalid historical call; missing call id is mandatory");
            }
            if (userId == null) {
                errors.add("Invalid historical call; missing user id is mandatory");
            }
            if (interestId == null) {
                errors.add("Invalid historical call; missing interest id is mandatory");
            }
            if (state == null) {
                errors.add("Invalid historical call; missing state is mandatory");
            }
            if (direction == null) {
                errors.add("Invalid historical call; missing direction is mandatory");
            }
            if (callerNumber == null) {
                errors.add("Invalid historical call; missing caller number is mandatory");
            }
            if (callerName == null) {
                errors.add("Invalid historical call; missing caller name is mandatory");
            }
            if (calledNumber == null) {
                errors.add("Invalid historical call; missing called number is mandatory");
            }
            if (calledName == null) {
                errors.add("Invalid historical call; missing called name is mandatory");
            }
            if (startTime == null) {
                errors.add("Invalid historical call; missing start time is mandatory");
            }
            if (duration == null) {
                errors.add("Invalid historical call; missing duration is mandatory");
            }
            if (tsc == null) {
                errors.add("Invalid historical call; missing TSC is mandatory");
            }

            return new HistoricalCall<>(this);
        }

        @Nonnull
        public Builder<J> setId(@Nonnull final CallId callId) {
            this.callId = callId;
            return this;
        }

        @Nonnull
        public Builder<J> setUserId(@Nonnull final UserId userId) {
            this.userId = userId;
            return this;
        }

        @Nonnull
        public Builder<J> setInterestId(@Nonnull final InterestId interestId) {
            this.interestId = interestId;
            return this;
        }

        @Nonnull
        public Builder<J> setState(@Nonnull final CallState state) {
            this.state = state;
            return this;
        }

        @Nonnull
        public Builder<J> setDirection(@Nonnull final CallDirection direction) {
            this.direction = direction;
            return this;
        }

        @Nonnull
        public Builder<J> setCallerNumber(@Nonnull final PhoneNumber callerNumber) {
            this.callerNumber = callerNumber;
            return this;
        }

        @Nonnull
        public Builder<J> setCallerName(@Nonnull final String callerName) {
            this.callerName = callerName;
            return this;
        }

        public Builder<J> setCalledNumber(@Nonnull final PhoneNumber calledNumber) {
            this.calledNumber = calledNumber;
            return this;
        }

        @Nonnull
        public Builder<J> setCalledName(@Nonnull final String calledName) {
            this.calledName = calledName;
            return this;
        }

        @Nonnull
        public Builder<J> setStartTime(@Nonnull final Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public boolean isStartTimeNull() {
            return this.startTime == null;
        }

        @Nonnull
        public Builder<J> setDuration(@Nonnull final Duration duration) {
            this.duration = duration;
            return this;
        }

        @Nonnull
        public Builder<J> setTsc(@Nonnull final J tsc) {
            this.tsc = tsc;
            return this;
        }

    }
}
