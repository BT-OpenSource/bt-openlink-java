package com.bt.openlink;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallFeature;
import com.bt.openlink.type.CallFeatureBoolean;
import com.bt.openlink.type.CallFeatureDeviceKey;
import com.bt.openlink.type.CallFeatureHandset;
import com.bt.openlink.type.CallFeatureSpeakerChannel;
import com.bt.openlink.type.CallFeatureTextValue;
import com.bt.openlink.type.CallFeatureVoiceRecorder;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.CallStatus;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.ConferenceId;
import com.bt.openlink.type.DeviceId;
import com.bt.openlink.type.DeviceKey;
import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.DeviceType;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;
import com.bt.openlink.type.OriginatorReference;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RecorderChannel;
import com.bt.openlink.type.RecorderNumber;
import com.bt.openlink.type.RecorderPort;
import com.bt.openlink.type.RecorderType;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;
import com.bt.openlink.type.TelephonyCallId;
import com.bt.openlink.type.UserId;
import com.bt.openlink.type.VoiceRecorderInfo;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "WeakerAccess"})
public final class CoreFixtures {

    static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter JAVA_UTIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");

    public enum typeEnum {
        set,
        result,
        MY_UNEXPECTED_TYPE
    }

    private static final String USER_NAME = "test-user";
    private static final String DOMAIN = "test-domain";
    private static final String RESOURCE = "test-resource";
    public static final String TSC = "component42." + DOMAIN;
    public static final String USER_BARE_JID_STRING = String.format("%s@%s", USER_NAME, DOMAIN);
    public static final String USER_FULL_JID_STRING = String.format("%s/%s", USER_BARE_JID_STRING, RESOURCE);
    public static final String TO_JID_STRING = String.format("test-to-user@%s/%s", DOMAIN, RESOURCE);
    public static final String FROM_JID_STRING = String.format("test-from-user@%s/%s", DOMAIN, RESOURCE);
    public static final String STANZA_ID = "test-stanza-id";
    public static final Instant START_TIME = LocalDateTime.parse("2017-10-09T08:07:00").atZone(ZoneId.of("UTC")).toInstant();
    public static final Duration DURATION = Duration.ofMinutes(1);
    public static final CallId CALL_ID = CallId.from("test-call-id").get();
    public static final TelephonyCallId TELEPHONY_CALL_ID = TelephonyCallId.from("test-telephony-call-id").get();
    public static final ConferenceId CONFERENCE_ID = ConferenceId.from("test-conference-id").get();
    public static final ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
    public static final DeviceId DEVICE_ID = DeviceId.from("test-device-id").get();
    public static final DeviceType DEVICE_TYPE = DeviceType.from("test-device-type").get();
    public static final UserId USER_ID = UserId.from("test-user-id").get();
    public static final InterestId INTEREST_ID = InterestId.from("test-interest-id").get();
    public static final InterestType INTEREST_TYPE = InterestType.from("test-interest-type").get();
    public static final PhoneNumber INTEREST_NUMBER = PhoneNumber.from("test-interest-number").get();
    public static final FeatureId FEATURE_ID = FeatureId.from("test-feature-id").get();
    public static final FeatureId SPEAKER_CHANNEL_ID = FeatureId.from("test-speaker-id").get();
    public static final long SPEAKER_CHANNEL_NUMBER = 42;
    public static final PhoneNumber CALLER_NUMBER = PhoneNumber.from("test-caller-number").get();
    public static final String CALLER_NAME = "test-caller-name";
    public static final PhoneNumber CALLER_E164_NUMBER = PhoneNumber.from("test-caller-e164-number").get();
    public static final PhoneNumber CALLED_NUMBER = PhoneNumber.from("test-called-number").get();
    public static final String CALLED_NAME = "test-called-name";
    public static final PhoneNumber CALLED_DESTINATION = PhoneNumber.from("test-called-destination").get();
    public static final PhoneNumber CALLED_E164_NUMBER = PhoneNumber.from("test-called-e164-number").get();
    public static final Participant LOCAL_PARTICIPANT = Participant.Builder.start()
            .setJID(USER_BARE_JID_STRING)
            .setType(ParticipantType.ACTIVE)
            .setDirection(CallDirection.INCOMING)
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .build();
    public static final Participant REMOTE_PARTICIPANT = Participant.Builder.start()
            .setNumber(CALLED_NUMBER)
            .setDestinationNumber(CALLED_DESTINATION)
            .addE164Number(CALLED_E164_NUMBER)
            .setType(ParticipantType.ACTIVE)
            .setDirection(CallDirection.OUTGOING)
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .build();
    public static final Site SITE = Site.Builder.start()
            .setId(42)
            .setType(Site.Type.BTSM)
            .setName("test site name")
            .setDefault(true)
            .build();
    public static final Profile PROFILE = Profile.Builder.start()
            .setId(PROFILE_ID)
            .setLabel("test profile label")
            .setOnline(true)
            .setDefault(true)
            .setSite(SITE)
            .build();
    public static final CallFeature CALL_FEATURE = CallFeatureBoolean.Builder.start()
            .setType(FeatureType.CALL_BACK)
            .setId(FEATURE_ID)
            .setLabel("Call Back")
            .setEnabled(true)
            .build();
    public static final CallFeatureSpeakerChannel SPEAKER_FEATURE = CallFeatureSpeakerChannel.Builder.start()
            .setId(SPEAKER_CHANNEL_ID)
            .setChannel(SPEAKER_CHANNEL_NUMBER)
            .setMicrophoneActive(true)
            .setMuteRequested(true)
            .build();
    public static final OriginatorReference ORIGINATOR_REFERENCE = new OriginatorReference("key1", "value1");
    public static final Call CALL_OUTGOING_CONFERENCED = Call.Builder
            .start()
            .setId(CALL_ID)
            .setTelephonyCallId(TELEPHONY_CALL_ID)
            .setConferenceId(CONFERENCE_ID)
            .setSite(SITE)
            .setProfileId(PROFILE_ID)
            .setDeviceId(DEVICE_ID)
            .setUserId(USER_ID)
            .setInterestId(INTEREST_ID)
            .setChanged(Changed.STATE)
            .setState(CallState.CALL_CONFERENCED)
            .setDirection(CallDirection.OUTGOING)
            .setCallerNumber(CALLER_NUMBER)
            .setCallerName(CALLER_NAME)
            .addCallerE164Number(CALLER_E164_NUMBER)
            .setCalledNumber(CALLED_NUMBER)
            .setCalledName(CALLED_NAME)
            .addCalledE164Number(CALLED_E164_NUMBER)
            .setCalledDestination(CALLED_DESTINATION)
            .addOriginatorReference(ORIGINATOR_REFERENCE)
            .addOriginatorReference("key2", "value2")
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .addAction(RequestAction.ANSWER_CALL)
            .addFeature(CallFeatureHandset.Builder.start().setId(FeatureId.from("hs_1").get()).setType(FeatureType.HANDSET).setMicrophoneEnabled(true).setLabel("Handset 1").setEnabled(true).build())
            .addFeature(CallFeatureHandset.Builder.start().setId(FeatureId.from("hs_2").get()).setType(FeatureType.HANDSET).setLabel("Handset 2").setEnabled(false).build())
            .addFeature(CallFeatureBoolean.Builder.start().setId(FeatureId.from("priv_1").get()).setType(FeatureType.PRIVACY).setLabel("Privacy").setEnabled(false).build())
            .addFeature(
                    CallFeatureDeviceKey.Builder.start().setId(FeatureId.from("NetrixHiTouch_sales1").get()).setType(FeatureType.DEVICE_KEYS).setLabel("NetrixHiTouch").addDeviceKey(DeviceKey.from("key_1:1:1").get())
                            .build())
            .addFeature(SPEAKER_FEATURE)
            .addFeature(CallFeatureTextValue.Builder.start()
                    .setId(FeatureId.from("MK1021").get())
                    .setValue("VoiceMessage")
                    .setType(FeatureType.VOICE_MESSAGE)
                    .setLabel("Voice Message")
            .build())
            .addFeature(CallFeatureVoiceRecorder.Builder.start()
                    .setId(FeatureId.from("voicerecorder_1").get())
                    .setLabel("Voice Recorder")
                    .setType(FeatureType.VOICE_RECORDER)
                    .setVoiceRecorderInfo(
                            VoiceRecorderInfo.Builder.start()
                                    .setRecorderNumber(RecorderNumber.from("011").get())
                                    .setRecorderPort(RecorderPort.from("5").get())
                                    .setRecorderChannel(RecorderChannel.from("2").get())
                                    .setRecorderType(RecorderType.from("V").get())
                                    .build()
                    )
                    .build())
            .addParticipant(LOCAL_PARTICIPANT)
            .addParticipant(REMOTE_PARTICIPANT)
            .build();
    public static final CallStatus CALL_STATUS = CALL_OUTGOING_CONFERENCED.toCallStatus(false);
    public static final Interest INTEREST = Interest.Builder.start()
            .setType(INTEREST_TYPE)
            .setDefault(true)
            .setId(INTEREST_ID)
            .setLabel("test interest label")
            .setCallStatus(CALL_STATUS)
            .setMaxCalls(3)
            .setNumber(INTEREST_NUMBER)
            .build();

    public static final String START_TIME_ISO_8601 = ISO_8601_FORMATTER.format(START_TIME.atZone(ZoneOffset.UTC));


    public static final DeviceStatus DEVICE_STATUS_LOGON = DeviceStatus.Builder.start()
            .setProfileId(PROFILE_ID)
            .setOnline(true)
            .setDeviceId(DEVICE_ID)
            .build();

    public static final String CALL_STATUS_OUTGOING_CONFERENCED =
            "<callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status' busy='false'>\n" +
                    "  <call>\n" +
                    "    <id telephony='" + CoreFixtures.TELEPHONY_CALL_ID + "'>" + CoreFixtures.CALL_ID + "</id>\n" +
                    "    <conference>" + CoreFixtures.CONFERENCE_ID + "</conference>\n" +
                    "    <site id='42' type='BTSM' default='true'>test site name</site>\n" +
                    "    <profile devicenum='" + CoreFixtures.DEVICE_ID + "'>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                    "    <user>" + CoreFixtures.USER_ID + "</user>\n" +
                    "    <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
                    "    <changed>State</changed>\n" +
                    "    <state>CallConferenced</state>\n" +
                    "    <direction>Outgoing</direction>\n" +
                    "    <caller>\n" +
                    "      <number e164='" + CoreFixtures.CALLER_E164_NUMBER + "'>" + CoreFixtures.CALLER_NUMBER + "</number>\n" +
                    "      <name>" + CoreFixtures.CALLER_NAME + "</name>\n" +
                    "    </caller>\n" +
                    "    <called>\n" +
                    "      <number e164='" + CoreFixtures.CALLED_E164_NUMBER + "' destination='" + CoreFixtures.CALLED_DESTINATION + "'>" + CoreFixtures.CALLED_NUMBER + "</number>\n" +
                    "      <name>" + CoreFixtures.CALLED_NAME + "</name>\n" +
                    "    </called>\n" +
                    "    <originator-ref>\n" +
                    "      <property id='key1'>\n" +
                    "        <value>value1</value>\n" +
                    "      </property>\n" +
                    "      <property id='key2'>\n" +
                    "        <value>value2</value>\n" +
                    "      </property>\n" +
                    "    </originator-ref>\n" +
                    "    <start>" + ISO_8601_FORMATTER.format(START_TIME.atZone(ZoneOffset.UTC)) + "</start>\n" +
                    "    <duration>60000</duration>\n" +
                    "    <actions>\n" +
                    "      <AnswerCall/>\n" +
                    "    </actions>\n" +
                    "    <features>\n" +
                    "      <feature id='hs_1' type='Handset' label='Handset 1' microphone='true'>true</feature>\n" +
                    "      <feature id='hs_2' type='Handset' label='Handset 2'>false</feature>\n" +
                    "      <feature id='priv_1' type='Privacy' label='Privacy'>false</feature>\n" +
                    "      <feature id='NetrixHiTouch_sales1' type='DeviceKeys' label='NetrixHiTouch'>\n" +
                    "        <devicekeys xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#device-keys'>\n" +
                    "          <key>key_1:1:1</key>\n" +
                    "        </devicekeys>\n" +
                    "      </feature>\n" +
                    "      <feature id='" + SPEAKER_CHANNEL_ID + "' type='SpeakerChannel'>\n" +
                    "        <speakerchannel xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#speaker-channel'>\n" +
                    "          <channel>" + SPEAKER_CHANNEL_NUMBER + "</channel>\n" +
                    "          <microphone>true</microphone>\n" +
                    "          <mute>true</mute>\n" +
                    "        </speakerchannel>\n" +
                    "      </feature>\n" +
                    "      <feature id=\"MK1021\" type='VoiceMessage' label='Voice Message'>VoiceMessage</feature>\n" +
                    "      <feature id='voicerecorder_1' type='VoiceRecorder'>\n" +
                    "           <voicerecorder xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#voice-recorder'>\n" +
                    "               <recnumber>011</recnumber>\n" +
                    "               <recport>5</recport>\n" +
                    "               <recchan>2</recchan>\n" +
                    "               <rectype>V</rectype>\n" +
                    "           </voicerecorder>\n" +
                    "       </feature>\n" +
                    "    </features>\n" +
                    "    <participants>\n" +
                    "      <participant direction='Incoming' jid='test-user@test-domain' start='" + START_TIME_ISO_8601 + "' timestamp='"
                    + JAVA_UTIL_DATE_FORMATTER.format(START_TIME.atZone(TimeZone.getTimeZone("UTC").toZoneId())) + "' duration='60000' type='Active'/>\n" +
                    "      <participant direction='Outgoing' number='" + CALLED_NUMBER + "'"
                    + " destination='" + CALLED_DESTINATION + "'"
                    + " e164Number='" + CALLED_E164_NUMBER + "'"
                    + " start='" + START_TIME_ISO_8601 + "' timestamp='" + JAVA_UTIL_DATE_FORMATTER.format(START_TIME.atZone(TimeZone.getTimeZone("UTC").toZoneId())) + "' duration='60000' type='Active'/>\n" +
                    "    </participants>\n" +
                    "  </call>\n" +
                    "</callstatus>\n";

    public static final String CALL_STATUS_LEGACY_FEATURES =
            "<callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status' busy='false'>\n" +
                    "  <call>\n" +
                    "    <id telephony='" + CoreFixtures.TELEPHONY_CALL_ID + "'>" + CoreFixtures.CALL_ID + "</id>\n" +
                    "    <changed>State</changed>\n" +
                    "    <state>CallConferenced</state>\n" +
                    "    <direction>Outgoing</direction>\n" +
                    "    <duration>60000</duration>\n" +
                    "    <features>\n" +
                    "      <feature id='hs_1'>true</feature>\n" +
                    "      <feature id='hs_2'>false</feature>\n" +
                    "      <feature id='priv_1'>false</feature>\n" +
                    "      <feature id='NetrixHiTouch_sales1'>\n" +
                    "        <devicekeys xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#device-keys'>\n" +
                    "          <key>key_1:1:1</key>\n" +
                    "        </devicekeys>\n" +
                    "      </feature>\n" +
                    "      <feature id='" + SPEAKER_CHANNEL_ID + "'>\n" +
                    "        <speakerchannel xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#speaker-channel'>\n" +
                    "          <channel>" + SPEAKER_CHANNEL_NUMBER + "</channel>\n" +
                    "          <microphone>true</microphone>\n" +
                    "          <mute>true</mute>\n" +
                    "        </speakerchannel>\n" +
                    "      </feature>\n" +
                    "      <feature id=\"MK1021\" type='VoiceMessage'>VoiceMessage</feature>\n" +
                    "      <feature id='voicerecorder_1'>\n" +
                    "           <voicerecorder xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#voice-recorder'>\n" +
                    "               <recnumber>011</recnumber>\n" +
                    "               <recport>5</recport>\n" +
                    "               <recchan>2</recchan>\n" +
                    "               <rectype>V</rectype>\n" +
                    "           </voicerecorder>\n" +
                    "       </feature>\n" +
                    "    </features>\n" +
                    "  </call>\n" +
                    "</callstatus>\n";

    public static final List<Feature> LEGACY_FEATURE_LIST;
    static {
        final List<Feature> features = new ArrayList<>();
        features.add(CallFeatureBoolean.Builder.start()
                .setId(FeatureId.from("hs_1").get())
                .setEnabled(true)
                .build(new ArrayList<>()));
        features.add(CallFeatureBoolean.Builder.start()
                .setId(FeatureId.from("hs_2").get())
                .setEnabled(false)
                .build(new ArrayList<>()));
        features.add(CallFeatureBoolean.Builder.start()
                .setId(FeatureId.from("priv_1").get())
                .setEnabled(false)
                .build(new ArrayList<>()));
        features.add(CallFeatureDeviceKey.Builder.start()
                .setId(FeatureId.from("NetrixHiTouch_sales1").get())
                .addDeviceKey(DeviceKey.from("key_1:1:1").get())
                .build(new ArrayList<>()));
        features.add(CallFeatureSpeakerChannel.Builder.start()
                .setId(FeatureId.from("test-speaker-id").get())
                .setChannel(42)
                .setMicrophoneActive(true)
                .setMuteRequested(true)
                .build(new ArrayList<>()));
        features.add(CallFeatureTextValue.Builder.start()
                .setId(FeatureId.from("MK1021").get())
                .setValue("VoiceMessage")
                .setType(FeatureType.VOICE_MESSAGE)
                .build(new ArrayList<>()));
        features.add(CallFeatureVoiceRecorder.Builder.start()
                .setId(FeatureId.from("voicerecorder_1").get())
                .setVoiceRecorderInfo(                            VoiceRecorderInfo.Builder.start()
                        .setRecorderNumber(RecorderNumber.from("011").get())
                        .setRecorderPort(RecorderPort.from("5").get())
                        .setRecorderChannel(RecorderChannel.from("2").get())
                        .setRecorderType(RecorderType.from("V").get())
                        .build())
                .build(new ArrayList<>()));
        LEGACY_FEATURE_LIST = Collections.unmodifiableList(features);
    }

}
