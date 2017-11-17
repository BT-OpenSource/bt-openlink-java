package com.bt.openlink;

public class GetCallHistoryFixtures {

    public static final String GET_CALL_HISTORY_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' action='execute'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <jid>" + CoreFixtures.USER_BARE_JID_STRING + "</jid>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' action='execute'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <jid>" + CoreFixtures.USER_BARE_JID_STRING + "</jid>\n" +
                    "        <caller>from-caller</caller>\n" +
                    "        <called>to-destination</called>\n" +
                    "        <calltype>missed</calltype>\n" +
                    "        <fromdate>06/01/2016</fromdate>\n" +
                    "        <uptodate>06/29/2016</uptodate>\n" +
                    "        <start>1</start>\n" +
                    "        <count>50</count>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_CALL_HISTORY_REQUEST_WITH_BAD_VALUES =
            "<iq type='result'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' action='execute'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <caller>from-caller</caller>\n" +
                    "        <called>to-destination</called>\n" +
                    "        <calltype>not-a-call-type</calltype>\n" +
                    "        <fromdate>not-a-from-date</fromdate>\n" +
                    "        <uptodate>not-a-to-date</uptodate>\n" +
                    "        <start>not-a-start-number</start>\n" +
                    "        <count>not-a-count-number</count>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>";

}
