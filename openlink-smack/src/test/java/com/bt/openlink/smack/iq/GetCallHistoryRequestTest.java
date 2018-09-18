package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.LocalDate;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetCallHistoryFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.CallType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetCallHistoryRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID).build();

        assertThat(request.getStanzaId(), CoreMatchers.is(CoreFixtures.STANZA_ID));
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

        assertThat(request.toXML().toString(), isIdenticalTo(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetCallHistoryRequest request = GetCallHistoryRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_BARE_JID)
                .build();

        assertThat(request.toXML().toString(), isIdenticalTo(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST).ignoreWhitespace());
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

        assertThat(request.toXML().toString(), isIdenticalTo(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetCallHistoryRequest request = PacketParserUtils.parseStanza(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST);
        assertThat(request.getStanzaId(), CoreMatchers.is(CoreFixtures.STANZA_ID));
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
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willParseAnXmppStanzaWithAnEmptyForJid() throws Exception {

        final GetCallHistoryRequest request = PacketParserUtils.parseStanza(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_FOR_ALL_USERS);
        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
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

        final GetCallHistoryRequest request = PacketParserUtils.parseStanza(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_WITH_ALL_FIELDS);

        assertThat(request.getStanzaId(), CoreMatchers.is(CoreFixtures.STANZA_ID));
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
    public void willReturnParsingErrors() throws Exception {

        final GetCallHistoryRequest request = PacketParserUtils.parseStanza(GetCallHistoryFixtures.GET_CALL_HISTORY_REQUEST_WITH_BAD_VALUES);

        assertThat(request.getParseErrors(), contains(
                "Invalid get-call-history request; invalid calltype - 'not-a-call-type' should be 'in', 'out' or 'missed'",
                "Invalid get-call-history request; invalid fromdate 'not-a-from-date'; date format is 'MM/dd/yyyy'",
                "Invalid get-call-history request; invalid uptodate 'not-a-to-date'; date format is 'MM/dd/yyyy'",
                "Invalid get-call-history request; invalid start 'not-a-start-number'; please supply an integer",
                "Invalid get-call-history request; invalid count 'not-a-count-number'; please supply an integer"
        ));
    }

}