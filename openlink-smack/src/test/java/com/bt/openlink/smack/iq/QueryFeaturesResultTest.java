package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.ArrayList;
import java.util.Collections;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.QueryFeaturesFixtures;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.ActiveFeature;

public class QueryFeaturesResultTest {

    @Before
    public void setUp() {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @After
    public void tearDown() {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() {

        final QueryFeaturesResult result = QueryFeaturesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addFeature(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
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

        assertThat(result.toXML().toString(), isIdenticalTo(QueryFeaturesFixtures.QUERY_FEATURES_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXMPPStanza() throws Exception {
        final QueryFeaturesResult result = PacketParserUtils.parseStanza(QueryFeaturesFixtures.QUERY_FEATURES_RESULT);

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertReflectionEquals(Collections.singletonList(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE), result.getFeatures());
        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {
        final QueryFeaturesResult result = PacketParserUtils.parseStanza(QueryFeaturesFixtures.QUERY_FEATURES_RESULT_WITH_BAD_VALUES);

        assertThat(result.getType(), is(IQ.Type.get));
        assertReflectionEquals(Collections.singletonList(ActiveFeature.Builder.start().build(new ArrayList<>())), result.getFeatures());
        assertThat(result.getParseErrors(), containsInAnyOrder(
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

        assertThat(result.getStanzaId(), is(request.getStanzaId()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }


}
