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

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.CallFeature;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MakeCallResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canBuildAStanza() {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.toXML(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_RESULT).ignoreWhitespace());
    }

    @Test
    public void willEnsureTheStanzaHasACall() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");
        MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() {

        final MakeCallResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(MakeCallFixtures.MAKE_CALL_RESULT));

        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertReflectionEquals(CoreFixtures.CALL_STATUS, result.getCallStatus().get());
        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() {

        final MakeCallResult result = MakeCallResult.from(Fixtures.iqFrom(MakeCallFixtures.MAKE_CALL_RESULT_WITH_BAD_VALUES));

        assertThat(result.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: get",
                "Invalid make-call result stanza; missing or invalid callstatus"));
    }

    @Test
    public void willBuildAResultFromARequest() {

        final MakeCallRequest request = MakeCallRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        final MakeCallResult result = MakeCallResult.Builder.createResultBuilder(request)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

    @Test
    public void willParseLegacyStyleFeatures() {
        final MakeCallResult result = MakeCallResult.from(Fixtures.iqFrom("<iq from='" + CoreFixtures.FROM_JID_STRING + "' to='" + CoreFixtures.TO_JID_STRING + "' id='" + CoreFixtures.STANZA_ID + "' type='result'>\n" +
                "   <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#make-call' status='completed'>\n" +
                "     <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                "      <out>\n" +
                CoreFixtures.CALL_STATUS_LEGACY_FEATURES +
                "      </out>\n" +
                "    </iodata>\n" +
                "  </command>\n" +
                "</iq>"));

        final List<CallFeature> features = result.getCallStatus().get().getCalls().get(0).getFeatures();

        assertReflectionEquals(features, CoreFixtures.LEGACY_FEATURE_LIST);
    }


}