package com.bt.openlink;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class StanzaBuilder<B extends StanzaBuilder, J> {

    @Nullable private J to;
    @Nullable private J from;
    @Nullable private String id;

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setTo(@Nullable J to) {
        this.to = to;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setFrom(@Nullable J from) {
        this.from = from;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setID(@Nullable String id) {
        this.id = id;
        return (B) this;
    }

    @Nonnull
    public B setStanzaId(@Nullable String stanzaId) {
        return setID(stanzaId);
    }

    @Nonnull
    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    @Nonnull
    public Optional<String> getStanzaId() {
        return getId();
    }

    @Nonnull
    public Optional<J> getTo() {
        return Optional.ofNullable(to);
    }

    @Nonnull
    public Optional<J> getFrom() {
        return Optional.ofNullable(from);
    }

    protected void validate() {
        if (to == null) {
            throw new IllegalStateException("The stanza 'to' has not been set");
        }
    }

    protected void validate(final List<String> errors) {
        if (to == null) {
            errors.add("Invalid stanza; missing 'to' attribute is mandatory");
        }
    }
}
