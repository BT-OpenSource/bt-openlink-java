package com.bt.openlink;

import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.Site;

@SuppressWarnings("ConstantConditions")
public class Fixtures {
    public static final CallId CALL_ID = CallId.from("test-call-id").get();
    public static ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
    public static InterestId INTEREST_ID = InterestId.from("test-interest-id").get();
    public static Site SITE = Site.Builder.start()
            .setId(42)
            .setType(Site.Type.BTSM)
            .setName("test-site-name")
            .setDefault(true)
            .build();
}
