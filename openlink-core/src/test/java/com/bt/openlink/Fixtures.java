package com.bt.openlink;

import com.bt.openlink.type.CallId;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.Site;

@SuppressWarnings("ConstantConditions")
public class Fixtures {
    public static final CallId CALL_ID = CallId.from("test-call-id").get();
    public static final ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
    public static final InterestId INTEREST_ID = InterestId.from("test-interest-id").get();
    public static final InterestType INTEREST_TYPE = InterestType.from("test-interest-type").get();
    public static final FeatureId FEATURE_ID = FeatureId.from("test-feature-id").get();
    public static final Site SITE = Site.Builder.start()
            .setId(42)
            .setType(Site.Type.BTSM)
            .setName("test-site-name")
            .setDefault(true)
            .build();
}
