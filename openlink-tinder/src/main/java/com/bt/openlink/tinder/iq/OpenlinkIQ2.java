package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.iq.IQBuilder;

abstract class OpenlinkIQ2 extends IQ {

    @Nonnull private List<String> parseErrors;

    OpenlinkIQ2(@Nonnull final IQBuilder<?, JID, IQ.Type> builder, @Nullable final List<String> parseErrors) {
        builder.getTo().ifPresent(this::setTo);
        builder.getFrom().ifPresent(this::setFrom);
        builder.getId().ifPresent(this::setID);
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
