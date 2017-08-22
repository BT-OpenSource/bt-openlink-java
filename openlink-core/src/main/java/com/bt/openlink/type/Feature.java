package com.bt.openlink.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Feature {
    @Nullable private final FeatureId featureId;
    @Nullable private final FeatureType featureType;
    @Nullable private final String label;
    @Nonnull private final List<String> parseErrors;

    private Feature(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.featureId = builder.featureId;
        this.featureType = builder.featureType;
        this.label = builder.label;
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
    public Optional<FeatureId> getId() {
        return Optional.ofNullable(featureId);
    }

    @Nonnull
    public Optional<FeatureType> getType() {
        return Optional.ofNullable(featureType);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    public static final class Builder {

        @Nullable private FeatureId featureId = null;
        @Nullable private FeatureType featureType = null;
        @Nullable private String label = null;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Feature build() {
            if (featureId == null) {
                throw new IllegalStateException("The feature id has not been set");
            }
            if (featureType == null) {
                throw new IllegalStateException("The feature type has not been set");
            }
            if (label == null) {
                throw new IllegalStateException("The feature label has not been set");
            }
            return new Feature(this, null);
        }

        @Nonnull
        private Feature build(final List<String> parseErrors) {
            return new Feature(this, parseErrors);
        }

        public Builder setId(@Nonnull final FeatureId featureId) {
            this.featureId = featureId;
            return this;
        }

        public Builder setType(@Nonnull final FeatureType featureType) {
            this.featureType = featureType;
            return this;
        }

        public Builder setLabel(@Nonnull final String featureLabel) {
            this.label = featureLabel;
            return this;
        }
    }

}
