package com.bt.openlink;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.Site;

@SuppressWarnings("ConstantConditions")
public class Fixtures {
    public enum typeEnum {
        set,
        result,
        MY_UNEXPECTED_TYPE
    }

    public static final String JID = "test-user";
    public static final Instant START_TIME = LocalDateTime.parse("2017-10-09T08:07:00").atZone(ZoneId.of("UTC")).toInstant();
    public static final Duration DURATION = Duration.ofMinutes(1);
    public static final CallId CALL_ID = CallId.from("test-call-id").get();
    public static final ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
    public static final InterestId INTEREST_ID = InterestId.from("test-interest-id").get();
    public static final InterestType INTEREST_TYPE = InterestType.from("test-interest-type").get();
    public static final FeatureId FEATURE_ID = FeatureId.from("test-feature-id").get();
    public static final Participant PARTICIPANT = Participant.Builder.start()
            .setJID(JID)
            .setType(ParticipantType.ACTIVE)
            .setDirection(CallDirection.INCOMING)
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .build();
    public static final Site SITE = Site.Builder.start()
            .setId(42)
            .setType(Site.Type.BTSM)
            .setName("test-site-name")
            .setDefault(true)
            .build();
    public static final Profile PROFILE = Profile.Builder.start()
            .setId(PROFILE_ID)
            .setLabel("test-profile")
            .setOnline(true)
            .setDefault(true)
            .setSite(SITE)
            .build();
    public static final Feature FEATURE = Feature.Builder.start()
            .setType(FeatureType.PRIVACY)
            .setId(FEATURE_ID)
            .setLabel("Privact")
            .build();
}
