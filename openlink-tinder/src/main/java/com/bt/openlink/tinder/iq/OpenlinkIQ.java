package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

abstract class OpenlinkIQ extends IQ {

    @Nonnull private List<String> parseErrors;

    OpenlinkIQ(@Nonnull final IQBuilder builder, @Nullable final List<String> parseErrors) {
        setTo(builder.to);
        setFrom(builder.from);
        setType(builder.type);
        if (builder.id != null) {
            setID(builder.id);
        }
        if(parseErrors == null ) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = new ArrayList<>(parseErrors);
            if (builder.id == null) {
                this.parseErrors.add(0, "Invalid stanza; missing 'id' attribute is mandatory");
            }
            if (builder.from == null) {
                this.parseErrors.add(0, "Invalid stanza; missing 'from' attribute is mandatory");
            }
            if (builder.to == null) {
                this.parseErrors.add(0, "Invalid stanza; missing 'to' attribute is mandatory");
            }
            if (builder.getExpectedType() != builder.type) {
                this.parseErrors.add(0, "Invalid stanza; missing or incorrect 'type' attribute");
            }
        }
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
    }

    abstract static class IQBuilder<B extends IQBuilder> {
        @Nullable JID to;
        @Nullable JID from;
        @Nullable String id;
        @Nullable IQ.Type type = getExpectedType();

        public IQBuilder() {
        }

        IQBuilder(@Nonnull final IQ iq) {
            this.to = iq.getTo();
            this.from = iq.getFrom();
            this.id = iq.getID();
            this.type = iq.getType();
        }

        @Nonnull
        protected abstract Type getExpectedType();

        void validateBuilder() {
            if (to == null) {
                throw new IllegalStateException("The stanza 'to' has not been set");
            }
        }

        @SuppressWarnings("unchecked")
        public B setTo(@Nullable JID to) {
            this.to = to;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setFrom(@Nullable JID from) {
            this.from = from;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setID(@Nullable String id) {
            this.id = id;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setType(@Nullable final IQ.Type type) {
            this.type = type;
            return (B) this;
        }

    }

}
