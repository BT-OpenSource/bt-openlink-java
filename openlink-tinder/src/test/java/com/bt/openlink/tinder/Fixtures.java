package com.bt.openlink.tinder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public final class Fixtures {

    private Fixtures() {
    }

    public static final JID USER_JID = new JID(CoreFixtures.USER_JID_STRING);
    public static final JID TO_JID = new JID(CoreFixtures.TO_JID_STRING);
    public static final JID FROM_JID = new JID(CoreFixtures.FROM_JID_STRING);

    private static Element elementFrom(final InputStream is) {
        try {
            final SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            return document.getRootElement();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static IQ iqFrom(final String stanzaString) {
        return new IQ(elementFrom(new ByteArrayInputStream(stanzaString.getBytes(StandardCharsets.UTF_8))));
    }

    public static Message messageFrom(final String stanzaString) {
        return new Message(elementFrom(new ByteArrayInputStream(stanzaString.getBytes(StandardCharsets.UTF_8))));
    }

}
