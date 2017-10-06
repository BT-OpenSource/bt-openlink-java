package com.bt.openlink.type;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: (Greg 2017-09-27) Add all call related fields to this class
public class Call {

    @Nullable private final CallId callId;
    @Nullable private final Site site;
    @Nullable private final ProfileId profileId;
    @Nullable private final InterestId interestId;
    @Nullable private final CallState state;
    @Nullable private final CallDirection direction;
    @Nullable private final Instant startTime;
    @Nullable private final Duration duration;
    @Nonnull private final Collection<RequestAction> actions;
    @Nonnull private final List<Participant> participants;

    private Call(@Nonnull final Builder builder) {
        this.callId = builder.callId;
        this.site = builder.site;
        this.profileId = builder.profileId;
        this.interestId = builder.interestId;
        this.state = builder.state;
        this.direction = builder.direction;
        this.startTime = builder.startTime;
        this.duration = builder.duration;
        this.actions = Collections.unmodifiableCollection(builder.actions);
        this.participants = Collections.unmodifiableList(builder.participants);
    }

    @Nonnull
    public Optional<CallId> getId() {
        return Optional.ofNullable(callId);
    }

    @Nonnull
    public Optional<Site> getSite() {
        return Optional.ofNullable(site);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
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
    public Optional<Instant> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    @Nonnull
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    @Nonnull
    public Collection<RequestAction> getActions() {
        return actions;
    }

    @Nonnull
    public List<Participant> getParticipants() {
        return participants;
    }

    public boolean isParticipating() {
        return state != null && direction != null && state.isParticipating(direction);
    }

    public static final class Builder {
        @Nullable private CallId callId;
        @Nullable private Site site;
        @Nullable private ProfileId profileId;
        @Nullable private InterestId interestId;
        @Nullable private CallState state;
        @Nullable private CallDirection direction;
        @Nullable private Instant startTime;
        @Nullable private Duration duration;
        @Nonnull private final List<RequestAction> actions = new ArrayList<>();
        @Nonnull private final List<Participant> participants = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Call build() {
            if (callId == null) {
                throw new IllegalStateException("The call id has not been set");
            }
            if (site == null) {
                throw new IllegalStateException("The call site has not been set");
            }
            if (profileId == null) {
                throw new IllegalStateException("The profile id has not been set");
            }
            if (interestId == null) {
                throw new IllegalStateException("The interest id has not been set");
            }
            if (state == null) {
                throw new IllegalStateException("The call state has not been set");
            }
            if (direction == null) {
                throw new IllegalStateException("The call direction has not been set");
            }
            if (startTime == null) {
                throw new IllegalStateException("The call start time has not been set");
            }
            if (duration == null) {
                throw new IllegalStateException("The call duration has not been set");
            }

            return buildWithoutValidating();
        }

        @Nonnull
        public Call buildWithoutValidating() {
            return new Call(this);
        }

        @Nonnull
        public Builder setId(@Nonnull final CallId callId) {
            this.callId = callId;
            return this;
        }

        @Nonnull
        public Builder setSite(@Nonnull final Site site) {
            this.site = site;
            return this;
        }

        @Nonnull
        public Builder setProfileId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        @Nonnull
        public Builder setInterestId(@Nonnull final InterestId interestId) {
            this.interestId = interestId;
            return this;
        }

        @Nonnull
        public Builder setState(@Nonnull final CallState state) {
            this.state = state;
            return this;
        }

        @Nonnull
        public Builder setDirection(@Nonnull final CallDirection direction) {
            this.direction = direction;
            return this;
        }

        @Nonnull
        public Builder setStartTime(final Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        @Nonnull
        public Builder setDuration(final Duration duration) {
            this.duration = duration;
            return this;
        }

        @Nonnull
        public Builder addAction(@Nonnull final RequestAction action) {
            actions.add(action);
            return this;
        }

        @Nonnull
        public Builder addParticipant(@Nonnull final Participant participant) {
            participants.add(participant);
            return this;
        }

    }
}
