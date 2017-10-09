package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.iq.IQBuilder;

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

    OpenlinkIQ(@Nonnull String command, @Nonnull String uri, final @Nonnull IQBuilder<?, Jid, IQ.Type> builder, @Nullable List<String> parseErrors) {
        super(command, uri);
        builder.getTo().ifPresent(this::setTo);
        builder.getFrom().ifPresent(this::setFrom);
        builder.getId().ifPresent(this::setStanzaId);
        builder.getIqType().ifPresent(this::setType);
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = new ArrayList<>(parseErrors);
            builder.validate(parseErrors);
        }
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
    }

}
