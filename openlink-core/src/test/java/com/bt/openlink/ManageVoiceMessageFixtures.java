package com.bt.openlink;

import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.VoiceMessage;
import com.bt.openlink.type.VoiceMessageFeature;
import com.bt.openlink.type.VoiceMessageStatus;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

@SuppressWarnings("ConstantConditions")
public class ManageVoiceMessageFixtures {

    public static final FeatureId VOICE_MESSAGE_ID_FEATURE = FeatureId.from("MK1047").get();
    public static final String VOICE_MESSAGE_LABEL = "VM label";

    public static final String MANAGE_VOICE_MESSAGE_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
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

    public static final String MANAGE_VOICE_MESSAGE_QUERY_RESULT = "<iq type=\"result\" id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "    <command xmlns=\"http://jabber.org/protocol/commands\"\n" +
            "             node=\"http://xmpp.org/protocol/openlink:01:00:00#manage-voice-message\" status=\"completed\">\n" +
            "        <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "            <out>\n" +
            "                <devicestatus xmlns=\"http://xmpp.org/protocol/openlink:01:00:00#device-status\">\n" +
            "                    <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "                    <features>\n" +
            "                        <feature id=\"MK1013\">\n" +
            "                            <voicemessage xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/features#voice-message\">\n" +
            "                                <label>testVD10Apr2015</label>\n" +
            "                                <status>ok</status>\n" +
            "                                <action>Query</action>\n" +
            "                                <msglen>4.073</msglen>\n" +
            "                                <creationdate>2015-04-10 21:42:29.0</creationdate>\n" +
            "                            </voicemessage>\n" +
            "                        </feature>\n" +
            "                        <feature id=\"MK1021\">\n" +
            "                            <voicemessage xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/features#voice-message\">\n" +
            "                                <label>VM20150822</label>\n" +
            "                                <status>ok</status>\n" +
            "                                <action>Query</action>\n" +
            "                                <msglen>6.713</msglen>\n" +
            "                                <creationdate>2015-08-22 17:09:58.0</creationdate>\n" +
            "                            </voicemessage>\n" +
            "                        </feature>\n" +
            "                    </features>\n" +
            "                </devicestatus>\n" +
            "            </out>\n" +
            "        </iodata>\n" +
            "    </command>\n" +
            "</iq>";

    public static final String MANAGE_VOICE_MESSAGE_PLAYBACK_RESULT = "<iq type=\"result\" id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "    <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#manage-voice-message\" status=\"completed\">\n" +
            "        <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "            <out>\n" +
            "                <devicestatus xmlns=\"http://xmpp.org/protocol/openlink:01:00:00#device-status\">\n" +
            "                    <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "                    <features>\n" +
            "                        <feature id=\"MK1047\">\n" +
            "                            <voicemessage xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/features#voice-message\">\n" +
            "                                <status>ok</status>\n" +
            "                                <action>Playback</action>\n" +
            "                                <exten>551603</exten>\n" +
            "                            </voicemessage>\n" +
            "                        </feature>\n" +
            "                    </features>\n" +
            "                </devicestatus>\n" +
            "            </out>\n" +
            "        </iodata>\n" +
            "    </command>\n" +
            "</iq>";

    public static final String MANAGE_VOICE_MESSAGE_EDIT_RESULT = "<iq type=\"result\" id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#manage-voice-message\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <devicestatus xmlns=\"http://xmpp.org/protocol/openlink:01:00:00#device-status\">\n" +
            "          <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "          <features>\n" +
            "            <feature id=\"MK1047\">\n" +
            "              <voicemessage xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/features#voice-message\">\n" +
            "                <status>ok</status>\n" +
            "                <action>Edit</action>\n" +
            "              </voicemessage>\n" +
            "            </feature>\n" +
            "          </features>\n" +
            "        </devicestatus>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final DeviceStatus DEVICE_STATUS_EDIT = DeviceStatus.Builder.start()
            .setProfileId(CoreFixtures.PROFILE_ID)
            .addFeature(VoiceMessageFeature.Builder.start()
                    .setId(FeatureId.from("MK1047").get())
                    .setVoiceMessage(VoiceMessage.Builder.start()
                            .setStatus(VoiceMessageStatus.OK)
                            .setAction(ManageVoiceMessageAction.EDIT)
                            .build())
                    .build()
            )
            .build();

    public static final DeviceStatus DEVICE_STATUS_PLAYBACK = DeviceStatus.Builder.start()
            .setProfileId(CoreFixtures.PROFILE_ID)
            .addFeature(VoiceMessageFeature.Builder.start()
                    .setId(FeatureId.from("MK1047").get())
                    .setVoiceMessage(VoiceMessage.Builder.start()
                            .setStatus(VoiceMessageStatus.OK)
                            .setAction(ManageVoiceMessageAction.PLAYBACK)
                            .setExtension(PhoneNumber.from("551603").get())
                            .build())
                    .build()
            )
            .build();

    public static final DeviceStatus DEVICE_STATUS_QUERY = DeviceStatus.Builder.start()
            .setProfileId(CoreFixtures.PROFILE_ID)
            .addFeature(VoiceMessageFeature.Builder.start()
                    .setId(FeatureId.from("MK1013").get())
                    .setVoiceMessage(VoiceMessage.Builder.start()
                            .setLabel("testVD10Apr2015")
                            .setStatus(VoiceMessageStatus.OK)
                            .setAction(ManageVoiceMessageAction.QUERY)
                            .setMsgLength(Duration.ofMillis(4073))
                            .setCreationDate(parseSqlTimeStamp("2015-04-10 21:42:29.0"))
                            .build())
                    .build()
            )
            .addFeature(VoiceMessageFeature.Builder.start()
                    .setId(FeatureId.from("MK1021").get())
                    .setVoiceMessage(VoiceMessage.Builder.start()
                            .setLabel("VM20150822")
                            .setStatus(VoiceMessageStatus.OK)
                            .setAction(ManageVoiceMessageAction.QUERY)
                            .setMsgLength(Duration.ofMillis(6713))
                            .setCreationDate(parseSqlTimeStamp("2015-08-22 17:09:58.0"))
                            .build())
                    .build()
            )
            .build();;

    private static Instant parseSqlTimeStamp(@Nonnull final String dateTime) {
        return Instant.from(Timestamp.valueOf(dateTime).toInstant());
    }
}
