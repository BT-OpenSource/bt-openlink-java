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
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;

@SuppressWarnings("ConstantConditions")
public class RequestActionResultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canBuildAStanza() {

        final RequestActionResult result = RequestActionResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .build();

        assertThat(result.getCalls(), contains(CoreFixtures.CALL_INCOMING_ORIGINATED));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final RequestActionResult result = RequestActionResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCalls(Collections.singletonList(CoreFixtures.CALL_INCOMING_ORIGINATED))
                .build();

        assertThat(result.toXML(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_RESULT).ignoreWhitespace());
    }

    @Test
    public void willEnsureTheStanzaHasACall() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");
        RequestActionResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() {

        final RequestActionResult result = (RequestActionResult) OpenlinkIQParser.parse(Fixtures.iqFrom(RequestActionFixtures.REQUEST_ACTION_RESULT));

        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.isCallStatusBusy().get(),is(false));
        final List<Call> calls = result.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(theOnlyCall.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
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

}