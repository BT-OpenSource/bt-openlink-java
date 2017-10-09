package com.bt.openlink.tinder.iq;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.iq.IQBuilder;
import com.bt.openlink.tinder.TinderStanzaBuilder;

final class TinderIQBuilder {

    private TinderIQBuilder() {
    }

    static void setIQBuilder(final IQBuilder<?, JID, IQ.Type> builder, final IQ iq) {
        TinderStanzaBuilder.setStanzaBuilder(builder, iq);
        builder.setIQType(iq.getType())
                .setId(iq.getID());
    }
}
