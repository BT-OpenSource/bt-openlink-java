package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.core.Is.is;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.ManageVoiceMessageFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ManageVoiceMessageRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

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

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getAction().get(), is(ManageVoiceMessageAction.CREATE));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(request.getLabel().get(), is(ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL));
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

        assertThat(request.toXML(), isIdenticalTo(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final ManageVoiceMessageRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST));

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getAction().get(), is(ManageVoiceMessageAction.PLAYBACK));
        assertThat(request.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(request.getLabel(), is(java.util.Optional.empty()));
        assertThat(request.getFeatures(), contains(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() {

        final ManageVoiceMessageRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES));

        assertThat(request.getID(), is(nullValue()));
        assertThat(request.getTo(), is(nullValue()));
        assertThat(request.getFrom(), is(nullValue()));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getProfileId(), is(Optional.empty()));
        assertThat(request.getAction(), is(Optional.empty()));
        assertThat(request.getLabel(), is(Optional.empty()));
        assertThat(request.getFeatures(), is(emptyCollectionOf(FeatureId.class)));
        assertThat(request.getParseErrors(), contains(
                //  "Invalid stanza; missing 'to' attribute is mandatory",
                //  "Invalid stanza; missing 'from' attribute is mandatory",
                //  "Invalid stanza; missing 'id' attribute is mandatory",
                // "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid manage-voice-message request stanza; missing 'profile'",
                "Invalid manage-voice-message request stanza; missing 'action'"));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() {

        final ManageVoiceMessageRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES));

        assertThat(request.toXML(), isIdenticalTo(ManageVoiceMessageFixtures.MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());
    }

}