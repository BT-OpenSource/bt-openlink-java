package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;
import com.bt.openlink.type.SubscriptionState;

public abstract class PubSubSubscriptionRequestResultBuilder<B extends PubSubSubscriptionRequestResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private PubSubNodeId pubSubNodeId;
    @Nullable private J jid;
    @Nullable private SubscriptionState subscriptionState;

    protected PubSubSubscriptionRequestResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setPubSubNodeId(@Nonnull final PubSubNodeId pubSubNodeId) {
        this.pubSubNodeId = pubSubNodeId;
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setInterestId(@Nonnull final InterestId interestId) {
        return setPubSubNodeId(interestId.toPubSubNodeId());
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(pubSubNodeId == null ? null : pubSubNodeId.toInterestId());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setJID(@Nonnull final J jid) {
        this.jid = jid;
        return (B) this;
    }

    @Nonnull
    public Optional<J> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B setSubscriptionState(@Nonnull final SubscriptionState subscriptionState) {
        this.subscriptionState = subscriptionState;
        return (B) this;
    }

    @Nonnull
    public Optional<SubscriptionState> getSubscriptionState() {
        return Optional.ofNullable(subscriptionState);
    }

    @Override
    protected void validate() {
        super.validate();
        if (pubSubNodeId == null) {
            throw new IllegalStateException("The stanza 'pubSubNodeId'/'interestId' has not been set");
        }
        if (jid == null) {
            throw new IllegalStateException("The stanza 'jid' has not been set");
        }
        if (subscriptionState == null) {
            throw new IllegalStateException("The stanza 'subscriptionState' has not been set");
        }
        if (subscriptionState != SubscriptionState.SUBSCRIBED && subscriptionState != SubscriptionState.NONE) {
            throw new IllegalStateException("The only supported requests states are 'subscribed' and 'none'");
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
        if (pubSubNodeId == null) {
            errors.add("Invalid pub-sub subscription request stanza; missing node id/interest id");
        }
        if (jid == null) {
            errors.add("Invalid pub-sub subscription request stanza; missing jid");
        }
        if (subscriptionState == null) {
            errors.add("Invalid pub-sub subscription request stanza; missing subscriptionState");
        } else if (subscriptionState != SubscriptionState.SUBSCRIBED && subscriptionState != SubscriptionState.NONE) {
            errors.add("Invalid pub-sub subscription request stanza; subscriptionState should only be subscribed or none");
        }
    }
}
