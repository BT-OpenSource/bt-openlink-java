package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetInterestFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Interest;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetInterestResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() throws Exception {

        final GetInterestResult result = GetInterestResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterest(CoreFixtures.INTEREST)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getInterest().get(), is(CoreFixtures.INTEREST));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetInterestsResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final GetInterestResult result = GetInterestResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterest(CoreFixtures.INTEREST)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GetInterestFixtures.GET_INTEREST_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetInterestResult result = (GetInterestResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_RESULT));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        final Interest interest = result.getInterest().get();
        assertThat(interest.getId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(interest.getType().get(), is(CoreFixtures.INTEREST_TYPE));
        assertThat(interest.getLabel().get(), is("test interest label"));
        assertThat(interest.isDefaultInterest().get(), is(true));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetInterestResult result = GetInterestResult.from(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_RESULT_WITH_BAD_VALUES));

        assertThat(result.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-interest result stanza; missing 'interest'"));
    }

    @Test
    public void willBuildAResultFromARequest() throws Exception {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        final GetInterestResult result = GetInterestResult.Builder.start(request)
                .setInterest(CoreFixtures.INTEREST)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}