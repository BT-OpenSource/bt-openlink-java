package com.bt.openlink.IQ;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.StanzaBuilder;

@SuppressWarnings("unchecked")
public abstract class IQBuilder<B extends IQBuilder, J, T> extends StanzaBuilder<B, J> {

    @Nullable private T iqType = getExpectedIQType();

    @Nonnull
    public abstract T getExpectedIQType();

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setIQType(@Nullable final T iqType) {
        this.iqType = iqType;
        return (B) this;
    }

    @Nonnull
    public Optional<T> getIqType() {
        return Optional.ofNullable(iqType);
    }

    protected void validate() {
        super.validate();
    }

    public void validate(final List<String> errors) {
        super.validate(errors);
        if (!getFrom().isPresent()) {
            errors.add("Invalid stanza; missing 'from' attribute is mandatory");
        }
        if (!getId().isPresent()) {
            errors.add(0, "Invalid stanza; missing 'id' attribute is mandatory");
        }
        if (getExpectedIQType() != iqType) {
            errors.add("Invalid stanza; missing or incorrect 'type' attribute");
        }
    }

}
