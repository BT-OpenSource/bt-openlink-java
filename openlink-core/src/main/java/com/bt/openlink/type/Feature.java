package com.bt.openlink.type;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Feature {
    @Nullable private final FeatureId featureId;
    @Nullable private final FeatureType featureType;
    @Nullable private final String label;

    Feature(@Nonnull final AbstractFeatureBuilder builder) {
        this.featureId = builder.featureId;
        this.featureType = builder.featureType;
        this.label = builder.label;
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

    public abstract static class AbstractFeatureBuilder<B extends AbstractFeatureBuilder> {

        @Nullable private FeatureId featureId = null;
        @Nullable private FeatureType featureType = null;
        @Nullable private String label = null;

        protected void validate() {
            if (featureId == null) {
                throw new IllegalStateException("The feature id has not been set");
            }
            if (featureType == null) {
                throw new IllegalStateException("The feature type has not been set");
            }
            if (label == null) {
                throw new IllegalStateException("The feature label has not been set");
            }
        }

        public void validate(final List<String> errors) {
            if (featureId == null) {
                errors.add("Invalid feature; missing feature id is mandatory");
            }
            if (featureType == null) {
                errors.add("Invalid feature; missing feature type is mandatory");
            }
            if (label == null) {
                errors.add("Invalid feature; missing feature label is mandatory");
            }
        }

        @SuppressWarnings("unchecked")
        public B setId(@Nonnull final FeatureId featureId) {
            this.featureId = featureId;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setType(@Nonnull final FeatureType featureType) {
            this.featureType = featureType;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setLabel(@Nonnull final String featureLabel) {
            this.label = featureLabel;
            return (B) this;
        }

        @Nullable
        public String getLabel() {
            return label;
        }

    }

    public static final class Builder extends AbstractFeatureBuilder<Builder> {

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Feature build() {
            super.validate();
            return new Feature(this);
        }

        @Nonnull
        public Feature build(final List<String> errors) {
            super.validate(errors);
            return new Feature(this);
        }

    }

}
