package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubMessageFixtures;
import com.bt.openlink.PubSubSubscribeFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("ConstantConditions")
public class PubSubSubscribeRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final PubSubSubscribeRequest request = PubSubSubscribeRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .setPubSubNodeId(PubSubMessageFixtures.NODE_ID)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
        assertThat(request.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAForUserField() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'jid' has not been set");
        PubSubSubscribeRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(PubSubMessageFixtures.NODE_ID)
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final PubSubSubscribeRequest request = PubSubSubscribeRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(PubSubSubscribeFixtures.SUBSCRIBE_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final PubSubSubscribeRequest request = (PubSubSubscribeRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(PubSubSubscribeFixtures.SUBSCRIBE_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
        assertThat(request.getParseErrors().size(), is(0));
    }

    @Test
    public void willRoundTripAnXmppStanza() {

        final IQ originalIQ = Fixtures.iqFrom(PubSubSubscribeFixtures.SUBSCRIBE_REQUEST);
        final PubSubSubscribeRequest request = (PubSubSubscribeRequest) OpenlinkIQParser.parse(originalIQ);

        assertThat(request.toXML(), isIdenticalTo(originalIQ.toXML()).ignoreWhitespace());
    }

}