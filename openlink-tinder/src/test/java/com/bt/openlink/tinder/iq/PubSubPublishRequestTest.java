package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Collection;
import java.util.Collections;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class PubSubPublishRequestTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String PUBLISH_REQUEST = "<iq type='set' id='" + Fixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
            "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
            "    <publish node='" + Fixtures.INTEREST_ID + "'>\n" +
            "      <item>\n" +
            "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
            "          <call>\n" +
            "            <id>" + Fixtures.CALL_ID + "</id>\n" +
            "            <site default='true' id='42' type='BTSM'>test-site-name</site>\n" +
            "            <profile devicenum='258'>" + Fixtures.PROFILE_ID + "</profile>\n" +
            "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
            "            <state>CallDelivered</state>\n" +
            "            <direction>Outgoing</direction>\n" +
            "            <caller>\n" +
            "              <number>8866</number>\n" +
            "              <name>Lucia Law 8866</name>\n" +
            "            </caller>\n" +
            "            <called>\n" +
            "              <number destination='888970008'>888970008</number>\n" +
            "              <name>888970008</name>\n" +
            "            </called>\n" +
            "            <duration>6000</duration>\n" +
            "            <actions>\n" +
            "              <ClearConnection/>\n" +
            "              <HoldCall/>\n" +
            "              <PrivateCall/>\n" +
            "              <ConferenceCall/>\n" +
            "              <ConnectSpeaker/>\n" +
            "              <ClearCall/>\n" +
            "            </actions>\n" +
            "            <features>\n" +
            "              <feature id='priv_1'>false</feature>\n" +
            "              <feature id='hs_1'>true</feature>\n" +
            "              <feature id='hs_2'>false</feature>\n" +
            "              <feature id='Netrix_8866'>\n" +
            "                <devicekeys xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#device-keys'>\n" +
            "                  <key>key_1:0:7</key>\n" +
            "                </devicekeys>\n" +
            "              </feature>\n" +
            "              <feature id='MK1205'>VoiceMessage</feature>\n" +
            "              <feature id='voicerecorder_1'>\n" +
            "                <voicerecorder xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#voice-recorder'>\n" +
            "                  <recnumber>001</recnumber>\n" +
            "                  <recport>1</recport>\n" +
            "                  <recchan>4</recchan>\n" +
            "                  <rectype>?</rectype>\n" +
            "                </voicerecorder>\n" +
            "              </feature>\n" +
            "            </features>\n" +
            "            <participants>\n" +
            "              <participant jid='lawl@moitrader.int.clsa.com' type='Active' direction='Outgoing' timestamp='Wed Mar 18 20:27:25 CST 2015' duration='0'/>\n" +
            "            </participants>\n" +
            "          </call>\n" +
            "        </callstatus>\n" +
            "      </item>\n" +
            "    </publish>\n" +
            "  </pubsub>" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.NODE_ID)
                .addCall(Fixtures.CALL)
                .build();

        assertThat(request.getID(), is(Fixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        assertThat(request.getCalls().size(), is(1));
        assertThat(request.getCalls().iterator().next(), is(Fixtures.CALL));
    }

    @Test
    public void cannotCreateAStanzaWithoutAPubSubNodeId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");
        PubSubPublishRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithACallOnADifferentInterestFromTheNode() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id another-node");
        PubSubPublishRequest.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(PubSubNodeId.from("another-node").get())
                .addCalls(Collections.singletonList(Fixtures.CALL))
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        // TODO: (Greg 2017-10-03) Implement all features, use PUBLISH_REQUEST
        final String partRequest = "<iq type='set' id='" + Fixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
                "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                "    <publish node='" + Fixtures.INTEREST_ID + "'>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "          <call>\n" +
                "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                "            <site default='true' id='42' type='BTSM'>test-site-name</site>\n" +
                "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                "            <state>CallOriginated</state>\n" +
                "            <direction>Incoming</direction>\n" +
                "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
                "            <duration>60000</duration>\n" +
                "            <actions>\n" +
                "              <AnswerCall/>\n" +
                "            </actions>\n" +
                "            <participants>\n" +
                "              <participant direction='Incoming' jid='test-user@test-domain' starttime='2017-10-09T08:07:00.000Z' duration='60000' type='Active'/>\n" +
                "            </participants>\n" +
                "          </call>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </publish>\n" +
                "  </pubsub>" +
                "</iq>\n";

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(Fixtures.CALL)
                .setInterestId(Fixtures.INTEREST_ID)
                .build();
        assertThat(request.toXML(), isIdenticalTo(partRequest).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final PubSubPublishRequest request = (PubSubPublishRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(PUBLISH_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(Fixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        final Collection<Call> calls = request.getCalls();
        assertThat(calls.size(), is(1));
        final Call call = calls.iterator().next();
        assertThat(call.getId().get(), is(Fixtures.CALL_ID));
        assertThat(call.getInterestId().get(), is(Fixtures.INTEREST_ID));
        assertThat(call.getProfileId().get(), is(Fixtures.PROFILE_ID));
    }

    @Test
    public void willRoundTripAnXmppStanza() throws Exception {

        // TODO: (Greg 2017-10-03) Implement all features, use PUBLISH_REQUEST
        final String partRequest = "<iq type='set' id='" + Fixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
                "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                "    <publish node='" + Fixtures.INTEREST_ID + "'>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "          <call>\n" +
                "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                "            <site default='true' id='42' type='BTSM'>test-site-name</site>\n" +
                "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                "            <state>CallOriginated</state>\n" +
                "            <direction>Incoming</direction>\n" +
                "            <duration>0</duration>\n" +
                "            <actions>\n" +
                "              <AnswerCall/>\n" +
                "            </actions>\n" +
                "            <participants>\n" +
                "              <participant direction='Incoming' jid='test-user@test-domain' starttime='2017-10-09T08:07:00.000Z' duration='60000' type='Active'/>\n" +
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