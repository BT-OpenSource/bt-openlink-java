package com.bt.openlink.message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.StanzaBuilder;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

public abstract class PubSubMessageBuilder<B extends PubSubMessageBuilder, J> extends StanzaBuilder<B, J> {

    @Nullable private Instant delay;
    @Nullable private PubSubNodeId pubSubNodeId;
    @Nullable private ItemId itemId;

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setDelay(@Nonnull Instant delay) {
        this.delay = delay;
        return (B) this;
    }

    @Nonnull
    public Optional<Instant> getDelay() {
        return Optional.ofNullable(delay);
    }

    @Nonnull
    public B setPubSubNodeId(@Nonnull final InterestId interestId) {
        return setPubSubNodeId(interestId.toPubSubNodeId());
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setPubSubNodeId(@Nonnull final PubSubNodeId pubSubNodeId) {
        this.pubSubNodeId = pubSubNodeId;
        return (B) this;
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setItemId(@Nonnull final ItemId itemId) {
        this.itemId = itemId;
        return (B) this;
    }

    @Nonnull
    public Optional<ItemId> getItemId() {
        return Optional.ofNullable(itemId);
    }

    protected void validate() {
        if (!getTo().isPresent()) {
            throw new IllegalStateException("The stanza 'to' has not been set");
        }
        if (!getFrom().isPresent()) {
            throw new IllegalStateException("The stanza 'from' has not been set");
        }
        // Note; not necessary to validate id/type as these can be automatically set
        if (pubSubNodeId == null) {
            throw new IllegalStateException("The stanza 'pubSubNodeId' has not been set");
        }
        if (itemId == null) {
            itemId = ItemId.random();
        }
    }

    public void validate(final List<String> errors) {
        if (!getTo().isPresent()) {
            errors.add("Invalid stanza; missing 'to' attribute is mandatory");
        }
        if (!getFrom().isPresent()) {
            errors.add("Invalid stanza; missing 'from' attribute is mandatory");
        }
        if (!getId().isPresent()) {
            errors.add("Invalid stanza; missing 'id' attribute is mandatory");
        }
        if (pubSubNodeId == null) {
            errors.add("Invalid callstatus message stanza; the 'pubSubNodeId' has not been set");
        }
    }

}
