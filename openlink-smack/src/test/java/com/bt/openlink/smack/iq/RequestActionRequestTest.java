package com.bt.openlink.smack.iq;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.RequestAction;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

@SuppressWarnings({ "ConstantConditions", "RedundantThrows" })
public class RequestActionRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() throws Exception {
        final RequestActionRequest request = RequestActionRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .build();

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getAction().get(), is(RequestAction.START_VOICE_DROP));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getCallId().get(), is(CoreFixtures.CALL_ID));
        assertThat(request.getValue1().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_1));
        assertThat(request.getValue2().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_2));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final RequestActionRequest request = RequestActionRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .build();

        assertThat(request.toXML().toString(), isIdenticalTo(RequestActionFixtures.REQUEST_ACTION_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final RequestActionRequest request = PacketParserUtils.parseStanza(RequestActionFixtures.REQUEST_ACTION_REQUEST);
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getAction().get(), is(RequestAction.START_VOICE_DROP));
        assertThat(request.getCallId().get(), is(CoreFixtures.CALL_ID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getValue1().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_1));
        assertThat(request.getValue2().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_2));
    }

    @Test
    public void cannotCreateAStanzaWithoutAnInterestId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action 'interestId' has not been set");
        RequestActionRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithoutAnAction() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action 'action' has not been set");
        RequestActionRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();
    }

}