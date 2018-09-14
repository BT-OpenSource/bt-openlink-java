package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetInterestFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetInterestRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GetInterestFixtures.GET_INTEREST_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithARandomId() {

        final GetInterestsRequest request = GetInterestsRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.getID(), is(not(nullValue())));
    }

    @Test
    public void willParseAnXmppStanza() {

        final GetInterestRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_REQUEST));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() {

        final GetInterestRequest request = GetInterestRequest.from(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_REQUEST_WITH_BAD_VALUES));

        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-interest request stanza; missing 'interest'"));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() {

        final GetInterestRequest request = GetInterestRequest.from(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_REQUEST_WITH_BAD_VALUES));

        assertThat(request.toXML(), isIdenticalTo(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_REQUEST_WITH_BAD_VALUES).toXML()).ignoreWhitespace());

    }

}