package com.bt.openlink;

import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

@SuppressWarnings("ConstantConditions")
public class GetProfilesFixtures {

    public static final String GET_PROFILES_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-profiles'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <jid>" + CoreFixtures.USER_BARE_JID_STRING + "</jid>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILES_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-profiles'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final Site SITE_2 = Site.Builder.start()
            .setId(11)
            .setType(Site.Type.ITS)
            .setName("another-test-site-name")
            .build();
    public static final ProfileId PROFILE_ID_2 = ProfileId.from("test-profile-id-2").get();
    public static final Profile PROFILE_2 = Profile.Builder.start()
            .setId(PROFILE_ID_2)
            .setDefault(true)
            .setDevice("uta")
            .setLabel("7001")
            .setOnline(true)
            .setSite(SITE_2)
            .addAction(RequestAction.ANSWER_CALL)
            .addAction(RequestAction.CLEAR_CALL)
            .build();

    public static final String GET_PROFILES_RESULT_WITH_NO_NOTES =
            "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-profiles' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "        <profiles xmlns='http://xmpp.org/protocol/openlink:01:00:00/profiles'>\n" +
                    "          <profile default='true' id='" + CoreFixtures.PROFILE_ID + "' label='test profile label' online='true'>\n" +
                    "            <site default='true' id='42' type='BTSM'>test site name</site>\n" +
                    "          </profile>\n" +
                    "          <profile default='true' device='uta' id='" + PROFILE_ID_2 + "' label='7001' online='true'>\n" +
                    "            <site id='11' type='ITS'>another-test-site-name</site>\n" +
                    "            <actions>\n" +
                    "              <action id='AnswerCall' label='Answer a ringing call'/>\n" +
                    "              <action id='ClearCall' label='Remove all participants from a call'/>\n" +
                    "            </actions>\n" +
                    "          </profile>\n" +
                    "        </profiles>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILES_RESULT_WITH_BAD_VALUES =
            "<iq type='set'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-profiles' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_PROFILES_RESULT_WITH_NO_PROFILES =
            "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-profiles' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "        <profiles xmlns='http://xmpp.org/protocol/openlink:01:00:00/profiles'>\n" +
                    "        </profiles>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

}