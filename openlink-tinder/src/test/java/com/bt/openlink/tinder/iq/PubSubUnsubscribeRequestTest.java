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
import com.bt.openlink.PubSubSubscribeFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("ConstantConditions")
public class PubSubUnsubscribeRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() throws Exception {

        final PubSubUnsubscribeRequest request = PubSubUnsubscribeRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.NODE_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final PubSubUnsubscribeRequest request = PubSubUnsubscribeRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        assertThat(request.toXML(),isIdenticalTo(PubSubSubscribeFixtures.UNSUBSCRIBE_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final PubSubUnsubscribeRequest request = (PubSubUnsubscribeRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(PubSubSubscribeFixtures.UNSUBSCRIBE_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
        assertThat(request.getParseErrors().size(), is(0));
    }

    @Test
    public void willRoundTripAnXmppStanza() throws Exception {

        final IQ originalIQ = Fixtures.iqFrom(PubSubSubscribeFixtures.UNSUBSCRIBE_REQUEST);
        final PubSubUnsubscribeRequest request = (PubSubUnsubscribeRequest) OpenlinkIQParser.parse(originalIQ);

        assertThat(request.toXML(),isIdenticalTo(PubSubSubscribeFixtures.UNSUBSCRIBE_REQUEST).ignoreWhitespace());
    }

}