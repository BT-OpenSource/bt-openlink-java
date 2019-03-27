package com.bt.openlink;

public class GetProfileFixtures {

    public static final String GET_PROFILE_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-profile' action='execute'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILE_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-profile'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILE_RESULT =
            "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-profile' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "       <out>\n" +
                    "        <profile xmlns='http://xmpp.org/protocol/openlink:01:00:00/profile/Netrix+Button' online='true' devicenum='271'>\n" +
                    "          <keypages>\n" +
                    "            <keypage id='300' label='KeyPage 1' module='1' local_keypage='2'>\n" +
                    "              <key id='key_1:2:1' label='Key Label 1' function='14' qualifier='553' modifier='0' color='5' interest='L553'/>\n" +
                    "              <key id='key_1:3:1' label='Key Label 2' function='14' qualifier='554' modifier='0' color='5' interest='L554'/>\n" +
                    "            </keypage>            \n" +
                    "            <keypage id='301' label='KeyPage 2' module='2' local_keypage='3'>\n" +
                    "              <key id='key_2:3:1' label='Key Label 3' function='14' qualifier='555' modifier='0' color='5' interest='L555'/>\n" +
                    "              <key id='key_2:3:2' label='Key Label 4' function='14' qualifier='556' modifier='0' color='5' interest='L556'/>\n" +
                    "            </keypage>\n" +
                    "          </keypages>\n" +
                    "        </profile>\n" +
                    "      </out>" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILE_RESULT_WITH_NO_KEYPAGES =
            "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-profile' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "       <out>\n" +
                    "        <profile xmlns='http://xmpp.org/protocol/openlink:01:00:00/profile/Netrix+Button' online='true' devicenum='271'>\n" +
                    "          <keypages/>\n" +
                    "        </profile>\n" +
                    "      </out>" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILE_RESULT_WITH_BAD_VALUES =
            "<iq type='set'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' status='completed' node='http://xmpp.org/protocol/openlink:01:00:00#get-profile'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

}
