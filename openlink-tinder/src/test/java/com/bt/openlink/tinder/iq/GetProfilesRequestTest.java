package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetProfilesFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetProfilesRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetProfilesRequest.Builder.start()
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithoutAJID() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-profiles request 'jid' has not been set");
        GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_BARE_JID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GetProfilesFixtures.GET_PROFILES_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithARandomId() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_BARE_JID)
                .build();

        assertThat(request.getID(), is(not(nullValue())));
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfilesRequest request = (GetProfilesRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(GetProfilesFixtures.GET_PROFILES_REQUEST));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_BARE_JID));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final IQ iq = Fixtures.iqFrom(GetProfilesFixtures.GET_PROFILES_REQUEST_WITH_BAD_VALUES);

        final GetProfilesRequest request = GetProfilesRequest.from(iq);

        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-profiles request stanza; missing or invalid 'jid'"
        ));
        assertThat(request.getTo(), is(nullValue()));
        assertThat(request.getFrom(), is(nullValue()));
        assertThat(request.getID(), is(nullValue()));
        assertThat(request.getType(), is(IQ.Type.get));
        assertThat(request.getJID(), is(Optional.empty()));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final IQ iq = Fixtures.iqFrom(GetProfilesFixtures.GET_PROFILES_REQUEST_WITH_BAD_VALUES);

        final GetProfilesRequest request = GetProfilesRequest.from(iq);

        assertThat(request.toXML(), isIdenticalTo(iq.toXML()).ignoreWhitespace());
    }

}