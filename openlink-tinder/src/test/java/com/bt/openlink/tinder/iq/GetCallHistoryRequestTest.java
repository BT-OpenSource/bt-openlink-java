package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.LocalDate;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.CallType;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetCallHistoryRequestTest {

    private static final String GET_CALL_HISTORY_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' action='execute'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in>\n" +
            "        <jid>" + Fixtures.USER_JID + "</jid>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history' action='execute'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in>\n" +
            "        <jid>" + Fixtures.USER_JID + "</jid>\n" +
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

    private static final String GET_CALL_HISTORY_REQUEST_WITH_BAD_VALUES = "<iq type='result'>\n" +
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
    private static final String GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-call-history'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in/>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() throws Exception {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_JID).build();

        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_JID));
        assertThat(request.getCaller(), is(Optional.empty()));
        assertThat(request.getCalled(), is(Optional.empty()));
        assertThat(request.getCallType(), is(Optional.empty()));
        assertThat(request.getFromDate(), is(Optional.empty()));
        assertThat(request.getUpToDate(), is(Optional.empty()));
        assertThat(request.getStart(), is(Optional.empty()));
        assertThat(request.getCount(), is(Optional.empty()));
    }

    @Test
    public void canCreateAStanzaWithoutAJID() throws Exception {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_JID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GET_CALL_HISTORY_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithAllFields() throws Exception {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_JID)
                .setCaller("from-caller")
                .setCalled("to-destination")
                .setCallType(CallType.MISSED)
                .setFromDate(LocalDate.of(2016, 6, 1))
                .setUpToDate(LocalDate.of(2016, 6, 29))
                .setStart(1L)
                .setCount(50L)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetCallHistoryRequest request = (GetCallHistoryRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_CALL_HISTORY_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_JID));
        assertThat(request.getCaller(), is(Optional.empty()));
        assertThat(request.getCalled(), is(Optional.empty()));
        assertThat(request.getCallType(), is(Optional.empty()));
        assertThat(request.getFromDate(), is(Optional.empty()));
        assertThat(request.getUpToDate(), is(Optional.empty()));
        assertThat(request.getStart(), is(Optional.empty()));
        assertThat(request.getCount(), is(Optional.empty()));
        assertThat(request.getParseErrors().size(), is(0));
    }

    @Test
    public void willParseAnXmppStanzaWithAnEmptyForJid() throws Exception {

        final GetCallHistoryRequest request = (GetCallHistoryRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID(), is(Optional.empty()));
        assertThat(request.getCaller(), is(Optional.empty()));
        assertThat(request.getCalled(), is(Optional.empty()));
        assertThat(request.getCallType(), is(Optional.empty()));
        assertThat(request.getFromDate(), is(Optional.empty()));
        assertThat(request.getUpToDate(), is(Optional.empty()));
        assertThat(request.getStart(), is(Optional.empty()));
        assertThat(request.getCount(), is(Optional.empty()));
        assertThat(request.getParseErrors().size(), is(0));
    }

    @Test
    public void willParseAnXmppStanzaWithAllFields() throws Exception {

        final GetCallHistoryRequest request = (GetCallHistoryRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS));

        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_JID));
        assertThat(request.getCaller().get(), is("from-caller"));
        assertThat(request.getCalled().get(), is("to-destination"));
        assertThat(request.getCallType().get(), is(CallType.MISSED));
        assertThat(request.getFromDate().get(), is(LocalDate.of(2016, 6, 1)));
        assertThat(request.getUpToDate().get(), is(LocalDate.of(2016, 6, 29)));
        assertThat(request.getStart().get(), is(1L));
        assertThat(request.getCount().get(), is(50L));
        assertThat(request.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetCallHistoryRequest request = GetCallHistoryRequest.from(Fixtures.iqFrom(GET_CALL_HISTORY_REQUEST_WITH_BAD_VALUES));

        assertThat(request.getParseErrors(), contains(
                "Invalid get-call-history request; invalid calltype - 'not-a-call-type' should be 'in', 'out' or 'missed'",
                "Invalid get-call-history request; invalid fromdate 'not-a-from-date'; date format is 'MM/dd/yyyy'",
                "Invalid get-call-history request; invalid uptodate 'not-a-to-date'; date format is 'MM/dd/yyyy'",
                "Invalid get-call-history request; invalid start 'not-a-start-number'; please supply an integer",
                "Invalid get-call-history request; invalid count 'not-a-count-number'; please supply an integer",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute"
                ));
    }

    @Test
    public void shouldRoundTripAStanza() throws Exception {

        final IQ originalStanza = Fixtures.iqFrom(GET_CALL_HISTORY_REQUEST);
        final GetCallHistoryRequest request = (GetCallHistoryRequest) OpenlinkIQParser.parse(originalStanza);

        assertThat(request.toXML(), isIdenticalTo(originalStanza.toXML()).ignoreWhitespace());
    }

}
