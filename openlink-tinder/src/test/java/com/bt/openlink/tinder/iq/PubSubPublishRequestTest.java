package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;

import com.bt.openlink.type.DeviceStatus;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubPublishFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings({"ConstantConditions", "RedundantThrows"})
public class PubSubPublishRequestTest {
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

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.NODE_ID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        assertThat(request.getCalls().size(), is(1));
        assertThat(request.getCalls().iterator().next(), is(CoreFixtures.CALL_INCOMING_ORIGINATED));
    }

    @Test
    public void cannotCreateAStanzaWithoutAPubSubNodeId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");
        PubSubPublishRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
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
                .addCalls(Collections.singletonList(CoreFixtures.CALL_INCOMING_ORIGINATED))
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();
        assertThat(request.toXML(), isIdenticalTo(PubSubPublishFixtures.PUBLISH_REQUEST_CALL_STATUS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final PubSubPublishRequest request = (PubSubPublishRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(PubSubPublishFixtures.PUBLISH_REQUEST_CALL_STATUS));
        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        assertThat(request.isCallStatusBusy().get(),is(false));
        final Collection<Call> calls = request.getCalls();
        assertThat(calls.size(), is(1));
        final Call call = calls.iterator().next();
        assertThat(call.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(call.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(call.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void willRoundTripAnXmppStanza() throws Exception {

        final IQ originalIQ = Fixtures.iqFrom(PubSubPublishFixtures.PUBLISH_REQUEST_CALL_STATUS);
        final PubSubPublishRequest request = (PubSubPublishRequest) OpenlinkIQParser.parse(originalIQ);

        assertThat(request.toXML(), isIdenticalTo(PubSubPublishFixtures.PUBLISH_REQUEST_CALL_STATUS).ignoreWhitespace());
    }

    @Test
    public void canCreateADeviceStatusStanza() throws Exception {

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.NODE_ID)
                .setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        assertThat(request.getCalls().size(), is(0));
        assertThat(request.getDeviceStatus().get(), is(CoreFixtures.DEVICE_STATUS_LOGON));
    }

    @Test
    public void willGenerateADeviceStatusXmppStanza() throws Exception {

        final PubSubPublishRequest request = PubSubPublishRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.NODE_ID)
                .setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .build();
        assertThat(request.toXML(), isIdenticalTo(PubSubPublishFixtures.PUBLISH_REQUEST_DEVICE_STATUS).ignoreWhitespace());
    }

    @Test
    public void willParseADeviceStatusXmppStanza() throws Exception {

        final PubSubPublishRequest request = (PubSubPublishRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(PubSubPublishFixtures.PUBLISH_REQUEST_DEVICE_STATUS));

        final DeviceStatus deviceStatus = request.getDeviceStatus().get();
        assertThat(deviceStatus.isOnline().get(), is(true));
        assertThat(deviceStatus.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }


}