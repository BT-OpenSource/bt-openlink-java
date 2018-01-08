package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;

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
        assertThat(EqualsBuilder.reflectionEquals(CoreFixtures.CALL_INCOMING_ORIGINATED, theOnlyCall, false, null, true), is(true));
        assertThat(calls.size(), is(1));
        assertThat(result.getParseErrors().size(), is(0));
    }

}