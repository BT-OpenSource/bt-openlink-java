package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetFeaturesResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String GET_FEATURES_RESULT = "<iq type=\"result\" id=\"" + CoreFixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-features\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <profile id=\"" + CoreFixtures.PROFILE_ID + "\"/>\n" +
            "        <features xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/features\">\n" +
            "          <feature id=\"hs_1\" type=\"Handset\" label=\"Handset 1\"/>\n" +
            "          <feature id=\"hs_2\" type=\"Handset\" label=\"Handset 2\"/>\n" +
            "          <feature id=\"priv_1\" type=\"Privacy\" label=\"Privacy\"/>\n" +
            "          <feature id=\"fwd_1\" type=\"CallForward\" label=\"Call Forward\"/>\n" +
            "        </features>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_FEATURES_RESULT_WITH_BAD_VALUES = "<iq type=\"get\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" action=\"execute\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-features\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

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
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetFeaturesResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final Feature hs1Feature = Feature.Builder.start()
                .setId(FeatureId.from("hs_1").get())
                .setType(FeatureType.HANDSET)
                .setLabel("Handset 1")
                .build();
        final Feature hs2Feature = Feature.Builder.start()
                .setId(FeatureId.from("hs_2").get())
                .setType(FeatureType.HANDSET)
                .setLabel("Handset 2")
                .build();
        final Feature privFeature = Feature.Builder.start()
                .setId(FeatureId.from("priv_1").get())
                .setType(FeatureType.PRIVACY)
                .setLabel("Privacy")
                .build();
        final Feature fwdFeature = Feature.Builder.start()
                .setId(FeatureId.from("fwd_1").get())
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("Call Forward")
                .build();
        final GetFeaturesResult result = GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(hs1Feature)
                .addFeature(hs2Feature)
                .addFeature(privFeature)
                .addFeature(fwdFeature)
                .build();

        //        System.out.println(stanza);
        //        System.out.println(GET_FEATURES_RESULT);
        assertThat(result.toXML(), isIdenticalTo(GET_FEATURES_RESULT).ignoreWhitespace());
    }

    @Test
    public void willNotBuildAPacketWithDuplicateFeatureIds() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each feature id must be unique - hs_1 appears more than once");
        final Feature hs1Feature = Feature.Builder.start()
                .setId(FeatureId.from("hs_1").get())
                .setType(FeatureType.HANDSET)
                .setLabel("Handset 1")
                .build();
        GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(hs1Feature)
                .addFeature(hs1Feature)
                .build();
    }

    @Test
    public void willNotBuildAPacketWithoutAProfileId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-features result profile has not been set");
        final Feature hs1Feature = Feature.Builder.start()
                .setId(FeatureId.from("hs_1").get())
                .setType(FeatureType.HANDSET)
                .setLabel("Handset 1")
                .build();
        GetFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addFeature(hs1Feature)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetFeaturesResult result = (GetFeaturesResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_FEATURES_RESULT));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        final List<Feature> features = result.getFeatures();
        assertThat(features.size(), is(4));
        assertThat(features.get(0).getId(), is(FeatureId.from("hs_1")));
        assertThat(features.get(0).getType().get(), is(FeatureType.HANDSET));
        assertThat(features.get(0).getLabel().get(), is("Handset 1"));

        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetFeaturesResult result = GetFeaturesResult.from(Fixtures.iqFrom(GET_FEATURES_RESULT_WITH_BAD_VALUES));

        System.out.println(result.getParseErrors());

        assertThat(result.getParseErrors(), contains(
                "Invalid get-features result; missing 'features' element is mandatory",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-features result stanza; missing profile"));
    }

    @Test
    public void willBuildAResultFromARequest() throws Exception {

        final GetFeaturesRequest request = GetFeaturesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        final GetFeaturesResult result = GetFeaturesResult.Builder.start(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}