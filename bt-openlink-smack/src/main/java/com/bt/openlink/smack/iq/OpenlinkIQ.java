package com.bt.openlink.smack.iq;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

abstract class OpenlinkIQ extends IQ {

    static void moveToStartOfTag(final XmlPullParser parser, final String... tags) throws XmlPullParserException, IOException {
        for (final String tag : tags) {
            parser.nextTag();
            if (!tag.equals(parser.getName())) {
                return;
            }
        }
    }

    @Nonnull private List<String> parseErrors;

    OpenlinkIQ(@Nonnull String command, @Nonnull String uri, final @Nonnull IQBuilder builder, @Nonnull List<String> parseErrors) {
        super(command, uri);
        // Note; if we're being parsed, then the to/from/type/stanzaId are rewritten by the parser, so there's no need to parse them
        setTo(builder.to);
        setFrom(builder.from);
        setType(builder.getExpectedType());
        if (builder.stanzaId != null) {
            setStanzaId(builder.stanzaId);
        }
        this.parseErrors = parseErrors;
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
    }

    static abstract class IQBuilder<B extends IQBuilder> {

        @Nullable Jid to;
        @Nullable Jid from;
        @Nullable String stanzaId;

        @Nonnull
        protected abstract Type getExpectedType();

        void validateBuilder() {
            if (to == null) {
                throw new IllegalStateException("The stanza 'to' has not been set");
            }
        }

        @SuppressWarnings("unchecked")
        public B setTo(@Nullable Jid to) {
            this.to = to;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setFrom(@Nullable Jid from) {
            this.from = from;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B setStanzaId(@Nullable String stanzaId) {
            this.stanzaId = stanzaId;
            return (B) this;
        }

    }

}
