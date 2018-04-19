package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("ConstantConditions")
public class RequestActionResultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canBuildAStanza() {

        final RequestActionResult result = RequestActionResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final RequestActionResult result = RequestActionResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.toXML(), isIdenticalTo(RequestActionFixtures.REQUEST_ACTION_RESULT).ignoreWhitespace());
    }

    @Test
    public void willEnsureTheStanzaHasACall() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action result has no calls");
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
        assertThat(EqualsBuilder.reflectionEquals(CoreFixtures.CALL_STATUS, result.getCallStatus().get(), false, null, true), is(true));
        assertThat(result.getParseErrors().size(), is(0));
    }

}