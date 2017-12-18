package com.bt.openlink;

public class PubSubPublishFixtures {

    public static final String PUBLISH_REQUEST_CALL_STATUS =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                    "    <publish node='" + CoreFixtures.INTEREST_ID + "'>\n" +
                    "      <item>\n" +
                    CoreFixtures.CALL_STATUS_INCOMING_ORIGINATED +
                    "      </item>\n" +
                    "    </publish>\n" +
                    "  </pubsub>" +
                    "</iq>\n";

    public static final String PUBLISH_REQUEST_DEVICE_STATUS =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                    "    <publish node='" + CoreFixtures.INTEREST_ID + "'>\n" +
                    "      <item>\n" +
                    "        <devicestatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#device-status'>\n" +
                    "          <profile online='true'>\n" +
                    "            " + CoreFixtures.PROFILE_ID + "\n" +
                    "          </profile>\n" +
                    "        </devicestatus>\n" +
                    "      </item>\n" +
                    "    </publish>\n" +
                    "  </pubsub>" +
                    "</iq>\n";

}
