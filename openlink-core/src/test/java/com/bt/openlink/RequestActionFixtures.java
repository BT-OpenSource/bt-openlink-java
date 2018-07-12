package com.bt.openlink;

import com.bt.openlink.type.RequestActionValue;

@SuppressWarnings("ConstantConditions")
public class RequestActionFixtures {
    public static final RequestActionValue REQUEST_ACTION_VALUE_1 = RequestActionValue.from("test-request-action-value-1").get();
    public static final RequestActionValue REQUEST_ACTION_VALUE_2 = RequestActionValue.from("test-request-action-value-2").get();

    public static final String REQUEST_ACTION_REQUEST =
            "<iq from='" + CoreFixtures.FROM_JID_STRING + "' to='" + CoreFixtures.TO_JID_STRING + "' id='" + CoreFixtures.STANZA_ID + "' type='set'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#request-action'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
                    "        <action>StartVoiceDrop</action>\n" +
                    "        <call>" + CoreFixtures.CALL_ID + "</call>\n" +
                    "        <value1>" + REQUEST_ACTION_VALUE_1 + "</value1>\n" +
                    "        <value2>" + REQUEST_ACTION_VALUE_2 + "</value2>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>";

    public static final String REQUEST_ACTION_REQUEST_WITH_BAD_VALUES =
            "<iq type='set'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#request-action'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>";

    public static final String REQUEST_ACTION_RESULT =
            "<iq from='" + CoreFixtures.FROM_JID_STRING + "' to='" + CoreFixtures.TO_JID_STRING + "' id='" + CoreFixtures.STANZA_ID + "' type='result'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#request-action' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    CoreFixtures.CALL_STATUS_OUTGOING_CONFERENCED +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>";
}
