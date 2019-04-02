package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.MakeCallFeature;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.RequestActionValue;

public abstract class RequestActionRequestBuilder<B extends RequestActionRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private InterestId interestId;
    @Nullable private RequestAction requestAction;
    @Nullable private CallId callId;
    @Nullable private RequestActionValue value2;
    @Nullable private RequestActionValue value1;
    @Nonnull private final List<MakeCallFeature> features = new ArrayList<>();

    protected RequestActionRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setInterestId(@Nonnull final InterestId interestId) {
        this.interestId = interestId;
        return (B) this;
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setAction(@Nonnull final RequestAction requestAction) {
        this.requestAction = requestAction;
        return (B) this;
    }

    @Nonnull
    public Optional<RequestAction> getAction() {
        return Optional.ofNullable(requestAction);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setCallId(@Nonnull final CallId callId) {
        this.callId = callId;
        return (B) this;
    }

    @Nonnull
    public Optional<CallId> getCallId() {
        return Optional.ofNullable(callId);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue1(@Nonnull final RequestActionValue value1) {
        this.value1 = value1;
        return (B) this;
    }

    @Nonnull
    public Optional<RequestActionValue> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setValue2(@Nonnull final RequestActionValue value2) {
        this.value2 = value2;
        return (B) this;
    }

    @Nonnull
    public Optional<RequestActionValue> getValue2() {
        return Optional.ofNullable(value2);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addFeatures(@Nonnull final List<MakeCallFeature> features) {
        this.features.addAll(features);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B addFeature(@Nonnull final MakeCallFeature feature) {
        this.features.add(feature);
        return (B) this;
    }

    @Nonnull
    public List<MakeCallFeature> getFeatures() {
        return features;
    }

    @Override
    protected void validate() {
        super.validate();
        if (interestId == null) {
            throw new IllegalStateException("The request-action 'interestId' has not been set");
        }
        if (requestAction == null) {
            throw new IllegalStateException("The request-action 'action' has not been set");
        }
        if (callId == null) {
            throw new IllegalStateException("The request-action 'callId' has not been set");
        }
        if (value1 == null && value2 != null) {
            throw new IllegalStateException("The request-action 'value2' has been set without setting 'value1'");
        }
        if (requestAction.getMinValueCount() > 0 && value1 == null) {
            throw new IllegalStateException(String.format("The requestAction '%s' requires 'value1' to be set", requestAction.getId()));
        }
        if (requestAction.getMinValueCount() > 1 && value2 == null) {
            throw new IllegalStateException(String.format("The requestAction '%s' requires 'value2' to be set", requestAction.getId()));
        }
        if (requestAction.getMaxValueCount() < 1 && value1 != null) {
            throw new IllegalStateException(String.format("The requestAction '%s' does not require 'value1' to be set", requestAction.getId()));
        }
        if (requestAction.getMaxValueCount() < 2 && value2 != null) {
            throw new IllegalStateException(String.format("The requestAction '%s' does not require 'value2' to be set", requestAction.getId()));
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
        if (interestId == null) {
            errors.add("Invalid request-action stanza; missing 'interestId'");
        }
        if (requestAction == null) {
            errors.add("Invalid request-action stanza; missing or invalid 'requestAction'");
        } else {
            checkValuesArePResent(requestAction, errors);
        }
        if (callId == null) {
            errors.add("Invalid request-action stanza; missing 'callId'");
        }
        if (value1 == null && value2 != null) {
            errors.add("Invalid request-action stanza; value2 cannot be set without value1");
        }
    }

    private void checkValuesArePResent(@Nonnull final RequestAction requestActionToCheck, final List<String> errors) {
        if (requestActionToCheck.getMinValueCount() > 0 && value1 == null) {
            errors.add(String.format("Invalid request-action stanza; the action '%s' requires value1 to be set", requestActionToCheck.getId()));
        }
        if (requestActionToCheck.getMinValueCount() > 1 && value2 == null) {
            errors.add(String.format("Invalid request-action stanza; the action '%s' requires value2 to be set", requestActionToCheck.getId()));
        }
        if (requestActionToCheck.getMaxValueCount() < 1 && value1 != null) {
            errors.add(String.format("Invalid request-action stanza; the action '%s' does not require value1 to be set", requestActionToCheck.getId()));
        }
        if (requestActionToCheck.getMaxValueCount() < 2 && value2 != null) {
            errors.add(String.format("Invalid request-action stanza; the action '%s' does not require value2 to be set", requestActionToCheck.getId()));
        }
    }
}
