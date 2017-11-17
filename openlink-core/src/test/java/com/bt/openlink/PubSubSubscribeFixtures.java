package com.bt.openlink;

public class PubSubSubscribeFixtures {

    public static final String SUBSCRIBE_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "    <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                    "        <subscribe node='" + CoreFixtures.NODE_ID + "' jid='" + CoreFixtures.USER_FULL_JID_STRING + "'/>\n" +
                    "    </pubsub>\n" +
                    "</iq>\n";

    public static final String UNSUBSCRIBE_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "    <pubsub xmlns='http://jabber.org/protocol/pubsub'>\n" +
                    "        <unsubscribe node='" + CoreFixtures.NODE_ID + "' jid='" + CoreFixtures.USER_FULL_JID_STRING + "'/>\n" +
                    "    </pubsub>\n" +
                    "</iq>\n";

}
