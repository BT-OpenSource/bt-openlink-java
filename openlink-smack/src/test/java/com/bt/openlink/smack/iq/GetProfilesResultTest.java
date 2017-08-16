package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
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
            "            <site default=\"false\" id=\"1\" type=\"BTSM\">itrader-dev-sm-1</site>\n" +
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
            "            <site default=\"true\" id=\"11\" type=\"BTSM\">itrader-dev-sm-5</site>\n" +
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
            "  <command xmlns=\"http://jabber.org/protocol/commands\" status=\"completed\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\">\n" +
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

    @BeforeClass
    public static void setUpClass() throws Exception {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() throws Exception {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setStanzaId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getStanzaId(), is(Fixtures.STANZA_ID));
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
                "        <profiles xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/profiles\">" +
                "          <profile id=\"" + Fixtures.PROFILE_ID + "\">\n" +
                "            <site default=\"true\" id=\"42\" type=\"BTSM\">test-site-name</site>\n" +
                "           </profile>\n" +
                "        </profiles>" +
                "      </out>\n" +
                "    </iodata>\n" +
                "  </command>\n" +
                "</iq>";

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setStanzaId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(PROFILE)
                .build();

        assertThat(result.toXML().toString(), isIdenticalTo(expectedXML).ignoreWhitespace());
    }

    @Test
    public void willNotBuildAPacketWithDuplicateProfileIds() throws Exception {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The profile id must be unique");
        GetProfilesResult.Builder.start()
                .setStanzaId(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(PROFILE)
                .addProfile(PROFILE)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfilesResult result = PacketParserUtils.parseStanza(GET_PROFILES_RESULT_WITH_NO_NOTES);

        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getStanzaId(), is(Fixtures.STANZA_ID));
        assertThat(result.getType(), is(IQ.Type.result));
        final List<Profile> profiles = result.getProfiles();
        assertThat(profiles.size(), is(2));
        assertThat(profiles.get(0).profileId().get().value(), is("UCTrader1-trader1@btsm1"));
        assertThat(profiles.get(1).profileId().get().value(), is("UCTrader1-trader1@btsm11"));

        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willBuildAResultFromARequest() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setStanzaId(Fixtures.STANZA_ID)
                .setJid(Fixtures.USER_JID)
                .build();

        final GetProfilesResult result = GetProfilesResult.Builder.start(request)
                .build();

        assertThat(result.getStanzaId(), is(request.getStanzaId()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}