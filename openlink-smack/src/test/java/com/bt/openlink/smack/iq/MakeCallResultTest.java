package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.RequestAction;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MakeCallResultTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canBuildAStanza() {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.toXML().toString(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_RESULT).ignoreWhitespace());
    }

    @Test
    public void willEnsureTheStanzaHasACall() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");
        MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final MakeCallResult result = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_RESULT);

        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertReflectionEquals(CoreFixtures.CALL_STATUS, result.getCallStatus().get());
        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final MakeCallResult result = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_RESULT_WITH_BAD_VALUES);

        assertThat(result.getParseErrors(), contains(
                //  "Invalid stanza; missing 'to' attribute is mandatory",
                //  "Invalid stanza; missing 'from' attribute is mandatory",
                //  "Invalid stanza; missing 'id' attribute is mandatory",
                //  "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid make-call result stanza; missing or invalid callstatus"));
    }

    @Test
    public void willParseEmptyAndSelfClosingActions() throws Exception {

        final MakeCallResult result = PacketParserUtils.parseStanza("<iq from='" + CoreFixtures.FROM_JID_STRING + "' to='" + CoreFixtures.TO_JID_STRING + "' id='" + CoreFixtures.STANZA_ID + "' type='result'>\n" +
                "   <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#make-call' status='completed'>\n" +
                "     <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                "      <out>\n" +
                "       <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status' busy='false'>\n" +
                "        <call>\n" +
                "           <actions>\n" +
                "               <ClearCall/>\n" +
                "               <ClearConference/>\n" +
                "               <HoldCall></HoldCall>\n" +
                "               <SendDigit></SendDigit>\n" +
                "           </actions>\n" +
                "       </call>\n" +
                "       </callstatus>\n" +
                "      </out>\n" +
                "    </iodata>\n" +
                "  </command>\n" +
                "</iq>");

        assertThat(result.getCallStatus().get().getCalls().get(0).getActions(),
                contains(RequestAction.CLEAR_CALL, RequestAction.CLEAR_CONFERENCE, RequestAction.HOLD_CALL, RequestAction.SEND_DIGIT));
        
    }

    @Test
    public void willBuildAResultFromARequest() {

        final MakeCallRequest request = MakeCallRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        final MakeCallResult result = MakeCallResult.Builder.createResultBuilder(request)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getStanzaId(), is(request.getStanzaId()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}
