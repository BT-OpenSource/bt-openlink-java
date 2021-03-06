package com.bt.openlink;

import java.io.Serializable;

import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.HistoricalCall;

public class GetCallHistoryFixtures {

    public static final String CALL_HISTORY_RESULT = "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' status='completed'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
            "      <out>\n" +
            "        <callhistory xmlns='http://xmpp.org/protocol/openlink:01:00:00/callhistory' count='1' start='0' total='2'>\n" +
            "          <call>\n" +
            "            <id>test-call-id</id>\n" +
            "            <profile>test-user-id</profile>\n" +
            "            <interest>test-interest-id</interest>\n" +
            "            <state>CallEstablished</state>\n" +
            "            <direction>Outgoing</direction>\n" +
            "            <caller>test-caller-number</caller>\n" +
            "            <callername>test-caller-name</callername>\n" +
            "            <called>test-called-number</called>\n" +
            "            <calledname>test-called-name</calledname>\n" +
            "            <timestamp>2017-10-09 09:07:00.0</timestamp>\n" +
            "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
            "            <duration>60000</duration>\n" +
            "            <tsc>component42.test-domain</tsc>\n" +
            "          </call>\n" +
            "        </callhistory>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String CALL_HISTORY_RESULT_WITH_MISMATCHED_TIMES = "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' status='completed'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
            "      <out>\n" +
            "        <callhistory xmlns='http://xmpp.org/protocol/openlink:01:00:00/callhistory' count='1' start='0' total='2'>\n" +
            "          <call>\n" +
            "            <id>test-call-id</id>\n" +
            "            <profile>test-user-id</profile>\n" +
            "            <interest>test-interest-id</interest>\n" +
            "            <state>CallEstablished</state>\n" +
            "            <direction>Outgoing</direction>\n" +
            "            <caller>test-caller-number</caller>\n" +
            "            <callername>test-caller-name</callername>\n" +
            "            <called>test-called-number</called>\n" +
            "            <calledname>test-called-name</calledname>\n" +
            "            <starttime>2011-12-13T14:15:16.178Z</starttime>\n" +
            "            <timestamp>2017-10-09 09:07:00.0</timestamp>\n" +
            "            <duration>60000</duration>\n" +
            "            <tsc>component42.test-domain</tsc>\n" +
            "          </call>\n" +
            "        </callhistory>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String CALL_HISTORY_RESULT_WITH_BAD_VALUES = "<iq type='get'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' status='completed'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
            "      <out>\n" +
            "        <callhistory xmlns='http://xmpp.org/protocol/openlink:01:00:00/callhistory' count='2'>\n" +
            "          <call>\n" +
            "            <timestamp>not-a-timestamp</timestamp>\n" +
            "            <starttime>not-a-starttime</starttime>\n" +
            "            <duration>not-a-duration</duration>\n" +
            "          </call>\n" +
            "        </callhistory>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String GET_CALL_HISTORY_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' action='execute'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in>\n" +
            "        <jid>" + CoreFixtures.USER_BARE_JID_STRING + "</jid>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    public static final String GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
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

    public static final String GET_CALL_HISTORY_REQUEST_WITH_BAD_VALUES = "<iq type='result'>\n" +
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

    public static final String GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in/>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static <J extends Serializable> HistoricalCall<J> getHistoricalCall(final J tsc) {
        return HistoricalCall.Builder.<J> start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ESTABLISHED)
                .setDirection(CallDirection.OUTGOING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .setTsc(tsc)
                .build();
    }
}
