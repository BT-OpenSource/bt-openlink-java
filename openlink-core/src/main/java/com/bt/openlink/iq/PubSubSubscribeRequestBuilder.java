package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PubSubNodeId;

public abstract class PubSubSubscribeRequestBuilder<B extends PubSubSubscribeRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private PubSubNodeId pubSubNodeId;
    @Nullable private J jid;

    protected PubSubSubscribeRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
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

    @Override
    protected void validate() {
        super.validate();
        if (pubSubNodeId == null) {
            throw new IllegalStateException("The stanza 'pubSubNodeId'/'interestId' has not been set");
        }
        if (jid == null) {
            throw new IllegalStateException("The stanza 'jid' has not been set");
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
            errors.add("Invalid pub-sub subscribe request stanza; missing node id/interest id");
        }
        if (jid == null) {
            errors.add("Invalid pub-sub subscribe request stanza; missing jid");
        }
    }
}
