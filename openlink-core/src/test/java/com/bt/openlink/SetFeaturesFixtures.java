package com.bt.openlink;

public class SetFeaturesFixtures {

    public static final String SET_FEATURES_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "    <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#set-features\"\n" +
            "             action=\"execute\">\n" +
            "        <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"input\">\n" +
            "            <in xmlns=\"urn:xmpp:tmp:io-data\">\n" +
            "                <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "                <feature>" + CoreFixtures.FEATURE_ID + "</feature>\n" +
            "                <value1>SetLabel</value1>\n" +
            "            </in>\n" +
            "        </iodata>\n" +
            "    </command>\n" +
            "</iq>\n";

    public static final String SET_FEATURES_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#set-features'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String SET_FEATURES_RESULT =  "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "<command xmlns=\"http://jabber.org/protocol/commands\" \n" +
            "         node=\"http://xmpp.org/protocol/openlink:01:00:00#set-features\" status=\"completed\"/>\n" +
            "</iq>";
}
