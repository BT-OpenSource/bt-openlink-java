package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetProfileFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.DeviceId;
import com.bt.openlink.type.Key;
import com.bt.openlink.type.KeyColor;
import com.bt.openlink.type.KeyFunction;
import com.bt.openlink.type.KeyId;
import com.bt.openlink.type.KeyInterest;
import com.bt.openlink.type.KeyLabel;
import com.bt.openlink.type.KeyModifier;
import com.bt.openlink.type.KeyPage;
import com.bt.openlink.type.KeyPageId;
import com.bt.openlink.type.KeyPageLabel;
import com.bt.openlink.type.KeyPageLocalKeyPage;
import com.bt.openlink.type.KeyPageModule;
import com.bt.openlink.type.KeyQualifier;
import com.bt.openlink.type.Profile;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GetProfileResultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final GetProfileResult result = GetProfileResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfile(CoreFixtures.PROFILE)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getProfile().get(), is(CoreFixtures.PROFILE));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetProfileResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetProfileResult result = GetProfileResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfile(CoreFixtures.KEYPAGE_PROFILE)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GetProfileFixtures.GET_PROFILE_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final GetProfileResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(GetProfileFixtures.GET_PROFILE_RESULT));

        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        final Profile profile = result.getProfile().get();
        assertReflectionEquals(CoreFixtures.KEYPAGE_PROFILE, profile);
    }

    @Test
    public void willParseAnXmppStanzaWithNoKeyPages() {

        final GetProfileResult result = OpenlinkIQParser.parse(Fixtures.iqFrom(GetProfileFixtures.GET_PROFILE_RESULT_WITH_NO_KEYPAGES));

        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        final Profile profile = result.getProfile().get();
        assertThat(profile.getKeyPages(),is(Collections.emptyList()));
        assertThat(result.getParseErrors(),is(Collections.emptyList()));
    }

    @Test
    public void willGenerateAnXmppStanzaWithNoKeyPages() {

        final GetProfileResult result = GetProfileResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfile(CoreFixtures.KEYPAGE_PROFILE_NO_KEYPAGES)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GetProfileFixtures.GET_PROFILE_RESULT_WITH_NO_KEYPAGES).ignoreWhitespace());
    }

    @Test
    public void willReturnParsingErrors() {

        final GetProfileResult result = GetProfileResult.from(Fixtures.iqFrom(GetProfileFixtures.GET_PROFILE_RESULT_WITH_BAD_VALUES));

        assertThat(result.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: set",
                "Invalid get-profile result stanza; missing 'profile'"));
    }

    @Test
    public void willReturnParsedResult() {

        final GetProfileResult result = GetProfileResult.from(Fixtures.iqFrom(GetProfileFixtures.GET_PROFILE_RESULT));

        assertReflectionEquals(CoreFixtures.KEYPAGE_PROFILE, result.getProfile().get());
    }

    @Test
    public void willBuildAResultFromARequest() {

        final GetProfileRequest request = GetProfileRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        final GetProfileResult result = GetProfileResult.Builder.createResultBuilder(request)
                .setProfile(CoreFixtures.PROFILE)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }
}