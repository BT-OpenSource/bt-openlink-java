package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;
import java.util.Optional;

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
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

@SuppressWarnings({ "ConstantConditions" })
public class GetProfilesResultTest {

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
    public void willGenerateAnXmppStanza() throws Exception {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(GetProfilesFixtures.PROFILE_1)
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
        int i = 0;

        Profile profile = profiles.get(i++);
        Site site = profile.getSite().get();
        assertThat(profile.getId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(profile.isDefaultProfile().get(), is(true));
        assertThat(profile.getDevice().get(), is("uta"));
        assertThat(profile.getLabel().get(), is("7001"));
        assertThat(profile.isOnline().get(), is(true));
        assertThat(profile.getActions(), contains(RequestAction.ANSWER_CALL, RequestAction.CLEAR_CALL));
        assertThat(site.getId().get(), is(42L));
        assertThat(site.getType().get(), is(Site.Type.BTSM));
        assertThat(site.isDefault().get(), is(true));
        assertThat(site.getName().get(), is("test site name"));

        profile = profiles.get(i++);
        site = profile.getSite().get();
        assertThat(profile.getId().get(), is(ProfileId.from("test-profile-id-2").get()));
        assertThat(profile.isDefaultProfile().get(), is(true));
        assertThat(profile.getDevice().get(), is("uta"));
        assertThat(profile.getLabel().get(), is("7001"));
        assertThat(profile.isOnline().get(), is(true));
        assertThat(profile.getActions(), contains(RequestAction.ANSWER_CALL, RequestAction.CLEAR_CALL));
        assertThat(site.getId().get(), is(11L));
        assertThat(site.getType().get(), is(Site.Type.ITS));
        assertThat(site.isDefault(), is(Optional.empty()));
        assertThat(site.getName().get(), is("another-test-site-name"));

        assertThat(profiles.size(), is(i));

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
    public void willBuildAResultFromARequest() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        final GetProfilesResult result = GetProfilesResult.Builder.start(request)
                .build();

        assertThat(result.getStanzaId(), is(request.getStanzaId()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}