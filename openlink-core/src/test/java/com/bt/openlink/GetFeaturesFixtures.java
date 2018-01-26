package com.bt.openlink;

import com.bt.openlink.type.Feature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;

@SuppressWarnings("ConstantConditions")
public class GetFeaturesFixtures {

    public static final String GET_FEATURES_REQUEST =
            "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-features' action='execute'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in>\n" +
                    "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
                    "      </in>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_FEATURES_REQUEST_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#get-features'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
                    "      <in/>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final Feature FEATURE_HANDSET_1 = Feature.Builder.start()
            .setType(FeatureType.HANDSET)
            .setId(FeatureId.from("hs_1").get())
            .setLabel("Handset 1")
            .build();
    public static final Feature FEATURE_HANDSET_2 = Feature.Builder.start()
            .setType(FeatureType.HANDSET)
            .setId(FeatureId.from("hs_2").get())
            .setLabel("Handset 2")
            .build();
    public static final Feature FEATURE_PRIVACY = Feature.Builder.start()
            .setType(FeatureType.PRIVACY)
            .setId(FeatureId.from("priv_1").get())
            .setLabel("Privacy")
            .build();
    public static final Feature FEATURE_CALL_FORWARD = Feature.Builder.start()
            .setType(FeatureType.CALL_FORWARD)
            .setId(FeatureId.from("fwd_1").get())
            .setLabel("Call Forward")
            .build();


    public static final String GET_FEATURES_RESULT =
            "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-features' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "        <profile id='" + CoreFixtures.PROFILE_ID + "'/>\n" +
                    "        <features xmlns='http://xmpp.org/protocol/openlink:01:00:00/features'>\n" +
                    "          <feature id='hs_1' type='Handset' label='Handset 1'/>\n" +
                    "          <feature id='hs_2' type='Handset' label='Handset 2'/>\n" +
                    "          <feature id='priv_1' type='Privacy' label='Privacy'/>\n" +
                    "          <feature id='fwd_1' type='CallForward' label='Call Forward'/>\n" +
                    "        </features>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";

    public static final String GET_FEATURES_RESULT_WITH_BAD_VALUES =
            "<iq type='get'>\n" +
                    "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#get-features' status='completed'>\n" +
                    "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
                    "      <out>\n" +
                    "      </out>\n" +
                    "    </iodata>\n" +
                    "  </command>\n" +
                    "</iq>\n";
    
}
