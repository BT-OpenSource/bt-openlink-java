package com.bt.openlink.tinder.message;

import java.util.List;

import javax.annotation.Nonnull;

import org.dom4j.Element;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import com.bt.openlink.tinder.internal.TinderPacketUtil;

public final class OpenlinkMessageParser {

    private OpenlinkMessageParser() {
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <P extends Packet> P parse(@Nonnull final Message message) {

        final List elements = message.getElement().elements();
        if (elements.isEmpty()) {
            return (P) message;
        }
        final Element childElement = (Element) elements.get(0);
        final String namespace = childElement.getNamespaceURI();
        if (namespace.equals("http://jabber.org/protocol/pubsub#event")) {
            return (P) parsePubSubEvent(message);
        } else {
            return (P) message;
        }
    }

    @Nonnull
    private static Message parsePubSubEvent(final Message message) {
        final Element callStatusElement = TinderPacketUtil.getChildElement(message.getElement(), "event", "items", "item", "callstatus");
        final Element deviceStatusElement = TinderPacketUtil.getChildElement(message.getElement(), "event", "items", "item", "devicestatus");
        if (callStatusElement != null) {
            return CallStatusMessage.from(message);
        } else if (deviceStatusElement != null) {
            return DeviceStatusMessage.from(message);
        } else {
            return message;
        }
    }
}
