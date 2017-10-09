package com.bt.openlink.iq;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GetProfilesRequestBuilder<B extends IQBuilder, J, T> extends IQBuilder<B, J, T> {

    @Nullable protected J jid;

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
        if (jid == null) {
            errors.add("Invalid get-profiles request stanza; missing or invalid 'jid'");
        }
    }
}
