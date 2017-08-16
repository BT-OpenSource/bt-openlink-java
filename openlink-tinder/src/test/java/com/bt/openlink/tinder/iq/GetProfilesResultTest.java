package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.Site;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetProfilesResultTest {

    private static final Site SITE = Site.Builder.start()
            .setId(42)
            .setDefault(true)
            .setType(Site.Type.BTSM)
            .setName("test-site-name")
            .build();
    private static final Profile PROFILE = Profile.Builder.start()
            .setProfileId(Fixtures.PROFILE_ID)
            .setDefault(true)
            .setDevice("uta")
            .setLabel("7001")
            .setOnline(true)
            .setSite(SITE)
            .build();

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String GET_PROFILES_RESULT_WITH_NO_NOTES = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <profiles xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/profiles\">\n" +
            "          <profile default=\"true\" device=\"uta\" id=\"UCTrader1-trader1@btsm1\" label=\"7001\" online=\"true\">\n" +
            "            <site default=\"false\" id=\"1\" type=\"IPT\">itrader-dev-sm-1</site>\n" +
            "            <actions>\n" +
            "              <action id=\"AnswerCall\" label=\"Answers an alerting call on active profile device\"/>\n" +
            "              <action id=\"ClearCall\" label=\"Clears the call\"/>\n" +
            "              <action id=\"ClearConnection\" label=\"Clear this participant connection from active call or conference\"/>\n" +
            "              <action id=\"HoldCall\" label=\"Place a call on hold\"/>\n" +
            "              <action id=\"RetrieveCall\" label=\"Re-connect a held call\"/>\n" +
            "              <action id=\"JoinCall\" label=\"Join a connected or conferenced call\"/>\n" +
            "              <action id=\"PrivateCall\" label=\"Makes the active call private. Other users cannot join\"/>\n" +
            "              <action id=\"PublicCall\" label=\"Makes the active private call public for other users to join\"/>\n" +
            "              <action id=\"StartVoiceDrop\" label=\"Starts playing a pre-recorded voice message or playlist into the active call\"/>\n" +
            "              <action id=\"TransferCall\" label=\" Completes a transfer started with ConsultationCall. Releases the active profile device from the call.\"/>\n" +
            "              <action id=\"SingleStepTransfer\" label=\"Single Step transfer\"/>\n" +
            "              <action id=\"SendDigits\" label=\"Causes dial digits to be sent on an originated call on the active device\"/>\n" +
            "              <action id=\"SendDigit\" label=\"Send Digit\"/>\n" +
            "              <action id=\"ConsultationCall\" label=\"Consultation Call\"/>\n" +
            "            </actions>\n" +
            "          </profile>\n" +
            "          <profile default=\"true\" device=\"uta\" id=\"UCTrader1-trader1@btsm11\" label=\"7001\" online=\"true\">\n" +
            "            <site default=\"true\" id=\"11\" type=\"ITS\">itrader-dev-sm-5</site>\n" +
            "            <actions>\n" +
            "              <action id=\"AnswerCall\" label=\"Answers an alerting call on active profile device\"/>\n" +
            "              <action id=\"ClearCall\" label=\"Clears the call\"/>\n" +
            "              <action id=\"ClearConnection\" label=\"Clear this participant connection from active call or conference\"/>\n" +
            "              <action id=\"HoldCall\" label=\"Place a call on hold\"/>\n" +
            "              <action id=\"RetrieveCall\" label=\"Re-connect a held call\"/>\n" +
            "              <action id=\"JoinCall\" label=\"Join a connected or conferenced call\"/>\n" +
            "              <action id=\"PrivateCall\" label=\"Makes the active call private. Other users cannot join\"/>\n" +
            "              <action id=\"PublicCall\" label=\"Makes the active private call public for other users to join\"/>\n" +
            "              <action id=\"StartVoiceDrop\" label=\"Starts playing a pre-recorded voice message or playlist into the active call\"/>\n" +
            "              <action id=\"TransferCall\" label=\" Completes a transfer started with ConsultationCall. Releases the active profile device from the call.\"/>\n" +
            "              <action id=\"SingleStepTransfer\" label=\"Single Step transfer\"/>\n" +
            "              <action id=\"SendDigits\" label=\"Causes dial digits to be sent on an originated call on the active device\"/>\n" +
            "              <action id=\"SendDigit\" label=\"Send Digit\"/>\n" +
            "              <action id=\"ConsultationCall\" label=\"Consultation Call\"/>\n" +
            "            </actions>\n" +
            "          </profile>\n" +
            "        </profiles>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_PROFILES_RESULT_WITH_BAD_VALUES = "<iq type=\"set\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" action=\"execute\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_PROFILES_RESULT_WITH_NO_PROFILES = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <profiles xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/profiles\">\n" +
            "        </profiles>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetProfilesResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        // TODO: (Greg 2016-08-08) Replace this with GET_PROFILES_RESULT_WITH_NO_NOTES when fully implemented
        final String expectedXML = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
                "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\" status=\"completed\">\n" +
                "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
                "      <out>\n" +
                "        <profiles>" +
                "          <profile default=\"true\" device=\"uta\" id=\"" + Fixtures.PROFILE_ID + "\" label=\"7001\" online=\"true\">\n" +
                "           <site default=\"true\" id=\"42\" type=\"BTSM\">test-site-name</site>\n" +
                "          </profile>\n" +
                "        </profiles>" +
                "      </out>\n" +
                "    </iodata>\n" +
                "  </command>\n" +
                "</iq>";

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(PROFILE)
                .build();

        assertThat(result.toXML(), isIdenticalTo(expectedXML).ignoreWhitespace());
    }

    @Test
    public void willNotBuildAPacketWithDuplicateProfileIds() throws Exception {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The profile id must be unique");
        GetProfilesResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(PROFILE)
                .addProfile(PROFILE)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfilesResult result = (GetProfilesResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_PROFILES_RESULT_WITH_NO_NOTES));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getType(), is(IQ.Type.result));
        final List<Profile> profiles = result.getProfiles();
        int i = 0;

        Profile profile = profiles.get(i++);
        Site site = profile.getSite().get();
        assertThat(profile.profileId().get().value(), is("UCTrader1-trader1@btsm1"));
        assertThat(site.getId().get(), is(1L));
        assertThat(site.getType().get(), is(Site.Type.IPT));
        assertThat(site.isDefault().get(), is(false));
        assertThat(site.getName().get(), is("itrader-dev-sm-1"));

        profile = profiles.get(i++);
        site = profile.getSite().get();
        assertThat(profile.profileId().get().value(), is("UCTrader1-trader1@btsm11"));
        assertThat(site.getId().get(), is(11L));
        assertThat(site.getType().get(), is(Site.Type.ITS));
        assertThat(site.isDefault().get(), is(true));
        assertThat(site.getName().get(), is("itrader-dev-sm-5"));

        assertThat(profiles.size(), is(i));

        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final IQ iq = Fixtures.iqFrom(GET_PROFILES_RESULT_WITH_BAD_VALUES);

        final GetProfilesResult result = GetProfilesResult.from(iq);

        assertThat(result.getParseErrors(), contains(
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid get-profiles result; missing 'profiles' element is mandatory"));
    }

    @Test
    public void willReturnANoProfilesParsingError() throws Exception {

        final IQ iq = Fixtures.iqFrom(GET_PROFILES_RESULT_WITH_NO_PROFILES);

        final GetProfilesResult result = GetProfilesResult.from(iq);

        assertThat(result.getParseErrors(), contains(
                "Invalid get-profiles result; no 'profile' elements present"));
    }

    @Test
    public void willBuildAResultFromARequest() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setID(Fixtures.STANZA_ID)
                .setJID(Fixtures.USER_JID)
                .build();

        final GetProfilesResult result = GetProfilesResult.Builder.start(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }
}