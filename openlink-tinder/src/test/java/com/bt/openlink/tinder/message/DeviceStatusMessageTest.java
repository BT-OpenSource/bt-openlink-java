package com.bt.openlink.tinder.message;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.Message;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubMessageFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.ItemId;

@SuppressWarnings("ConstantConditions")
public class DeviceStatusMessageTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final DeviceStatusMessage message = DeviceStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_OUTGOING_CONFERENCED.getInterestId().get())
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .build();

        assertThat(message.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        final DeviceStatus deviceStatus = message.getDeviceStatus().get();
        assertThat(deviceStatus.isOnline().get(), is(true));
        assertThat(deviceStatus.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final DeviceStatusMessage message = DeviceStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_OUTGOING_CONFERENCED.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .build();

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.DEVICE_STATUS_MESSAGE).ignoreWhitespace());
    }

    @Test
    public void willCreateAStanzaWithoutMandatoryFields() {

        final DeviceStatusMessage message = DeviceStatusMessage.Builder.start()
                .build(new ArrayList<>());

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.PUBSUB_MESSAGE_WITH_NO_FIELDS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.DEVICE_STATUS_MESSAGE);

        final DeviceStatusMessage message = (DeviceStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(message.getItemId().get(), is(PubSubMessageFixtures.ITEM_ID));
        final DeviceStatus deviceStatus = message.getDeviceStatus().get();
        assertThat(EqualsBuilder.reflectionEquals(CoreFixtures.DEVICE_STATUS_LOGON, deviceStatus, false, null, true), is(true));
    }
}