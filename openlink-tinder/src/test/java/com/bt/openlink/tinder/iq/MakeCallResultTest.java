package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;

@SuppressWarnings({"ConstantConditions", "RedundantThrows"})
public class MakeCallResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canBuildAStanza() throws Exception {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();

        assertThat(result.getCalls(), contains(CoreFixtures.CALL_INCOMING_ORIGINATED));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCalls(Collections.singletonList(CoreFixtures.CALL_INCOMING_ORIGINATED))
                .build();

        assertThat(result.toXML(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_RESULT).ignoreWhitespace());
    }

    @Test
    public void willEnsureTheStanzaHasACall() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");
        MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final MakeCallResult result = (MakeCallResult) OpenlinkIQParser.parse(Fixtures.iqFrom(MakeCallFixtures.MAKE_CALL_RESULT));

        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.isCallStatusBusy().get(),is(false));
        final List<Call> calls = result.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(theOnlyCall.getConferenceId().get(), is(CoreFixtures.CONFERENCE_ID));
        assertThat(theOnlyCall.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(theOnlyCall.getUserId().get(), is(CoreFixtures.USER_ID));
        assertThat(theOnlyCall.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(theOnlyCall.getChanged().get(), is(Changed.STATE));
        assertThat(theOnlyCall.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(theOnlyCall.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(theOnlyCall.getCallerNumber().get(), is(CoreFixtures.CALLER_NUMBER));
        assertThat(theOnlyCall.getCallerName().get(), is(CoreFixtures.CALLER_NAME));
        assertThat(theOnlyCall.getCallerE164Numbers(), is(Collections.singletonList(CoreFixtures.CALLER_E164_NUMBER)));
        assertThat(theOnlyCall.getCalledNumber().get(), is(CoreFixtures.CALLED_NUMBER));
        assertThat(theOnlyCall.getCalledName().get(), is(CoreFixtures.CALLED_NAME));
        assertThat(theOnlyCall.getCalledDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
        assertThat(theOnlyCall.getCalledE164Numbers(), is(Collections.singletonList(CoreFixtures.CALLED_E164_NUMBER)));
        assertThat(theOnlyCall.getStartTime().get(), is(CoreFixtures.START_TIME));
        assertThat(theOnlyCall.getDuration().get(), is(Duration.ofMinutes(1)));
        assertThat(calls.size(), is(1));
        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final MakeCallResult result = MakeCallResult.from(Fixtures.iqFrom(MakeCallFixtures.MAKE_CALL_RESULT_WITH_BAD_VALUES));

        assertThat(result.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid make-call result stanza; missing or invalid calls"));
    }
}