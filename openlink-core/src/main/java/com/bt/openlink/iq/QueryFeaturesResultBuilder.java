package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.bt.openlink.type.ActiveFeature;

@SuppressWarnings("unchecked")
public abstract class QueryFeaturesResultBuilder<B extends QueryFeaturesResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nonnull private List<ActiveFeature> features = new ArrayList<>();

    protected QueryFeaturesResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addFeature(@Nonnull final ActiveFeature feature) {
        this.features.add(feature);
        return (B) this;
    }

    @Nonnull
    public List<ActiveFeature> getFeatures() {
        return features;
    }

    @Override
    protected void validate() {
        super.validate();
    }

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
    }

}
