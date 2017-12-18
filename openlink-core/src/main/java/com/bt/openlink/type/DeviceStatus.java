package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public final class DeviceStatus {
    @Nullable private final Boolean online;
    @Nullable private final ProfileId profileId;

    private DeviceStatus(@Nonnull final Builder builder) {
        this.online = builder.online;
        this.profileId = builder.profileId;
    }

    @Nonnull
    public Optional<Boolean> isOnline() {
        return Optional.ofNullable(online);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    public static final class Builder {

        @Nullable private Boolean online;
        @Nullable private ProfileId profileId;

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

        public Builder setOnline(final boolean online) {
            this.online = online;
            return this;
        }

        public Builder setProfileId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

    }

}
