package com.bt.openlink.smack.iq;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.hamcrest.CoreMatchers;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.RequestAction;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class RequestActionResultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() {
        final RequestActionResult result = RequestActionResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final RequestActionResult result = RequestActionResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.toXML().toString(), isIdenticalTo(RequestActionFixtures.REQUEST_ACTION_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final RequestActionResult result = PacketParserUtils.parseStanza(RequestActionFixtures.REQUEST_ACTION_RESULT);
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        assertReflectionEquals(CoreFixtures.CALL_STATUS, result.getCallStatus().get());
    }

    @Test
    public void willBuildAResultFromARequest() {

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

        final RequestActionResult result = RequestActionResult.Builder.createResultBuilder(request)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getStanzaId(), CoreMatchers.is(request.getStanzaId()));
        assertThat(result.getTo(), CoreMatchers.is(request.getFrom()));
        assertThat(result.getFrom(), CoreMatchers.is(request.getTo()));
    }

}