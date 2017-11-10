package com.bt.openlink.tinder.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.Message;

import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class CallStatusMessageTest {
    private static final String CALL_STATUS_MESSAGE = "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
            "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
            "    <items node='" + Fixtures.NODE_ID + "'>\n" +
            "      <item id='test-item-id'>\n" +
            "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status' busy='true'>\n" +
            "          <call>\n" +
            "            <id>" + Fixtures.CALL_ID + "</id>\n" +
            "            <site default='true' id='42' type='BTSM'>test-site-name</site>\n" +
            "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
            "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
            "            <changed>State</changed>\n" +
            "            <state>CallOriginated</state>\n" +
            "            <direction>Outgoing</direction>\n" +
            "            <caller>\n" +
            "              <number e164=\"test-caller-e164-number\">test-caller-number</number>\n" +
            "              <name>test-caller-name</name>\n" +
            "            </caller>\n" +
            "            <called>\n" +
            "              <number destination=\"test-called-destination\" e164=\"test-called-e164-number\">test-called-number</number>\n" +
            "              <name>test-called-name</name>\n" +
            "            </called>\n" +
            "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
            "            <duration>60000</duration>\n" +
            "            <actions>\n" +
            "              <AnswerCall/>\n" +
            "            </actions>" +
            "            <features>\n" +
            "              <feature id='hs_1' type='Handset' label='Handset 1'>false</feature>\n" +
            "              <feature id='hs_2' type='Handset' label='Handset 2'>false</feature>\n" +
            "              <feature id='priv_1' type='Privacy' label='Privacy'>false</feature>\n" +
            "              <feature id='NetrixHiTouch_sales1' type='DeviceKeys' label='NetrixHiTouch'>\n" +
            "                <devicekeys xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#device-keys'>\n" +
            "                  <key>key_1:1:1</key>\n" +
            "                </devicekeys>\n" +
            "              </feature>\n" +
            "            </features>\n" +
            "            <participants>\n" +
            "              <participant direction=\"Incoming\" duration=\"60000\" jid=\"test-user@test-domain\" starttime=\"2017-10-09T08:07:00.000Z\" timestamp=\"Mon Oct 09 08:07:00 UTC 2017\" type=\"Active\"/>\n" +
            "            </participants>\n" +
            "          </call>\n" +
            "        </callstatus>\n" +
            "      </item>\n" +
            "    </items>\n" +
            "  </event>\n" +
            "</message>";

    private static final Instant delayedFrom = Instant.parse("2016-09-01T15:18:53.999Z");
    private static final String CALL_STATUS_MESSAGE_DELAYED = CALL_STATUS_MESSAGE.replace("</message>", "  <delay xmlns='urn:xmpp:delay' stamp='" + delayedFrom + "'/>\n</message>");

    @Rule public final ExpectedException expectedException = ExpectedException.none();

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

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCall(Fixtures.CALL)
                .build();

        assertThat(message.getID(), is(Fixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        final List<Call> calls = message.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall, is(sameInstance(Fixtures.CALL)));
        assertThat(calls.size(), is(1));
    }

    @Test
    public void canCreateAStanzaWithANullId() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCalls(Collections.singletonList(Fixtures.CALL))
                .build();

        assertThat(message.getID(), is(nullValue()));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        CallStatusMessage.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithoutAnPubSubNodeIdField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId' has not been set");
        CallStatusMessage.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void cannotCreateAMessageWithACallOnADifferentNode() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id 'test-call-id' is not on this pubsub node");
        CallStatusMessage.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(PubSubNodeId.from("not-" + Fixtures.INTEREST_ID).get())
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void canCreateAStanzaWithMissingFields() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .build(new ArrayList<>());

        assertThat(message.getID(), is(nullValue()));
        assertThat(message.getTo(), is(nullValue()));
        assertThat(message.getFrom(), is(nullValue()));
        assertThat(message.getCalls(), is(empty()));
        assertThat(message.getDelay(), is(Optional.empty()));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .setCallStatusBusy(true)
                .addCall(Fixtures.CALL)
                .build();

        assertThat(message.toXML(), isIdenticalTo(CALL_STATUS_MESSAGE).ignoreWhitespace());
    }

    @Test
    public void willCreateAStanzaWithoutMandatoryFields() throws Exception {

        final String expectedXML = "<message>\n" +
                "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                "    <items>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </items>\n" +
                "  </event>\n" +
                "</message>";

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .build(new ArrayList<>());

        assertThat(message.toXML(), isIdenticalTo(expectedXML).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final Message stanza = Fixtures.messageFrom(CALL_STATUS_MESSAGE);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getID(), is(Fixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        final List<Call> calls = message.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall.getId().get(), is(Fixtures.CALL_ID));
        assertThat(theOnlyCall.getProfileId().get(), is(Fixtures.PROFILE_ID));
        assertThat(theOnlyCall.getInterestId().get(), is(Fixtures.INTEREST_ID));
        assertThat(theOnlyCall.getChanged().get(), is(Changed.STATE));
        assertThat(theOnlyCall.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(theOnlyCall.getDirection().get(), is(CallDirection.OUTGOING));
        assertThat(theOnlyCall.getStartTime().get(), is(Fixtures.START_TIME));
        assertThat(theOnlyCall.getDuration().get(), is(Duration.ofMinutes(1)));
        assertThat(calls.size(), is(1));
        assertThat(message.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnOriginalMessageForADeviceStatusEvent() throws Exception {

        final Message stanza = Fixtures
                .messageFrom(
                "<message from='pubsub.btp194094' to='ucwa.btp194094' id='Sma0SFtv'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='sip:6004@uta.bt.com-DirectDial-1sales1@btsm2'>\n" +
                        "      <item id='sip:6004@uta.bt.com-DirectDial-1sales1@btsm2'>\n" +
                        "        <devicestatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#device-status'>\n" +
                        "          <profile online='true'>/netrix/Cluster1|/uta/enterprises/bt/users/Sales1/denormalised-profiles/UCSales1/versions/72?build=70&amp;location=global.uk.Ipswich&amp;device=NetrixHiTouch</profile>\n" +
                        "          <interest id='sip:6004@uta.bt.com-DirectDial-1sales1@btsm2' online='true'/>\n" +
                        "        </devicestatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "</message>");

        assertThat(OpenlinkMessageParser.parse(stanza), is(sameInstance(stanza)));
    }

    @Test
    public void willBuildAMessageWithADelay() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .addCall(Fixtures.CALL)
                .setDelay(delayedFrom)
                .build();

        assertThat(message.toXML(), isIdenticalTo(CALL_STATUS_MESSAGE_DELAYED).ignoreWhitespace());
    }

    @Test
    public void willParseAMessageWithADelay() throws Exception {
        final Message stanza = Fixtures.messageFrom(CALL_STATUS_MESSAGE_DELAYED);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay().get(), is(Instant.parse("2016-09-01T15:18:53.999Z")));
    }

    @Test
    public void willParseAMessageWithBadFields() throws Exception {
        final Message stanza = Fixtures.messageFrom(
                "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                        "      <item>\n" +
                        "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                        "          <call>\n" +
                        "            <id></id>\n" +
                        "            <profile></profile>\n" +
                        "            <interest></interest>\n" +
                        "            <state></state>\n" +
                        "            <direction></direction>\n" +
                        "            <starttime>yesterday</starttime>\n" +
                        "            <duration>a while</duration>\n" +
                        "          </call>\n" +
                        "        </callstatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "  <delay xmlns='urn:xmpp:delay' stamp='not-a-timestamp'/>\n" +
                        "</message>");

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay(), is(Optional.empty()));
        assertThat(
                message.getParseErrors(),
                containsInAnyOrder(
                        "Invalid call status; missing call id is mandatory",
                        "Invalid call status; missing call site is mandatory",
                        "Invalid call status; missing profile id is mandatory",
                        "Invalid call status; missing interest id is mandatory",
                        "Invalid call status; missing call state is mandatory",
                        "Invalid call status; missing call direction is mandatory",
                        "Invalid call status; missing call start time is mandatory",
                        "Invalid call status; missing call duration is mandatory",
                        "Invalid call status; invalid starttime 'yesterday'; format should be compliant with XEP-0082",
                        "Invalid call status; invalid duration 'a while'; please supply an integer",
                        "Invalid call status; invalid timestamp 'not-a-timestamp'; format should be compliant with XEP-0082"
                ));
    }

    @Test
    public void willParseAMessageWithALegacyTimestamp() throws Exception {
        final Message stanza = Fixtures.messageFrom(
                "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                        "      <item>\n" +
                        "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                        "          <call>\n" +
                        "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                        "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                        "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                        "            <state>CallOriginated</state>\n" +
                        "            <direction>Incoming</direction>\n" +
                        "            <participants>\n" +
                        "              <participant direction='Incoming' jid='test-user@test-domain' timestamp='Tue May 18 16:12:51 PST 2010' duration='0' type='Inactive'/>\n" +
                        "            </participants>\n" +
                        "          </call>\n" +
                        "        </callstatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "</message>");

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getCalls().get(0).getParticipants().get(0).getStartTime().get(), is(Instant.parse("2010-05-18T23:12:51.000Z")));
    }

    @Test
    public void theLegacyTimestampShouldMatchTheStartTime() throws Exception {
        final Message stanza = Fixtures.messageFrom(
                "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                        "      <item>\n" +
                        "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                        "          <call>\n" +
                        "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                        "            <site default='true' id='42' type='BTSM'>test-site-name</site>" +
                        "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                        "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                        "            <changed>State</changed>\n" +
                        "            <state>CallOriginated</state>\n" +
                        "            <direction>Incoming</direction>\n" +
                        "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
                        "            <duration>60000</duration>\n" +
                        "            <actions>\n" +
                        "              <AnswerCall/>\n" +
                        "            </actions>\n" +
                        "            <participants>\n" +
                        "              <participant direction='Incoming' jid='test-user@test-domain' starttime='2017-10-09T09:07:00.000Z' timestamp='Mon Oct 09 08:07:00 UTC 2017' duration='60000' type='Active'/>\n" +
                        "            </participants>\n" +
                        "          </call>\n" +
                        "        </callstatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "</message>");

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getParseErrors(), contains("Invalid participant; the legacy timestamp field does not match the start time field"));
    }
}