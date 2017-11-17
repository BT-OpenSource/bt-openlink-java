package com.bt.openlink;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallFeature;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.DeviceKey;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.PubSubNodeId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

@SuppressWarnings("ConstantConditions")
public class CoreFixtures {
    public enum typeEnum {
        set,
        result,
        MY_UNEXPECTED_TYPE
    }

    private static final String USER_ID = "test-user";
    private static final String DOMAIN = "test-domain";
    private static final String RESOURCE = "test-resource";
    public static final String USER_JID_BARE_STRING = String.format("%s@%s", USER_ID, DOMAIN);
    public static final String USER_JID_STRING = String.format("%s/%s", USER_JID_BARE_STRING, RESOURCE);
    public static final String TO_JID_STRING = String.format("test-to-user@%s/%s", DOMAIN, RESOURCE);
    public static final String FROM_JID_STRING = String.format("test-from-user@%s/%s", DOMAIN, RESOURCE);
    public static final String STANZA_ID = "test-stanza-id";
    public static final Instant START_TIME = LocalDateTime.parse("2017-10-09T08:07:00").atZone(ZoneId.of("UTC")).toInstant();
    public static final Duration DURATION = Duration.ofMinutes(1);
    public static final CallId CALL_ID = CallId.from("test-call-id").get();
    public static final ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
    public static final InterestId INTEREST_ID = InterestId.from("test-interest-id").get();
    public static final PubSubNodeId NODE_ID = INTEREST_ID.toPubSubNodeId();
    public static final InterestType INTEREST_TYPE = InterestType.from("test-interest-type").get();
    public static final FeatureId FEATURE_ID = FeatureId.from("test-feature-id").get();
    public static final PhoneNumber CALLER_NUMBER = PhoneNumber.from("test-caller-number").get();
    public static final String CALLER_NAME = "test-caller-name";
    public static final PhoneNumber CALLER_E164_NUMBER = PhoneNumber.from("test-caller-e164-number").get();
    public static final PhoneNumber CALLED_NUMBER = PhoneNumber.from("test-called-number").get();
    public static final String CALLED_NAME = "test-called-name";
    public static final PhoneNumber CALLED_DESTINATION = PhoneNumber.from("test-called-destination").get();
    public static final PhoneNumber CALLED_E164_NUMBER = PhoneNumber.from("test-called-e164-number").get();
    public static final Participant PARTICIPANT = Participant.Builder.start()
            .setJID(USER_JID_BARE_STRING)
            .setType(ParticipantType.ACTIVE)
            .setDirection(CallDirection.INCOMING)
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
    public static final Feature FEATURE = Feature.Builder.start()
            .setType(FeatureType.PRIVACY)
            .setId(FEATURE_ID)
            .setLabel("Privacy")
            .build();
    public static final CallFeature CALL_FEATURE = CallFeature.Builder.start()
            .setType(FeatureType.PRIVACY)
            .setId(FEATURE_ID)
            .setLabel("Privacy")
            .setEnabled(true)
            .build();
    public static final Interest INTEREST = Interest.Builder.start()
            .setType(INTEREST_TYPE)
            .setDefault(true)
            .setId(INTEREST_ID)
            .setLabel("test interest label")
            .build();
    public static final Call CALL = Call.Builder.start()
            .setId(CALL_ID)
            .setSite(SITE)
            .setProfileId(PROFILE_ID)
            .setInterestId(INTEREST_ID)
            .setChanged(Changed.STATE)
            .setState(CallState.CALL_ORIGINATED)
            .setDirection(CallDirection.INCOMING)
            .setCallerNumber(CALLER_NUMBER)
            .setCallerName(CALLER_NAME)
            .addCallerE164Number(CALLER_E164_NUMBER)
            .setCalledNumber(CALLED_NUMBER)
            .setCalledName(CALLED_NAME)
            .addCalledE164Number(CALLED_E164_NUMBER)
            .setCalledDestination(CALLED_DESTINATION)
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .addAction(RequestAction.ANSWER_CALL)
            .addFeature(CallFeature.Builder.start().setId(FeatureId.from("hs_1").get()).setType(FeatureType.HANDSET).setLabel("Handset 1").setEnabled(false).build())
            .addFeature(CallFeature.Builder.start().setId(FeatureId.from("hs_2").get()).setType(FeatureType.HANDSET).setLabel("Handset 2").setEnabled(false).build())
            .addFeature(CallFeature.Builder.start().setId(FeatureId.from("priv_1").get()).setType(FeatureType.PRIVACY).setLabel("Privacy").setEnabled(false).build())
            .addFeature(CallFeature.Builder.start().setId(FeatureId.from("NetrixHiTouch_sales1").get()).setType(FeatureType.DEVICE_KEYS).setLabel("NetrixHiTouch").setDeviceKey(DeviceKey.from("key_1:1:1").get()).build())
            .addParticipant(PARTICIPANT)
            .build();

}
