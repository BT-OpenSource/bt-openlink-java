package com.bt.openlink.smack.iq;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.ManageVoiceMessageFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ManageVoiceMessageRequestTest {

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

        final ManageVoiceMessageRequest request = ManageVoiceMessageRequest.Builder.start().setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setAction(ManageVoiceMessageAction.CREATE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE)
                .setLabel(ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL)
                .build();

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getAction().get(), CoreMatchers.is(ManageVoiceMessageAction.CREATE));
        assertThat(request.getProfileId().get(), CoreMatchers.is(CoreFixtures.PROFILE_ID));
        assertThat(request.getLabel().get(), CoreMatchers.is(ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL));
        assertThat(request.getFeatures(), contains(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE));
    }


    @Test
    public void willGenerateAnXmppStanza() {

        final ManageVoiceMessageRequest request = ManageVoiceMessageRequest.Builder.start().setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setAction(ManageVoiceMessageAction.PLAYBACK)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE)
                .build();

        Assert.assertThat(request.toXML().toString(), isIdenticalTo(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final ManageVoiceMessageRequest request = PacketParserUtils.parseStanza(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST);

        Assert.assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        Assert.assertThat(request.getTo(), is(Fixtures.TO_JID));
        Assert.assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        Assert.assertThat(request.getType(), is(IQ.Type.set));
        Assert.assertThat(request.getAction().get(), CoreMatchers.is(ManageVoiceMessageAction.PLAYBACK));
        Assert.assertThat(request.getProfileId().get(), CoreMatchers.is(CoreFixtures.PROFILE_ID));
        Assert.assertThat(request.getLabel(), is(java.util.Optional.empty()));
        Assert.assertThat(request.getFeatures(), contains(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE));
        Assert.assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final ManageVoiceMessageRequest request = PacketParserUtils.parseStanza(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES);

        Assert.assertThat(request.getStanzaId(), is(nullValue()));
        Assert.assertThat(request.getTo(), is(nullValue()));
        Assert.assertThat(request.getFrom(), is(nullValue()));
        Assert.assertThat(request.getType(), is(IQ.Type.set));
        Assert.assertThat(request.getProfileId(), is(Optional.empty()));
        Assert.assertThat(request.getAction(), is(Optional.empty()));
        Assert.assertThat(request.getLabel(), is(Optional.empty()));
        Assert.assertThat(request.getFeatures(), is(emptyCollectionOf(FeatureId.class)));
        Assert.assertThat(request.getParseErrors(), contains(
                //  "Invalid stanza; missing 'to' attribute is mandatory",
                //  "Invalid stanza; missing 'from' attribute is mandatory",
                //  "Invalid stanza; missing 'id' attribute is mandatory",
                // "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid manage-voice-message request stanza; missing 'profile'",
                "Invalid manage-voice-message request stanza; missing 'action'"));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final ManageVoiceMessageRequest request = PacketParserUtils.parseStanza(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES);

        Assert.assertThat(request.toXML().toString(), isIdenticalTo(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());
    }

}