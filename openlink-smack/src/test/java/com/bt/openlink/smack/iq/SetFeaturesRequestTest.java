package com.bt.openlink.smack.iq;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.SetFeaturesFixtures;
import com.bt.openlink.smack.Fixtures;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

@SuppressWarnings("ConstantConditions")
public class SetFeaturesRequestTest {

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

        final SetFeaturesRequest request = SetFeaturesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1(true)
                .build();

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getFeatureId().get(), is(CoreFixtures.FEATURE_ID));
        assertThat(request.getValue1().get(), is("true"));
        assertThat(request.getValue2(), is(Optional.empty()));
        assertThat(request.getValue3(), is(Optional.empty()));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final SetFeaturesRequest request = SetFeaturesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1("SetLabel")
                .build();

        System.out.println(request.toXML());
        assertThat(request.toXML().toString(), isIdenticalTo(SetFeaturesFixtures.SET_FEATURES_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final SetFeaturesRequest request = PacketParserUtils.parseStanza(SetFeaturesFixtures.SET_FEATURES_REQUEST);
        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(request.getFeatureId().get(), is(CoreFixtures.FEATURE_ID));
        assertThat(request.getValue1().get(), is("SetLabel"));
        assertThat(request.getValue2(), is(Optional.empty()));
        assertThat(request.getValue3(), is(Optional.empty()));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final SetFeaturesRequest request = PacketParserUtils.parseStanza(SetFeaturesFixtures.SET_FEATURES_REQUEST_WITH_BAD_VALUES);

        assertThat(request.getParseErrors(), contains(
                // "Invalid stanza; missing 'to' attribute is mandatory",
                //   "Invalid stanza; missing 'from' attribute is mandatory",
                //   "Invalid stanza; missing 'id' attribute is mandatory",
                //   "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid set-features request stanza; missing featureId",
                "Invalid set-features request stanza; missing value1",
                "Invalid set-features request stanza; missing profileId"
                ));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final SetFeaturesRequest request = PacketParserUtils.parseStanza(SetFeaturesFixtures.SET_FEATURES_REQUEST_WITH_BAD_VALUES);

        assertThat(request.toXML().toString(), isIdenticalTo(SetFeaturesFixtures.SET_FEATURES_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());

    }

}