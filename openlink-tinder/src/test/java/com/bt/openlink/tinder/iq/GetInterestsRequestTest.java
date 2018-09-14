package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetInterestsFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetInterestsRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final GetInterestsRequest request = GetInterestsRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAProfileId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-interests request 'profileId' has not been set");
        GetInterestsRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetInterestsRequest request = GetInterestsRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GetInterestsFixtures.GET_INTERESTS_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final GetInterestsRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(GetInterestsFixtures.GET_INTERESTS_REQUEST));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() {

        final IQ iq = Fixtures.iqFrom(GetInterestsFixtures.GET_INTERESTS_REQUEST_WITH_BAD_VALUES);

        final GetInterestsRequest request = GetInterestsRequest.from(iq);

        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-interests request stanza; missing 'profile'"));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() {

        final IQ iq = Fixtures.iqFrom(GetInterestsFixtures.GET_INTERESTS_REQUEST_WITH_BAD_VALUES);

        final GetInterestsRequest request = GetInterestsRequest.from(iq);

        assertThat(request.toXML(), isIdenticalTo(iq.toXML()).ignoreWhitespace());
    }

}