package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetProfilesFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.Profile;

@SuppressWarnings({ "ConstantConditions" })
public class GetProfilesResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(CoreFixtures.PROFILE)
                .addProfile(GetProfilesFixtures.PROFILE_2)
                .build();

        assertThat(result.toXML().toString(), isIdenticalTo(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_NO_NOTES).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfilesResult result = PacketParserUtils.parseStanza(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_NO_NOTES);
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getType(), is(IQ.Type.result));
        final List<Profile> profiles = result.getProfiles();

        assertReflectionEquals(CoreFixtures.PROFILE, profiles.get(0));
        assertReflectionEquals(GetProfilesFixtures.PROFILE_2, profiles.get(1));

        assertThat(profiles.size(), is(2));

        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetProfilesResult result = PacketParserUtils.parseStanza(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_BAD_VALUES);

        System.out.println(result.getParseErrors());

        assertThat(result.getParseErrors(), contains("Invalid get-profiles result; no profiles present"));
    }

    @Test
    public void willReturnANoProfilesParsingError() throws Exception {

        final GetProfilesResult result = PacketParserUtils.parseStanza(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_NO_PROFILES);

        assertThat(result.getParseErrors(), contains("Invalid get-profiles result; no profiles present"));
    }

    @Test
    public void willBuildAResultFromARequest() {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        final GetProfilesResult result = GetProfilesResult.Builder.createResultBuilder(request)
                .build();

        assertThat(result.getStanzaId(), is(request.getStanzaId()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}