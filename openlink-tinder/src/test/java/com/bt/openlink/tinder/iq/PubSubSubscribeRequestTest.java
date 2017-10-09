package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("ConstantConditions")
public class PubSubSubscribeRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String SUBSCRIBE_REQUEST = "<iq type=\"set\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "    <pubsub xmlns=\"http://jabber.org/protocol/pubsub\">\n" +
            "        <subscribe node=\"" + Fixtures.NODE_ID + "\" jid=\"" + Fixtures.USER_JID + "\"/>\n" +
            "    </pubsub>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final PubSubSubscribeRequest request = PubSubSubscribeRequest.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_JID)
                .setPubSubNodeId(Fixtures.NODE_ID)
                .build();

        assertThat(request.getID(), is(Fixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_JID));
        assertThat(request.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAForUserField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'jid' has not been set");
        PubSubSubscribeRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.NODE_ID)
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final PubSubSubscribeRequest request = PubSubSubscribeRequest.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_JID)
                .setInterestId(Fixtures.INTEREST_ID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(SUBSCRIBE_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final PubSubSubscribeRequest request = (PubSubSubscribeRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(SUBSCRIBE_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(Fixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        assertThat(request.getJID().get(), is(Fixtures.USER_JID));
        assertThat(request.getParseErrors().size(), is(0));
    }

    @Test
    public void willRoundTripAnXmppStanza() throws Exception {

        final IQ originalIQ = Fixtures.iqFrom(SUBSCRIBE_REQUEST);
        final PubSubSubscribeRequest request = (PubSubSubscribeRequest) OpenlinkIQParser.parse(originalIQ);

        assertThat(request.toXML(), isIdenticalTo(originalIQ.toXML()).ignoreWhitespace());
    }

}