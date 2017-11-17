package com.bt.openlink.tinder.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.Message;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubMessageFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class CallStatusMessageTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_INCOMING_ORIGINATED.getInterestId().get())
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();

        assertThat(message.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        final List<Call> calls = message.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall, is(sameInstance(CoreFixtures.CALL_INCOMING_ORIGINATED)));
        assertThat(calls.size(), is(1));
    }

    @Test
    public void canCreateAStanzaWithANullId() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_INCOMING_ORIGINATED.getInterestId().get())
                .addCalls(Collections.singletonList(CoreFixtures.CALL_INCOMING_ORIGINATED))
                .build();

        assertThat(message.getID(), is(nullValue()));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_INCOMING_ORIGINATED.getInterestId().get())
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithoutAnPubSubNodeIdField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId' has not been set");
        CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();
    }

    @Test
    public void cannotCreateAMessageWithACallOnADifferentNode() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id 'test-call-id' is not on this pubsub node");
        CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(PubSubNodeId.from("not-" + CoreFixtures.INTEREST_ID).get())
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
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
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_INCOMING_ORIGINATED.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .setCallStatusBusy(true)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.CALL_STATUS_MESSAGE).ignoreWhitespace());
    }

    @Test
    public void willCreateAStanzaWithoutMandatoryFields() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .build(new ArrayList<>());

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.CALL_STATUS_MESSAGE_WITH_NO_FIELDS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(CoreFixtures.NODE_ID));
        final List<Call> calls = message.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(theOnlyCall.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(theOnlyCall.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(theOnlyCall.getChanged().get(), is(Changed.STATE));
        assertThat(theOnlyCall.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(theOnlyCall.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(theOnlyCall.getStartTime().get(), is(CoreFixtures.START_TIME));
        assertThat(theOnlyCall.getDuration().get(), is(Duration.ofMinutes(1)));
        assertThat(calls.size(), is(1));
        assertThat(message.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnOriginalMessageForADeviceStatusEvent() throws Exception {

        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.DEVICE_STATUS_EVENT);

        assertThat(OpenlinkMessageParser.parse(stanza), is(sameInstance(stanza)));
    }

    @Test
    public void willBuildAMessageWithADelay() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_INCOMING_ORIGINATED.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .setDelay(PubSubMessageFixtures.DELAYED_FROM)
                .build();

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.CALL_STATUS_MESSAGE_DELAYED).ignoreWhitespace());
    }

    @Test
    public void willParseAMessageWithADelay() throws Exception {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_DELAYED);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay().get(), is(PubSubMessageFixtures.DELAYED_FROM));
    }

    @Test
    public void willParseAMessageWithBadFields() throws Exception {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_DELAYED_WITH_BAD_TIMESTAMP);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay(), is(Optional.empty()));
        assertThat(
                message.getParseErrors(),
                contains("Invalid call status; invalid timestamp 'not-a-timestamp'; format should be compliant with XEP-0082"));
    }

    @Test
    public void willParseAMessageWithALegacyTimestamp() throws Exception {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_WITH_LEGACY_TIMESTAMP_ONLY);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getCalls().get(0).getParticipants().get(0).getStartTime().get(), is(CoreFixtures.START_TIME));
    }

    @Test
    public void theLegacyTimestampShouldMatchTheStartTime() throws Exception {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_WITH_MISMATCHED_TIMESTAMPS);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getParseErrors(), contains("Invalid participant; the legacy timestamp field does not match the start time field"));
    }
}