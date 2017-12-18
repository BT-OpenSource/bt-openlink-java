package com.bt.openlink.tinder.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.message.MessageBuilder;

public class OpenlinkMessage extends Message {

    @Nonnull
    private List<String> parseErrors;

    OpenlinkMessage(@Nonnull final MessageBuilder<?, JID> builder, @Nullable final List<String> parseErrors) {
        builder.getTo().ifPresent(this::setTo);
        builder.getFrom().ifPresent(this::setFrom);
        builder.getId().ifPresent(this::setID);
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
