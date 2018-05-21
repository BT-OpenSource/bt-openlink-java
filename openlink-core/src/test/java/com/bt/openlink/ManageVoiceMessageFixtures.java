package com.bt.openlink;

import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;

@SuppressWarnings("ConstantConditions")
public class ManageVoiceMessageFixtures {

    public static final FeatureId VOICE_MESSAGE_ID_FEATURE = FeatureId.from("MK1047").get();
    public static final String VOICE_MESSAGE_LABEL = "VM label";

    public static final String MANAGE_VOICE_MESSAGE_REQUEST =  "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#manage-voice-message\" action=\"execute\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"input\">\n" +
            "      <in>\n" +
            "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "        <action>" + ManageVoiceMessageAction.PLAYBACK.getId() + "</action>\n" +
            "        <features>\n" +
            "          <feature>\n" +
            "            <id>MK1047</id>\n" +
            "          </feature>\n" +
            "        </features>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String MANAGE_VOICE_MESSAGE_REQUEST_WITH_BAD_VALUES = "<iq type='set'>\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#manage-voice-message\" action=\"execute\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"input\">\n" +
            "      <in>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";
}
