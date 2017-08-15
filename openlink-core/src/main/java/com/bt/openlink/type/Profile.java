package com.bt.openlink.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class Profile {
    @Nullable private final ProfileId profileId;
    @Nonnull private final List<String> parseErrors;
    @Nullable private final Site site;

    private Profile(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.profileId = builder.profileId;
        this.site = builder.site;
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
    public Optional<ProfileId> profileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<Site> getSite() {
        return Optional.ofNullable(site);
    }

    public static final class Builder {

        @Nullable private ProfileId profileId = null;
        @Nullable private Site site = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Profile build() {
            if(profileId == null ) {
                throw new IllegalStateException("The profileId has not been set");
            }
            if(site == null ) {
                throw new IllegalStateException("The site has not been set");
            }
            return new Profile(this, null);
        }

        @Nonnull
        public Profile build(final List<String> parseErrors) {
            return new Profile(this, parseErrors);
        }

        public Builder setProfileId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder setSite(@Nonnull Site site) {
            this.site = site;
            return this;
        }
    }
}
