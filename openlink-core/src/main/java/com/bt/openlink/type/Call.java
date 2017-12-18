package com.bt.openlink.type;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Call {

    public static Optional<Boolean> oneOrMoreCallsIsBusy(final Collection<Call> calls) {
        final AtomicBoolean callBusySet = new AtomicBoolean(false);
        final AtomicBoolean aCallIsBusy = new AtomicBoolean(false);
        calls.forEach(call -> call.getState().ifPresent(callState -> call.getDirection().ifPresent(callDirection -> {
            final boolean participating = callState.isParticipating(callDirection);
            if (!callBusySet.get() || participating) {
                callBusySet.compareAndSet(false, true);
                aCallIsBusy.compareAndSet(false, participating);
            }
        })));

        if (callBusySet.get()) {
            return Optional.of(aCallIsBusy.get());
        } else {
            return Optional.empty();
        }

    }

    @Nullable private final CallId callId;
    @Nullable private final Site site;
    @Nullable private final ProfileId profileId;
    @Nullable private final InterestId interestId;
    @Nullable private final Changed changed;
    @Nullable private final CallState state;
    @Nullable private final CallDirection direction;
    @Nullable private PhoneNumber callerNumber;
    @Nullable private String callerName;
    @Nonnull private final List<PhoneNumber> callerE164Numbers;
    @Nullable private PhoneNumber calledNumber;
    @Nullable private String calledName;
    @Nullable private PhoneNumber calledDestination;
    @Nonnull private final List<PhoneNumber> calledE164Numbers;
    @Nullable private final Instant startTime;
    @Nullable private final Duration duration;
    @Nonnull private final List<RequestAction> actions;
    @Nonnull private final List<CallFeature> features;
    @Nonnull private final List<Participant> participants;

    private Call(@Nonnull final Builder builder) {
        this.callId = builder.callId;
        this.site = builder.site;
        this.profileId = builder.profileId;
        this.interestId = builder.interestId;
        this.changed = builder.changed;
        this.state = builder.state;
        this.direction = builder.direction;
        this.callerNumber = builder.callerNumber;
        this.callerName = builder.callerName;
        this.callerE164Numbers = Collections.unmodifiableList(builder.callerE164Numbers);
        this.calledNumber = builder.calledNumber;
        this.calledName = builder.calledName;
        this.calledDestination = builder.calledDestination;
        this.calledE164Numbers = Collections.unmodifiableList(builder.calledE164Numbers);
        this.startTime = builder.startTime;
        this.duration = builder.duration;
        this.actions = Collections.unmodifiableList(builder.actions);
        this.features = Collections.unmodifiableList(builder.features);
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
    public Optional<Changed> getChanged() {
        return Optional.ofNullable(changed);
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
    public List<PhoneNumber> getCallerE164Numbers() {
        return callerE164Numbers;
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
    public Optional<PhoneNumber> getCalledDestination() {
        return Optional.ofNullable(calledDestination);
    }

    @Nonnull
    public List<PhoneNumber> getCalledE164Numbers() {
        return calledE164Numbers;
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
    public List<RequestAction> getActions() {
        return actions;
    }

    @Nonnull
    public List<CallFeature> getFeatures() {
        return features;
    }

    @Nonnull
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Determines the id, if any, of the active handset. Note, if two or more handsets are active, one of them is
     * selected in a nondeterministic manner.
     * 
     * @return the id of an active handset
     */
    @Nonnull
    public Optional<FeatureId> getActiveHandset() {
        return getFeatures().stream()
                .filter(feature -> {
                    final Optional<FeatureType> type = feature.getType();
                    final Optional<Boolean> enabled = feature.isEnabled();
                    return type.isPresent() && type.get() == FeatureType.HANDSET &&
                            enabled.isPresent() && enabled.get();
                })
                .findAny()
                .flatMap(Feature::getId);
    }

    /**
     * Determines the id, if any, of the active speaker. Note, if two or more speakers are active, one of them is
     * selected in a nondeterministic manner.
     * 
     * @return the id of an active speaker
     */
    @Nonnull
    public Optional<FeatureId> getActiveSpeakerChannel() {
        return getFeatures().stream()
                .filter(feature -> {
                    final Optional<FeatureType> type = feature.getType();
                    final Optional<Boolean> enabled = feature.isEnabled();
                    return type.isPresent() && type.get() == FeatureType.SPEAKER_CHANNEL &&
                            enabled.isPresent() && enabled.get();
                })
                .findAny()
                .flatMap(Feature::getId);
    }

    /**
     * Indicates if the call is public or private.
     *
     * @return {@code empty} if there is no privacy indication, otherwise {@code true} or {@code false}.
     */
    @Nonnull
    public Optional<Boolean> isPrivate() {
        return getFeatures().stream()
                .filter(feature -> {
                    final Optional<FeatureType> type = feature.getType();
                    final Optional<Boolean> enabled = feature.isEnabled();
                    return type.isPresent() && type.get() == FeatureType.PRIVACY &&
                            enabled.isPresent();
                })
                .findAny()
                .flatMap(CallFeature::isEnabled);
    }

    /**
     * Indicates if the call is public or private.
     *
     * @return {@code empty} if there is no privacy indication, otherwise {@code true} or {@code false}.
     */
    @Nonnull
    public Optional<Boolean> isPublic() {
        return isPrivate().map(isPrivate -> !isPrivate);
    }

    public boolean isParticipating() {
        return state != null && direction != null && state.isParticipating(direction);
    }

    public static final class Builder {
        @Nullable private CallId callId;
        @Nullable private Site site;
        @Nullable private ProfileId profileId;
        @Nullable private InterestId interestId;
        @Nullable private Changed changed;
        @Nullable private CallState state;
        @Nullable private CallDirection direction;
        @Nullable private PhoneNumber callerNumber;
        @Nullable private String callerName;
        @Nonnull private final List<PhoneNumber> callerE164Numbers = new ArrayList<>();
        @Nullable private PhoneNumber calledNumber;
        @Nullable private String calledName;
        @Nullable private PhoneNumber calledDestination;
        @Nonnull private final List<PhoneNumber> calledE164Numbers = new ArrayList<>();
        @Nullable private Instant startTime;
        @Nullable private Duration duration;
        @Nonnull private final List<RequestAction> actions = new ArrayList<>();
        @Nonnull private final List<CallFeature> features = new ArrayList<>();
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

            return new Call(this);
        }

        @Nonnull
        public Call build(@Nonnull final List<String> errors) {
            if (callId == null) {
                errors.add("Invalid call status; missing call id is mandatory");
            }
            if (site == null) {
                errors.add("Invalid call status; missing call site is mandatory");
            }
            if (profileId == null) {
                errors.add("Invalid call status; missing profile id is mandatory");
            }
            if (interestId == null) {
                errors.add("Invalid call status; missing interest id is mandatory");
            }
            if (state == null) {
                errors.add("Invalid call status; missing call state is mandatory");
            }
            if (direction == null) {
                errors.add("Invalid call status; missing call direction is mandatory");
            }
            if (startTime == null) {
                errors.add("Invalid call status; missing call start time is mandatory");
            }
            if (duration == null) {
                errors.add("Invalid call status; missing call duration is mandatory");
            }
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
        public Builder setChanged(@Nonnull final Changed changed) {
            this.changed = changed;
            return this;
        }

        @Nonnull
        public Builder setDirection(@Nonnull final CallDirection direction) {
            this.direction = direction;
            return this;
        }

        @Nonnull
        public Builder setStartTime(@Nonnull final Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        @Nonnull
        public Builder setCallerNumber(final PhoneNumber callerNumber) {
            this.callerNumber = callerNumber;
            return this;
        }

        @Nonnull
        public Builder setCallerName(final String callerName) {
            this.callerName = callerName;
            return this;
        }

        @Nonnull
        public Builder addCallerE164Number(final PhoneNumber callerE164Number) {
            this.callerE164Numbers.add(callerE164Number);
            return this;
        }

        @Nonnull
        public Builder addCallerE164Numbers(final List<PhoneNumber> callerE164Numbers) {
            this.callerE164Numbers.addAll(callerE164Numbers);
            return this;
        }

        @Nonnull
        public Builder setCalledNumber(final PhoneNumber calledNumber) {
            this.calledNumber = calledNumber;
            return this;
        }

        @Nonnull
        public Builder setCalledDestination(final PhoneNumber calledDestination) {
            this.calledDestination = calledDestination;
            return this;
        }

        @Nonnull
        public Builder setCalledName(final String calledName) {
            this.calledName = calledName;
            return this;
        }

        @Nonnull
        public Builder addCalledE164Number(final PhoneNumber calledE164Number) {
            this.calledE164Numbers.add(calledE164Number);
            return this;
        }

        @Nonnull
        public Builder addCalledE164Numbers(final List<PhoneNumber> calledE164Numbers) {
            this.calledE164Numbers.addAll(calledE164Numbers);
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
        public Builder addFeature(@Nonnull final CallFeature feature) {
            features.add(feature);
            return this;
        }

        @Nonnull
        public Builder addParticipant(@Nonnull final Participant participant) {
            participants.add(participant);
            return this;
        }

    }
}
