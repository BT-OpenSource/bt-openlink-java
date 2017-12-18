package com.bt.openlink.tinder.message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.message.PubSubMessageBuilder;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

public class OpenlinkPubSubMessage extends Message {

    @Nullable private final Instant delay;
    @Nullable private final PubSubNodeId pubSubNodeId;
    @Nullable private final ItemId itemId;

    @Nonnull
    private List<String> parseErrors;

    OpenlinkPubSubMessage(@Nonnull final PubSubMessageBuilder<?, JID> builder, @Nullable final List<String> parseErrors) {
        builder.getTo().ifPresent(this::setTo);
        builder.getFrom().ifPresent(this::setFrom);
        builder.getId().ifPresent(this::setID);
        this.delay = builder.getDelay().orElse(null);
        this.pubSubNodeId = builder.getPubSubNodeId().orElse(null);
        this.itemId = builder.getItemId().orElse(null);
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

    @Nonnull
    public Optional<Instant> getDelay() {
        return Optional.ofNullable(delay);
    }

    @Nonnull
    public Optional<PubSubNodeId> getPubSubNodeId() {
        return Optional.ofNullable(pubSubNodeId);
    }

    @Nonnull
    public Optional<ItemId> getItemId() {
        return Optional.ofNullable(itemId);
    }


}
