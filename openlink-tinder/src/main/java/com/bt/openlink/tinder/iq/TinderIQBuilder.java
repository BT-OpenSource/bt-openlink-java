package com.bt.openlink.tinder.iq;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.IQ.IQBuilder;
import com.bt.openlink.tinder.TinderStanzaBuilder;

final class TinderIQBuilder {

    private TinderIQBuilder() {
    }

    static void setIQBuilder(final IQBuilder<?, JID, IQ.Type> builder, final IQ iq) {
        TinderStanzaBuilder.setStanzaBuilder(builder, iq);
        builder.setID(iq.getID());
        builder.setIQType(iq.getType());
    }
}
