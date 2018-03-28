package com.bt.openlink;

import com.bt.openlink.type.MakeCallFeature;

@SuppressWarnings("ConstantConditions")
public class MakeCallFixtures {

    public static final MakeCallFeature MAKE_CALL_FEATURE = MakeCallFeature.Builder.start()
            .setFeatureId(CoreFixtures.FEATURE_ID)
            .setValue1(1)
            .setValue2(false)
            .build();

    public static final String MAKE_CALL_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#make-call'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <jid>" + CoreFixtures.USER_FULL_JID_STRING + "</jid>\n" +
                    "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                    "        <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
                    "        <destination>" + CoreFixtures.CALLED_DESTINATION + "</destination>\n" +
                    "        <originator-ref>\n" +
                    "          <property id='key1'>\n" +
                    "            <value>value1</value>\n" +
                    "          </property>\n" +
                    "          <property id='key2'>\n" +
                    "            <value>value2</value>\n" +
                    "          </property>\n" +
                    "        </originator-ref>\n" +
                    "        <features>" +
                    "           <feature>" +
                    "               <id>" + MAKE_CALL_FEATURE.getFeatureId().get() + "</id>" +
                    "               <value1>" + MAKE_CALL_FEATURE.getValue1().get() + "</value1>" +
                    "               <value2>" + MAKE_CALL_FEATURE.getValue2().get() + "</value2>" +
                    "           </feature>" +
                    "        </features>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String MAKE_CALL_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#make-call'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String MAKE_CALL_RESULT =
            "<iq from='" + CoreFixtures.FROM_JID_STRING + "' to='" + CoreFixtures.TO_JID_STRING + "' id='" + CoreFixtures.STANZA_ID + "' type='result'>\n" +
                    "   <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#make-call' status='completed'>\n" +
                    "     <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    CoreFixtures.CALL_STATUS_INCOMING_ORIGINATED +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>";

    public static final String MAKE_CALL_RESULT_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "   <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#make-call' status='completed'>\n" +
                    "     <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>";

}
