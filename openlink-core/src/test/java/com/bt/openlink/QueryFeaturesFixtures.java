package com.bt.openlink;

public class QueryFeaturesFixtures {

    public static final String QUERY_FEATURES_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#query-features\" action=\"execute\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"input\">\n" +
            "      <in>\n" +
            "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String QUERY_FEATURES_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#query-features'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";
}
