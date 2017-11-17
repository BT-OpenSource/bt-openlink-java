package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.StanzaBuilder;

@SuppressWarnings("unchecked")
public abstract class IQBuilder<B extends IQBuilder, J, T extends Enum<T>> extends StanzaBuilder<B, J> {

    @Nullable private T iqType;

    protected IQBuilder(final Class<T> typeClass) {
        iqType = Enum.valueOf(typeClass, getExpectedIQType());
    }

    @Nonnull
    public abstract String getExpectedIQType();

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
        if (!getTo().isPresent()) {
            throw new IllegalStateException("The stanza 'to' has not been set");
        }
        if (!getFrom().isPresent()) {
            throw new IllegalStateException("The stanza 'from' has not been set");
        }
        // Note; not necessary to validate id/type as these can be automatically set
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
        if (iqType == null || !iqType.name().equals(getExpectedIQType())) {
            errors.add("Invalid stanza; missing or incorrect 'type' attribute");
        }
    }

}
