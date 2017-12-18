package com.bt.openlink;

import java.time.Instant;
import java.time.ZoneOffset;

import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class PubSubMessageFixtures {

    public static final ItemId ITEM_ID = ItemId.from("test-item-id").get();
    public static final PubSubNodeId NODE_ID = CoreFixtures.INTEREST_ID.toPubSubNodeId();

    public static final String CALL_STATUS_MESSAGE =
            "<message from='" + CoreFixtures.FROM_JID_STRING + "' to='" + CoreFixtures.TO_JID_STRING + "' id='" + CoreFixtures.STANZA_ID + "'>\n" +
                    "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                    "    <items node='" + NODE_ID + "'>\n" +
                    "      <item id='" + ITEM_ID + "'>\n" +
                    CoreFixtures.CALL_STATUS_INCOMING_ORIGINATED +
                    "      </item>\n" +
                    "    </items>\n" +
                    "  </event>\n" +
                    "</message>";

    public static final String CALL_STATUS_MESSAGE_WITH_LEGACY_TIMESTAMP_ONLY = CALL_STATUS_MESSAGE.replace(" starttime='" + CoreFixtures.START_TIME_ISO_8601 + "'", "");

    public static final String CALL_STATUS_MESSAGE_WITH_MISMATCHED_TIMESTAMPS = CALL_STATUS_MESSAGE.replace(" starttime='" + CoreFixtures.START_TIME_ISO_8601 + "'", " starttime='" + CoreFixtures.ISO_8601_FORMATTER.format(CoreFixtures.START_TIME.plusSeconds(60).atZone(ZoneOffset.UTC)) + "'");

    public static final String CALL_STATUS_MESSAGE_WITH_NO_FIELDS =
            "<message>\n" +
                    "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                    "    <items>\n" +
                    "      <item>\n" +
                    "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                    "        </callstatus>\n" +
                    "      </item>\n" +
                    "    </items>\n" +
                    "  </event>\n" +
                    "</message>";

    public static final Instant DELAYED_FROM = Instant.parse("2016-09-01T15:18:53.999Z");
    public static final String CALL_STATUS_MESSAGE_DELAYED = CALL_STATUS_MESSAGE.replace(
            "</message>",
            "  <delay xmlns='urn:xmpp:delay' stamp='" + DELAYED_FROM + "'/>\n</message>");
    public static final String CALL_STATUS_MESSAGE_DELAYED_WITH_BAD_TIMESTAMP = CALL_STATUS_MESSAGE.replace(
            "</message>",
            "  <delay xmlns='urn:xmpp:delay' stamp='not-a-timestamp'/>\n</message>");

    public static final String DEVICE_STATUS_EVENT =
            "<message from='pubsub.btp194094' to='ucwa.btp194094' id='Sma0SFtv'>\n"+
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
                    "</message>";
}
