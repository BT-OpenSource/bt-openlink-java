package com.bt.openlink;

public class PubSubPublishFixtures {

    public static final String PUBLISH_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "   <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                    "    <publish node='" + CoreFixtures.INTEREST_ID + "'>\n" +
                    "      <item>\n" +
                    CoreFixtures.CALL_STATUS_INCOMING_ORIGINATED +
                    "      </item>\n" +
                    "    </publish>\n" +
                    "  </pubsub>" +
                    "</iq>\n";

    // TODO: (Greg 2017-10-03) Implement all features, replace with PUBLISH_REQUEST
    public static final String PUBLISH_REQUEST_WITH_NO_FEATURES = PUBLISH_REQUEST.replaceAll("(?s)<features>.*</features>\n", "");
}
