package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetFeaturesFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Feature;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetFeaturesResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final GetFeaturesResult result = GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetFeaturesResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetFeaturesResult result = GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_2)
                .addFeature(GetFeaturesFixtures.FEATURE_PRIVACY)
                .addFeature(GetFeaturesFixtures.FEATURE_CALL_FORWARD)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GetFeaturesFixtures.GET_FEATURES_RESULT).ignoreWhitespace());
    }

    @Test
    public void willNotBuildAPacketWithDuplicateFeatureIds() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each feature id must be unique - hs_1 appears more than once");
        GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1)
                .build();
    }

    @Test
    public void willNotBuildAPacketWithoutAProfileId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-features result profile has not been set");
        GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() {

        final GetFeaturesResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(GetFeaturesFixtures.GET_FEATURES_RESULT));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        final List<Feature> features = result.getFeatures();

        assertReflectionEquals(GetFeaturesFixtures.FEATURE_HANDSET_1, features.get(0));
        assertReflectionEquals(GetFeaturesFixtures.FEATURE_HANDSET_2, features.get(1));
        assertReflectionEquals(GetFeaturesFixtures.FEATURE_PRIVACY, features.get(2));
        assertReflectionEquals(GetFeaturesFixtures.FEATURE_CALL_FORWARD, features.get(3));

        assertThat(features.size(), is(4));
        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() {

        final GetFeaturesResult result = GetFeaturesResult.from(Fixtures.iqFrom(GetFeaturesFixtures.GET_FEATURES_RESULT_WITH_BAD_VALUES));

        System.out.println(result.getParseErrors());

        assertThat(result.getParseErrors(), contains(
                "Invalid get-features result; missing 'features' element is mandatory",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: get",
                "Invalid get-features result stanza; missing profile"));
    }

    @Test
    public void willBuildAResultFromARequest() {

        final GetFeaturesRequest request = GetFeaturesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        final GetFeaturesResult result = GetFeaturesResult.Builder.createResultBuilder(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}