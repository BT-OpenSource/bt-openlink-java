package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.SetFeaturesFixtures;
import com.bt.openlink.tinder.Fixtures;

public class SetFeaturesResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final SetFeaturesResult result = SetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final SetFeaturesResult result = SetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.toXML(), isIdenticalTo(SetFeaturesFixtures.SET_FEATURES_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final SetFeaturesResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(SetFeaturesFixtures.SET_FEATURES_RESULT));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getType(), is(IQ.Type.result));

        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willBuildAResultFromARequest() {

        final SetFeaturesRequest request = SetFeaturesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1(true)
                .build();

        final SetFeaturesResult result = SetFeaturesResult.Builder.createResultBuilder(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }




}