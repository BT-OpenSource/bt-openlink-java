package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.IQ.IQBuilder;

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

    /**
     * Sets the packet ID. Packet ID's are optional, except for IQ packets.
     *
     * @param ID
     *            the packet ID.
     */
    public void setStanzaId(String ID) {
        setID(ID);
    }

    /**
     * Returns the packet ID, or <tt>null</tt> if the packet does not have an ID. Packet ID's are optional, except for IQ
     * packets.
     *
     * @return the packet ID.
     */
    public String getStanzaId() {
        return getID();
    }

    @Nonnull
    public List<String> getParseErrors() {
        return parseErrors;
    }

}
