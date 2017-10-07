package com.bt.openlink.tinder;

import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import com.bt.openlink.StanzaBuilder;

public final class TinderStanzaBuilder {

    private TinderStanzaBuilder() {
    }

    public static void setStanzaBuilder(final StanzaBuilder<?, JID> builder, final Packet packet) {
        builder.setTo(packet.getTo());
        builder.setFrom(packet.getFrom());
    }
}
