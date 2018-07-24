package com.bt.openlink.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DeviceStatus implements Serializable {
    private static final long serialVersionUID = -4817479046901684980L;
    @Nullable private final Boolean online;
    @Nullable private final ProfileId profileId;
    @Nullable private final DeviceId deviceId;
    @Nonnull private List<VoiceMessageFeature> features;

    private DeviceStatus(@Nonnull final Builder builder) {
        this.online = builder.online;
        this.profileId = builder.profileId;
        this.deviceId = builder.deviceId;
        this.features = Collections.unmodifiableList(builder.getFeatures());
    }

    @Nonnull
    public Optional<Boolean> isOnline() {
        return Optional.ofNullable(online);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<DeviceId> getDeviceId() {
        return Optional.ofNullable(deviceId);
    }

    @Nonnull
    public List<VoiceMessageFeature> getFeatures() {
        return features;
    }

    public static final class Builder {

        @Nullable private Boolean online;
        @Nullable private ProfileId profileId;
        @Nullable private DeviceId deviceId;
        @Nonnull private List<VoiceMessageFeature> features = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public DeviceStatus build() {
            if (profileId == null) {
                throw new IllegalStateException("The device profile has not been set");
            }
            return new DeviceStatus(this);
        }

        @Nonnull
        public DeviceStatus build(final List<String> errors) {
            if (profileId == null) {
                errors.add("Invalid device status; missing profile is mandatory");
            }
            return new DeviceStatus(this);
        }

        @Nonnull
        public Builder addFeature(@Nonnull final VoiceMessageFeature feature) {
            features.add(feature);
            return this;
        }

        @Nonnull
        public List<VoiceMessageFeature> getFeatures() {
            return features;
        }

        public Builder setOnline(final boolean online) {
            this.online = online;
            return this;
        }

        public Builder setProfileId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder setDeviceId(@Nonnull final DeviceId deviceId) {
            this.deviceId = deviceId;
            return this;
        }

    }

}
