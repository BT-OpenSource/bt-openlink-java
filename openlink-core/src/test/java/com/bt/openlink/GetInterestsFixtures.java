package com.bt.openlink;

public class GetInterestsFixtures {

    public static final String GET_INTERESTS_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-interests' action='execute'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_INTERESTS_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-interests'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_INTERESTS_RESULT =
            "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-interests' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "        <interests xmlns='http://xmpp.org/protocol/openlink:01:00:00/interests'>\n" +
                    "          <interest id='" + CoreFixtures.INTEREST_ID + "' type='test-interest-type' label='test interest label' default='true'/>\n" +
                    "          <interest id='sip:6001@uta.bt.com-DirectDial-1trader1@btsm11' type='DirectoryNumber' label='6001/1' default='false'/>\n" +
                    "        </interests>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_INTERESTS_RESULT_WITH_BAD_VALUES =
            "<iq type='set'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-interests' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

}
