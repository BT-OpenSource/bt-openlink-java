package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.TestStanzas;
import com.bt.openlink.smack.Fixtures;

@SuppressWarnings("ConstantConditions")
public class GetProfilesRequestTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start().setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetProfilesRequest.Builder.start()
                .setJID(Fixtures.USER_FULL_JID)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithoutAJid() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-profiles request 'jid' has not been set");
        GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start().setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_BARE_JID)
                .build();

        assertThat(request.toXML().toString(), isIdenticalTo(TestStanzas.GET_PROFILES_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithARandomId() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        assertThat(request.getStanzaId(), is(not(nullValue())));

    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfilesRequest request = PacketParserUtils.parseStanza(TestStanzas.GET_PROFILES_REQUEST);

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_BARE_JID));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetProfilesRequest request = PacketParserUtils.parseStanza(TestStanzas.GET_PROFILES_REQUEST_WITH_BAD_VALUES);

        // Note; it's not possible to validate the core elements of Smack packets as the to/from/id/type are not
        // set until after the parsing is complete.

        assertThat(request.getParseErrors(), contains(
//                "Invalid stanza; missing 'id' attribute is mandatory",
//                "Invalid stanza; missing 'to' attribute is mandatory",
//                "Invalid stanza; missing 'from' attribute is mandatory",
//                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-profiles request stanza; missing or invalid 'jid'"
        ));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final GetProfilesRequest request = PacketParserUtils.parseStanza(TestStanzas.GET_PROFILES_REQUEST_WITH_BAD_VALUES);

        assertThat(request.toXML().toString(), isIdenticalTo(TestStanzas.GET_PROFILES_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());

    }

}