package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.SetFeaturesFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class SetFeaturesRequestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final SetFeaturesRequest request = SetFeaturesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1(true)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getFeatureId().get(), is(CoreFixtures.FEATURE_ID));
        assertThat(request.getValue1().get(), is("true"));
        assertThat(request.getValue2(), is(Optional.empty()));
        assertThat(request.getValue3(), is(Optional.empty()));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final SetFeaturesRequest request = SetFeaturesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1("SetLabel")
                .build();

        assertThat(request.toXML(), isIdenticalTo(SetFeaturesFixtures.SET_FEATURES_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final SetFeaturesRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(SetFeaturesFixtures.SET_FEATURES_REQUEST));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
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
    public void willReturnParsingErrors() {

        final SetFeaturesRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(SetFeaturesFixtures.SET_FEATURES_REQUEST_WITH_BAD_VALUES));

        assertThat(request.getParseErrors(), containsInAnyOrder(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: get",
                "Invalid set-features request stanza; missing featureId",
                "Invalid set-features request stanza; missing value1",
                "Invalid set-features request stanza; missing profileId"));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() {

        final SetFeaturesRequest request = OpenlinkIQParser.parse(Fixtures.iqFrom(SetFeaturesFixtures.SET_FEATURES_REQUEST_WITH_BAD_VALUES));

        assertThat(request.toXML(), isIdenticalTo(SetFeaturesFixtures.SET_FEATURES_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());

    }

}