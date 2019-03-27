package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
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
import com.bt.openlink.GetProfileFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;

public class GetProfileRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

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

        final GetProfileRequest request = GetProfileRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final GetProfileRequest request = GetProfileRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.toXML().toString(), isIdenticalTo(GetProfileFixtures.GET_PROFILE_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithARandomId() throws Exception {

        final GetProfileRequest request = GetProfileRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.getStanzaId(), is(not(nullValue())));
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfileRequest request = PacketParserUtils.parseStanza(GetProfileFixtures.GET_PROFILE_REQUEST);
        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final GetProfileRequest request = PacketParserUtils.parseStanza(GetProfileFixtures.GET_PROFILE_REQUEST_WITH_BAD_VALUES);
        assertThat(request.toXML().toString(), isIdenticalTo(GetProfileFixtures.GET_PROFILE_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());

    }
}