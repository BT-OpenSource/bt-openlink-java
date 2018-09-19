package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.CallHistoryFixtures;
import com.bt.openlink.CoreFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.HistoricalCall;

public class GetCallHistoryResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();
    private TimeZone defaultTimeZone;

    @Before
    public void setUp() {
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    }

    @After
    public void tearDown() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void canCreateAStanza() {

        final GetCallHistoryResult result = GetCallHistoryResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setFirstRecordNumber(0)
                .setRecordCountInBatch(1)
                .setTotalRecordCount(2)
                .addCall(CallHistoryFixtures.getHistoricalCall(new JID(CoreFixtures.TSC)))
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getFirstRecordNumber(), is(Optional.of(0L)));
        assertThat(result.getRecordCountInBatch(), is(Optional.of(1L)));
        assertThat(result.getTotalRecordCount(), is(Optional.of(2L)));
        assertReflectionEquals(Collections.singletonList(CallHistoryFixtures.getHistoricalCall(new JID(CoreFixtures.TSC))), result.getCalls());
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetCallHistoryResult result = GetCallHistoryResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setFirstRecordNumber(0)
                .setRecordCountInBatch(1)
                .setTotalRecordCount(2)
                .addCall(CallHistoryFixtures.getHistoricalCall(new JID(CoreFixtures.TSC)))
                .build();

        assertThat(result.toXML(), isIdenticalTo(CallHistoryFixtures.CALL_HISTORY_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXMPPStanza() {
        final GetCallHistoryResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(CallHistoryFixtures.CALL_HISTORY_RESULT));

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getFirstRecordNumber(), is(Optional.of(0L)));
        assertThat(result.getRecordCountInBatch(), is(Optional.of(1L)));
        assertThat(result.getTotalRecordCount(), is(Optional.of(2L)));
        assertReflectionEquals(Collections.singletonList(CallHistoryFixtures.getHistoricalCall(new JID(CoreFixtures.TSC))), result.getCalls());
        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() {
        final GetCallHistoryResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(CallHistoryFixtures.CALL_HISTORY_RESULT_WITH_BAD_VALUES));

        assertThat(result.getType(), is(IQ.Type.get));
        assertThat(result.getFirstRecordNumber(), is(Optional.empty()));
        assertThat(result.getRecordCountInBatch(), is(Optional.of(2L)));
        assertThat(result.getTotalRecordCount(), is(Optional.empty()));
        assertReflectionEquals(Collections.singletonList(HistoricalCall.Builder.start().build(new ArrayList<>())), result.getCalls());
        assertThat(result.getParseErrors(), contains(
                "Invalid get-call-history result; invalid duration 'not-a-duration'; please supply an integer",
                "Invalid get-call-history result; invalid timestamp 'not-a-timestamp'; please supply a valid timestamp",
                "Invalid get-call-history result; invalid starttime 'not-a-starttime'; please supply a valid starttime",
                "Invalid historical call; missing call id is mandatory",
                "Invalid historical call; missing profile id is mandatory",
                "Invalid historical call; missing interest id is mandatory",
                "Invalid historical call; missing state is mandatory",
                "Invalid historical call; missing direction is mandatory",
                "Invalid historical call; missing caller number is mandatory",
                "Invalid historical call; missing caller name is mandatory",
                "Invalid historical call; missing called number is mandatory",
                "Invalid historical call; missing called name is mandatory",
                "Invalid historical call; missing start time is mandatory",
                "Invalid historical call; missing duration is mandatory",
                "Invalid historical call; missing TSC is mandatory",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: get",
                "Invalid call history; missing or invalid total record count",
                "Invalid call history; missing or invalid first record number",
                "Invalid call history; incorrect batch record count"));
    }

    @Test
    public void willPreferStartTimeOverTimestamp() {

        final GetCallHistoryResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(CallHistoryFixtures.CALL_HISTORY_RESULT_WITH_MISMATCHED_TIMES));

        assertThat(result.getCalls().get(0).getStartTime(), is(Optional.of(Instant.parse("2011-12-13T14:15:16.178Z"))));
    }

}