package com.bt.openlink;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class StanzaBuilder<B extends StanzaBuilder, J> {

    @Nullable private J to;
    @Nullable private J from;
    @Nullable private String id;

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setId(@Nullable String id) {
        this.id = id;
        return (B) this;
    }

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

    @Nonnull
    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    @Nonnull
    public Optional<J> getTo() {
        return Optional.ofNullable(to);
    }

    @Nonnull
    public Optional<J> getFrom() {
        return Optional.ofNullable(from);
    }

}
