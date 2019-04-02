package com.bt.openlink.tinder.iq;

import static com.bt.openlink.RequestActionFixtures.FEATURE_ID;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;

import org.junit.Test;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.MakeCallFeature;
import com.bt.openlink.type.RequestAction;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class RequestActionRequestTest {

    @Test
    public void canCreateAStanza() {

        final RequestActionRequest request = RequestActionRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getAction().get(), is(RequestAction.START_VOICE_DROP));
        assertThat(request.getCallId().get(), is(CoreFixtures.CALL_ID));
        assertThat(request.getValue1().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_1));
        assertThat(request.getValue2().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_2));
    }

    @Test
    public void willGenerateAnXMPPStanza() {

        final RequestActionRequest request = RequestActionRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .build();

        assertThat(request.toXML(), isIdenticalTo(RequestActionFixtures.REQUEST_ACTION_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final IQ stanza = Fixtures.iqFrom(RequestActionFixtures.REQUEST_ACTION_REQUEST);

        final RequestActionRequest request = OpenlinkIQParser.parse(stanza);

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getAction().get(), is(RequestAction.START_VOICE_DROP));
        assertThat(request.getCallId().get(), is(CoreFixtures.CALL_ID));
        assertThat(request.getValue1().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_1));
        assertThat(request.getValue2().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_2));
    }

    @Test
    public void willGenerateAnXMPPStanzaWithFeatures() {

        final RequestActionRequest request = RequestActionRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .addFeature(MakeCallFeature.Builder.start().setFeatureId(FEATURE_ID).setValue1(true).build())
                .build();

        assertThat(request.toXML(), isIdenticalTo(RequestActionFixtures.REQUEST_ACTION_REQUEST_WITH_FEATURES).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanzaWithFeatures() {

        final IQ stanza = Fixtures.iqFrom(RequestActionFixtures.REQUEST_ACTION_REQUEST_WITH_FEATURES);

        final RequestActionRequest request = OpenlinkIQParser.parse(stanza);

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getAction().get(), is(RequestAction.START_VOICE_DROP));
        assertThat(request.getCallId().get(), is(CoreFixtures.CALL_ID));
        assertThat(request.getValue1().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_1));
        assertThat(request.getValue2().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_2));
        assertThat(request.getFeatures().get(0).getFeatureId().get(), is(FEATURE_ID));
        assertThat(request.getFeatures().get(0).getBooleanValue1().get(), is(true));
    }

    @Test
    public void willReturnParsingErrors() {

        final IQ iq = Fixtures.iqFrom(RequestActionFixtures.REQUEST_ACTION_REQUEST_WITH_BAD_VALUES);

        final RequestActionRequest request = RequestActionRequest.from(iq);

        assertThat(request.getID(), is(nullValue()));
        assertThat(request.getTo(), is(nullValue()));
        assertThat(request.getFrom(), is(nullValue()));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getAction(), is(Optional.empty()));
        assertThat(request.getInterestId(), is(Optional.empty()));
        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid request-action stanza; missing 'interestId'",
                "Invalid request-action stanza; missing or invalid 'requestAction'",
                "Invalid request-action stanza; missing 'callId'"));
    }
}