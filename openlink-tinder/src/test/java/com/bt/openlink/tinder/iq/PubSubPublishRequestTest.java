package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class PubSubPublishRequestTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String PUBLISH_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
            "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
            "    <publish node='" + CoreFixtures.INTEREST_ID + "'>\n" +
            "      <item>\n" +
            "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
            "          <call>\n" +
            "            <id>" + CoreFixtures.CALL_ID + "</id>\n" +
            "            <site default='true' id='42' type='BTSM'>test site name</site>\n" +
            "            <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "            <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
            "            <changed>State</changed>\n" +
            "            <state>CallOriginated</state>\n" +
            "            <direction>Incoming</direction>\n" +
            "            <caller>\n" +
            "              <number e164='test-caller-e164-number'>test-caller-number</number>\n" +
            "              <name>test-caller-name</name>\n" +
            "            </caller>\n" +
            "            <called>\n" +
            "              <number destination='test-called-destination' e164='test-called-e164-number'>test-called-number</number>\n" +
            "              <name>test-called-name</name>\n" +
            "            </called>\n" +
            "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
            "            <duration>60000</duration>\n" +
            "            <actions>\n" +
            "              <AnswerCall/>\n" +
            "            </actions>\n" +
            "            <features>\n" +
            "              <feature id='hs_1' label='Handset 1' type='Handset'>false</feature>\n" +
            "              <feature id='hs_2' label='Handset 2' type='Handset'>false</feature>\n" +
            "              <feature id='priv_1' label='Privacy' type='Privacy'>false</feature>\n" +
            "              <feature id='NetrixHiTouch_sales1' label='NetrixHiTouch' type='DeviceKeys'>\n" +
            "                <devicekeys xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#device-keys'>\n" +
            "                  <key>key_1:1:1</key>\n" +
            "                </devicekeys>\n" +
            "              </feature>\n" +
            "            </features>\n" +
            "            <participants>\n" +
            "              <participant direction='Incoming' duration='60000' jid='test-user@test-domain' starttime='2017-10-09T08:07:00.000Z' timestamp='Mon Oct 09 08:07:00 UTC 2017' type='Active'/>\n\n" +
            "            </participants>\n" +
            "          </call>\n" +
            "        </callstatus>\n" +
            "      </item>\n" +
            "    </publish>\n" +
            "  </pubsub>" +
            "</iq>\n";

    private static TimeZone systemDefaultTimeZone;

    @BeforeClass
    public static void setupClass() {
        systemDefaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    }

    @AfterClass
    public static void tearDownClass() {
        TimeZone.setDefault(systemDefaultTimeZone);
    }

    @Test
    public void canCreateAStanza() throws Exception {

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.NODE_ID)
                .addCall(CoreFixtures.CALL)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        assertThat(request.getCalls().size(), is(1));
        assertThat(request.getCalls().iterator().next(), is(CoreFixtures.CALL));
    }

    @Test
    public void cannotCreateAStanzaWithoutAPubSubNodeId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");
        PubSubPublishRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithACallOnADifferentInterestFromTheNode() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id another-node");
        PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(PubSubNodeId.from("another-node").get())
                .addCalls(Collections.singletonList(CoreFixtures.CALL))
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        // TODO: (Greg 2017-10-03) Implement all features, use PUBLISH_REQUEST
        final String partRequest = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
                "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                "    <publish node='" + CoreFixtures.INTEREST_ID + "'>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "          <call>\n" +
                "            <id>" + CoreFixtures.CALL_ID + "</id>\n" +
                "            <site default='true' id='42' type='BTSM'>test-site-name</site>\n" +
                "            <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                "            <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
                "            <changed>State</changed>\n" +
                "            <state>CallOriginated</state>\n" +
                "            <direction>Incoming</direction>\n" +
                "            <caller>\n" +
                "               <number e164='" + CoreFixtures.CALLER_E164_NUMBER + "'>" + CoreFixtures.CALLER_NUMBER + "</number>\n" +
                "               <name>" + CoreFixtures.CALLER_NAME + "</name>\n" +
                "            </caller>\n" +
                "            <called>\n" +
                "               <number destination='" + CoreFixtures.CALLED_DESTINATION + "' e164='" + CoreFixtures.CALLED_E164_NUMBER + "'>" + CoreFixtures.CALLED_NUMBER + "</number>\n" +
                "               <name>" + CoreFixtures.CALLED_NAME + "</name>\n" +
                "            </called>\n" +
                "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
                "            <duration>60000</duration>\n" +
                "            <actions>\n" +
                "              <AnswerCall/>\n" +
                "            </actions>\n" +
                "            <participants>\n" +
                "              <participant direction='Incoming' jid='test-user@test-domain' starttime='2017-10-09T08:07:00.000Z' timestamp='Mon Oct 09 08:07:00 UTC 2017' duration='60000' type='Active'/>\n" +
                "            </participants>\n" +
                "          </call>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </publish>\n" +
                "  </pubsub>" +
                "</iq>\n";

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();
        assertThat(request.toXML(), isIdenticalTo(PUBLISH_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final PubSubPublishRequest request = (PubSubPublishRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(PUBLISH_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        final Collection<Call> calls = request.getCalls();
        assertThat(calls.size(), is(1));
        final Call call = calls.iterator().next();
        assertThat(call.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(call.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(call.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void willRoundTripAnXmppStanza() throws Exception {

        // TODO: (Greg 2017-10-03) Implement all features, use PUBLISH_REQUEST
        final String partRequest = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
                "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                "    <publish node='" + CoreFixtures.INTEREST_ID + "'>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "          <call>\n" +
                "            <id>" + CoreFixtures.CALL_ID + "</id>\n" +
                "            <site default='true' id='42' type='BTSM'>test-site-name</site>\n" +
                "            <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                "            <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
                "            <state>CallOriginated</state>\n" +
                "            <direction>Incoming</direction>\n" +
                "            <caller>\n" +
                "               <number></number>\n" +
                "               <name></name>\n" +
                "            </caller>\n" +
                "            <called>\n" +
                "               <number></number>\n" +
                "               <name></name>\n" +
                "            </called>\n" +
                "            <duration>0</duration>\n" +
                "            <actions>\n" +
                "              <AnswerCall/>\n" +
                "            </actions>\n" +
                "            <participants>\n" +
                "              <participant direction='Incoming' jid='test-user@test-domain' starttime='2017-10-09T08:07:00.000Z' timestamp='Mon Oct 09 08:07:00 UTC 2017' duration='60000' type='Active'/>\n" +
                "            </participants>\n" +
                "          </call>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </publish>\n" +
                "  </pubsub>" +
                "</iq>\n";
        final IQ originalIQ = Fixtures.iqFrom(partRequest);
        final PubSubPublishRequest request = (PubSubPublishRequest) OpenlinkIQParser.parse(originalIQ);

        assertThat(request.toXML(), isIdenticalTo(originalIQ.toXML()).ignoreWhitespace());
    }

}