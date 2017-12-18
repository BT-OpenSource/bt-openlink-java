package com.bt.openlink.message;

import java.util.List;

import com.bt.openlink.StanzaBuilder;

public abstract class MessageBuilder<B extends MessageBuilder, J> extends StanzaBuilder<B, J> {

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
    }

}
