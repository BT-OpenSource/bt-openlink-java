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
import com.bt.openlink.GetCallHistoryFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.CallType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetCallHistoryRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID).build();

        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
        assertThat(request.getCaller(), is(Optional.empty()));
        assertThat(request.getCalled(), is(Optional.empty()));
        assertThat(request.getCallType(), is(Optional.empty()));
        assertThat(request.getFromDate(), is(Optional.empty()));
        assertThat(request.getUpToDate(), is(Optional.empty()));
        assertThat(request.getStart(), is(Optional.empty()));
        assertThat(request.getCount(), is(Optional.empty()));
    }

    @Test
    public void canCreateAStanzaWithoutAJID() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_BARE_JID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithAllFields() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_BARE_JID)
                .setCaller("from-caller")
                .setCalled("to-destination")
                .setCallType(CallType.MISSED)
                .setFromDate(LocalDate.of(2016, 6, 1))
                .setUpToDate(LocalDate.of(2016, 6, 29))
                .setStart(1L)
                .setCount(50L)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final GetCallHistoryRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST));
        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_BARE_JID));
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
    public void willParseAnXmppStanzaWithAnEmptyForJid() {

        final GetCallHistoryRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS));
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
    public void willParseAnXmppStanzaWithAllFields() {

        final GetCallHistoryRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS));

        assertThat(request.getID(), CoreMatchers.is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), CoreMatchers.is(Fixtures.TO_JID));
        assertThat(request.getFrom(), CoreMatchers.is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_BARE_JID));
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
    public void willReturnParsingErrors() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.from(Fixtures.iqFrom(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_WITH_BAD_VALUES));

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
    public void shouldRoundTripAStanza() {

        final IQ originalStanza = Fixtures.iqFrom(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST);
        final GetCallHistoryRequest request = OpenlinkIQParser.parse(originalStanza);

        assertThat(request.toXML(), isIdenticalTo(originalStanza.toXML()).ignoreWhitespace());
    }

}
