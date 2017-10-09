package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ProfileId;

@SuppressWarnings("unchecked")
public abstract class GetFeaturesResultBuilder<B extends GetFeaturesResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private ProfileId profileId;
    @Nonnull private List<Feature> features = new ArrayList<>();

    protected GetFeaturesResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    public B setProfileId(@Nonnull ProfileId profileId) {
        this.profileId = profileId;
        return (B) this;
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addFeature(@Nonnull final Feature feature) {
        this.features.add(feature);
        return (B) this;
    }

    @Nonnull
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    protected void validate() {
        super.validate();
        if (profileId == null) {
            throw new IllegalStateException("The get-features result profile has not been set");
        }
        validateUniqueness(feature -> {
            throw new IllegalStateException("Each feature id must be unique - " + feature + " appears more than once");
        });
    }

    private void validateUniqueness(final Consumer<FeatureId> errorConsumer) {
        for (int i = 0; i < features.size(); i++) {
            final Optional<FeatureId> featureIdOptional = features.get(i).getId();
            for (int j = i + 1; j < features.size(); j++) {
                if (featureIdOptional.isPresent() && featureIdOptional.equals(features.get(j).getId())) {
                    errorConsumer.accept(featureIdOptional.get());
                }
            }
        }
    }

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
        if(profileId==null) {
            errors.add("Invalid get-features result stanza; missing profile");
        }
        validateUniqueness(profileId -> errors.add("Invalid get-features result stanza; each feature id must be unique - " + profileId + " appears more than once"));
    }

}
