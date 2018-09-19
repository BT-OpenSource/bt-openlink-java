package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.QueryFeaturesFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.ActiveFeature;

public class QueryFeaturesResultTest {

    @Test
    public void canCreateAStanza() {

        final QueryFeaturesResult result = QueryFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addFeature(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getFeatures(), contains(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final QueryFeaturesResult result = QueryFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addFeature(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE)
                .build();

        assertThat(result.toXML(), isIdenticalTo(QueryFeaturesFixtures.QUERY_FEATURES_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXMPPStanza() {
        final QueryFeaturesResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(QueryFeaturesFixtures.QUERY_FEATURES_RESULT));

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertReflectionEquals(Collections.singletonList(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE), result.getFeatures());
        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() {
        final QueryFeaturesResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(QueryFeaturesFixtures.QUERY_FEATURES_RESULT_WITH_BAD_VALUES));

        assertThat(result.getType(), is(IQ.Type.get));
        assertReflectionEquals(Collections.singletonList(ActiveFeature.Builder.start().build(new ArrayList<>())), result.getFeatures());
        System.out.println(result.getParseErrors());
        assertThat(result.getParseErrors(), containsInAnyOrder(
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: get",
                "Invalid query-features result; missing 'id' attribute is mandatory",
                "Invalid query-features result; missing 'type' attribute is mandatory",
                "Invalid query-features result; missing 'label' attribute is mandatory",
                "Invalid feature; missing feature id is mandatory",
                "Invalid feature; missing feature type is mandatory",
                "Invalid feature; missing feature label is mandatory"));
    }


    @Test
    public void willBuildAResultFromARequest() {

        final QueryFeaturesRequest request = QueryFeaturesRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        final QueryFeaturesResult result = QueryFeaturesResult.Builder.createResultBuilder(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }


}