package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetProfileFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.DeviceId;
import com.bt.openlink.type.Key;
import com.bt.openlink.type.KeyColor;
import com.bt.openlink.type.KeyFunction;
import com.bt.openlink.type.KeyId;
import com.bt.openlink.type.KeyInterest;
import com.bt.openlink.type.KeyLabel;
import com.bt.openlink.type.KeyModifier;
import com.bt.openlink.type.KeyPage;
import com.bt.openlink.type.KeyPageId;
import com.bt.openlink.type.KeyPageLabel;
import com.bt.openlink.type.KeyPageLocalKeyPage;
import com.bt.openlink.type.KeyPageModule;
import com.bt.openlink.type.KeyQualifier;
import com.bt.openlink.type.Profile;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetProfileResultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

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

        final GetProfileResult result = GetProfileResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfile(CoreFixtures.PROFILE)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getProfile().get(), is(CoreFixtures.PROFILE));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetProfileResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetProfileResult result = GetProfileResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfile(CoreFixtures.KEYPAGE_PROFILE)
                .build();

        assertThat(result.toXML().toString(), isIdenticalTo(GetProfileFixtures.GET_PROFILE_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfileResult result = PacketParserUtils.parseStanza(GetProfileFixtures.GET_PROFILE_RESULT);
        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        final Profile profile = result.getProfile().get();
        assertReflectionEquals(CoreFixtures.KEYPAGE_PROFILE, profile);
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetProfileResult result = PacketParserUtils.parseStanza(GetProfileFixtures.GET_PROFILE_RESULT_WITH_BAD_VALUES);

        assertThat(result.getParseErrors(), containsInAnyOrder(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid get-profile result stanza; missing 'profile'"));
    }

    @Test
    public void willBuildAResultFromARequest() {

        final GetProfileRequest request = GetProfileRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        final GetProfileResult result = GetProfileResult.Builder.createResultBuilder(request)
                .setProfile(CoreFixtures.PROFILE)
                .build();

        assertThat(result.getStanzaId(), is(request.getStanzaId()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }
}