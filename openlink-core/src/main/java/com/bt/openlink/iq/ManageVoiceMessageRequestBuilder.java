package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;
import com.bt.openlink.type.ProfileId;

public abstract class ManageVoiceMessageRequestBuilder<B extends ManageVoiceMessageRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private ProfileId profileId;
    @Nullable private ManageVoiceMessageAction action;
    @Nullable private String label;
    @Nonnull private final List<FeatureId> features = new ArrayList<>();

    protected ManageVoiceMessageRequestBuilder(Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Override
    protected void validate() {
        super.validate();

        if (profileId == null) {
            throw new IllegalStateException("The manage-voice-message request 'profileId' has not been set");
        }

        if (action == null) {
            throw new IllegalStateException("Invalid manage-voice-message stanza; missing or invalid 'action'");
        } else {
            if (features.size() < action.getMinFeaturesRequired()) {
                final String error = (action.getMinFeaturesRequired() == 1)
                        ? String.format("A single feature must be supplied with the '%s' action", action)
                        : String.format("Invalid manage-voice-message stanza; %d feature(s) must be supplied with the '%s' action", action.getMinFeaturesRequired(), action);
                throw new IllegalStateException(error);
            }

            if(label == null && action.requiresLabel()) {
                throw new IllegalStateException(String.format("Invalid manage-voice-message stanza; The '%s' action requires a label", action));
            }
        }

        if (features.size() > 1 && !action.allowsMultipleFeatures()) {
            throw new IllegalStateException(String.format("Invalid manage-voice-message stanza; Only one feature can be supplied with the '%s' action", action));
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

        if (profileId == null) {
            errors.add("Invalid manage-voice-message request stanza; missing 'profile'");
        }

        if (action == null) {
            errors.add("Invalid manage-voice-message request stanza; missing 'action'");
        } else {
            if (features.size() < action.getMinFeaturesRequired()) {
                final String error = (action.getMinFeaturesRequired() == 1)
                        ? String.format("A single feature must be supplied with the '%s' action", action)
                        : String.format("Invalid manage-voice-message stanza; %d feature(s) must be supplied with the '%s' action", action.getMinFeaturesRequired(), action);
                errors.add(error);
            }

            if(label == null && action.requiresLabel()) {
                errors.add(String.format("Invalid manage-voice-message stanza; The '%s' action requires a label", action));
            }

            if (features.size() > 1 && !action.allowsMultipleFeatures()) {
                errors.add((String.format("Invalid manage-voice-message stanza; Only one feature can be supplied with the '%s' action", action)));
            }
        }
    }

    @Nonnull
    public List<FeatureId> getFeatures() {
        return features;
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<ManageVoiceMessageAction> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addFeature(@Nonnull final FeatureId feature) {
        this.features.add(feature);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setProfile(@Nonnull final ProfileId profileId) {
        this.profileId = profileId;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setLabel(@Nonnull final String label) {
        this.label = label;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setAction(@Nonnull final ManageVoiceMessageAction action) {
        this.action = action;
        return (B) this;
    }

}