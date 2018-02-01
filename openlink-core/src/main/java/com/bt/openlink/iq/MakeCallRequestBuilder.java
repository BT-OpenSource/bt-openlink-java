package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.MakeCallFeature;
import com.bt.openlink.type.OriginatorReference;
import com.bt.openlink.type.PhoneNumber;

public abstract class MakeCallRequestBuilder<B extends MakeCallRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private J jid;
    @Nullable private InterestId interestId;
    @Nullable private PhoneNumber destination;
    @Nonnull private final List<MakeCallFeature> features = new ArrayList<>();
    @Nonnull private List<OriginatorReference> originatorReferences = new ArrayList<>();

    protected MakeCallRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setJID(@Nonnull final J jid) {
        this.jid = jid;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setInterestId(@Nonnull final InterestId interestId) {
        this.interestId = interestId;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setDestination(@Nonnull final PhoneNumber destination) {
        this.destination = destination;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addFeature(@Nonnull final MakeCallFeature feature) {
        this.features.add(feature);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addOriginatorReference(@Nonnull final String key, @Nonnull final String value) {
        this.originatorReferences.add(new OriginatorReference(key, value));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addOriginatorReference(@Nonnull final OriginatorReference originatorReference) {
        this.originatorReferences.add(originatorReference);
        return (B) this;
    }

    @Nonnull
    public Optional<J> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<PhoneNumber> getDestination() {
        return Optional.ofNullable(destination);
    }

    @Nonnull
    public List<MakeCallFeature> getFeatures() {
        return features;
    }

    @Nonnull
    public List<OriginatorReference> getOriginatorReferences() {
        return originatorReferences;
    }

    @Override
    protected void validate() {
        super.validate();
        if (jid == null) {
            throw new IllegalStateException("The make-call request 'jid' has not been set");
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
        if (jid == null) {
            errors.add("Invalid make-call request stanza; missing or invalid 'jid'");
        }

    }
}
