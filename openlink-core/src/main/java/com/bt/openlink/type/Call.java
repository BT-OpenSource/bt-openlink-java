package com.bt.openlink.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: (Greg 2017-09-27) Add all call related fields to this class
public class Call {

    @Nonnull private final List<String> parseErrors;
    @Nullable private final CallId callId;
    @Nullable private final Site site;
    @Nullable private final ProfileId profileId;
    @Nullable private final InterestId interestId;
    @Nullable private final CallState state;
    @Nullable private final CallDirection direction;
    @Nullable private final Long duration;
    @Nonnull private final Collection<RequestAction> actions;

    private Call(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.callId = builder.callId;
        this.site = builder.site;
        this.profileId = builder.profileId;
        this.interestId = builder.interestId;
        this.state = builder.state;
        this.direction = builder.direction;
        this.duration = builder.duration;
        this.actions = Collections.unmodifiableCollection(builder.actions);
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = parseErrors;
        }
    }

    @Nonnull
    public List<String> parseErrors() {
        return new ArrayList<>(parseErrors);
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
    public Optional<Long> getDuration() {
        return Optional.ofNullable(duration);
    }

    @Nonnull
    public Collection<RequestAction> getActions() {
        return actions;
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
        @Nullable private Long duration;
        @Nonnull private final List<RequestAction> actions = new ArrayList<>();

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
            if (duration == null) {
                throw new IllegalStateException("The call duration has not been set");
            }

            return build(null);
        }

        @Nonnull
        public Call build(@Nullable final List<String> parseErrors) {
            return new Call(this, parseErrors);
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
        public Builder setDuration(final long duration) {
            this.duration = duration;
            return this;
        }

        @Nonnull
        public Builder addAction(@Nonnull final RequestAction action) {
            actions.add(action);
            return this;
        }

    }
}
