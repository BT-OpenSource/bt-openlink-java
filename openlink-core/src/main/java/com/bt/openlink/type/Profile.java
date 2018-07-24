package com.bt.openlink.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Profile implements Serializable {
    private static final long serialVersionUID = -2429079888059635951L;
    @Nullable private final ProfileId profileId;
    @Nullable private final Boolean isDefault;
    @Nullable private final DeviceType deviceType;
    @Nullable private final DeviceId deviceId;
    @Nullable private final String label;
    @Nullable private final Boolean online;
    @Nullable private final Site site;
    @Nonnull private final List<RequestAction> actions;

    private Profile(@Nonnull final Builder builder) {
        this.profileId = builder.profileId;
        this.isDefault = builder.isDefault;
        this.deviceType = builder.deviceType;
        this.deviceId = builder.deviceId;
        this.label = builder.label;
        this.online = builder.online;
        this.site = builder.site;
        this.actions = builder.actions;
    }

    @Nonnull
    public Optional<ProfileId> getId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<Boolean> isDefaultProfile() {
        return Optional.ofNullable(isDefault);
    }

    /**
     * Returns the type of the device for this profile. This is stored in the "device" attribute of the XML.
     * For example;
     * {@code ITS.Netrix}
     * @return the type of device associated with the profile
     */
    @Nonnull
    public Optional<DeviceType> getDeviceType() {
        return Optional.ofNullable(deviceType);
    }

    /**
     * Returns the id of the device for this profile. This is stored in the "devicenum" attribute of the XML.
     * For example;
     * {@code 779}
     * @return the id of the device associated with the profile
     */
    @Nonnull
    public Optional<DeviceId> getDeviceId() {
        return Optional.ofNullable(deviceId);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<Boolean> isOnline() {
        return Optional.ofNullable(online);
    }

    @Nonnull
    public Optional<Site> getSite() {
        return Optional.ofNullable(site);
    }

    @Nonnull
    public List<RequestAction> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return "Profile[" +
                "profileId=" + profileId +
                ", isDefault=" + isDefault +
                ", deviceType='" + deviceType + '\'' +
                ", label='" + label + '\'' +
                ", online=" + online +
                ", site=" + site +
                ']';
    }

    public static final class Builder {

        @Nullable private ProfileId profileId = null;
        @Nullable private Site site = null;
        @Nullable private Boolean isDefault;
        @Nullable private DeviceType deviceType;
        @Nullable private DeviceId deviceId;
        @Nullable private String label;
        @Nullable private Boolean online;
        @Nonnull private final List<RequestAction> actions = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Profile build() {
            if (profileId == null) {
                throw new IllegalStateException("The profile id has not been set");
            }
            if (site == null) {
                throw new IllegalStateException("The site has not been set");
            }
            if (isDefault == null) {
                throw new IllegalStateException("The default indicator has not been set");
            }
            if (label == null) {
                throw new IllegalStateException("The label has not been set");
            }
            if (online == null) {
                throw new IllegalStateException("The online indicator has not been set");
            }
            return new Profile(this);
        }

        @Nonnull
        public Profile build(@Nonnull final List<String> errors) {
            if (profileId == null) {
                errors.add("Invalid profile; missing profile id is mandatory");
            }
            if (site == null) {
                errors.add("Invalid profile; missing site is mandatory");
            }
            if (isDefault == null) {
                errors.add("Invalid profile; missing default indicator is mandatory");
            }
            if (label == null) {
                errors.add("Invalid profile; missing label is mandatory");
            }
            if (online == null) {
                errors.add("Invalid profile; missing online indicator is mandatory");
            }
            return new Profile(this);
        }

        public Builder setId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder setDefault(final boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        /**
         * Sets the type of device for this profile. This is stored in the "device" attribute of the XML. For example;
         * {@code ITS.Netrix}
         * @param deviceType the type of device for the profile
         * @return the builder
         */
        public Builder setDeviceType(@Nonnull final DeviceType deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        /**
         * Sets the id of the device for this profile. This is stored in the "devicenum" attribute of the XML.
         * For example;
         * {@code 779}
         * @param deviceId the id of the device for the profile
         * @return the builder
         */
        public Builder setDeviceId(@Nonnull final DeviceId deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setLabel(@Nonnull final String label) {
            this.label = label;
            return this;
        }

        public Builder setOnline(final boolean online) {
            this.online = online;
            return this;
        }

        public Builder setSite(@Nonnull Site site) {
            this.site = site;
            return this;
        }

        public Builder addAction(@Nonnull RequestAction action) {
            actions.add(action);
            return this;
        }
    }
}
