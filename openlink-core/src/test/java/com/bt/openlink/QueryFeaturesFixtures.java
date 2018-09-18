package com.bt.openlink;

import com.bt.openlink.type.ActiveFeature;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class QueryFeaturesFixtures {

    public static final String QUERY_FEATURES_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#query-features' action='execute'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in>\n" +
            "        <profile>" + CoreFixtures.PROFILE_ID + "</profile>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String QUERY_FEATURES_REQUEST_WITH_BAD_VALUES = "<iq type='get'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#query-features'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in/>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    public static final String QUERY_FEATURES_RESULT = "<iq type='result' id='" + CoreFixtures.STANZA_ID + "' to='" + CoreFixtures.TO_JID_STRING + "' from='" + CoreFixtures.FROM_JID_STRING + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#query-features' status='completed'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
            "      <out>\n" +
            "        <features>\n" +
            "          <feature id='fwd_1' type='CallForward' label='Call Forward' value1='+44 800 328 9393' value2='immediate'/>\n" +
            "        </features>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final String QUERY_FEATURES_RESULT_WITH_BAD_VALUES = "<iq type='get'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' node='http://xmpp.org/protocol/openlink:01:00:00#query-features' status='completed'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='output'>\n" +
            "      <out>\n" +
            "        <features>\n" +
            "          <feature/>\n" +
            "        </features>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>";

    public static final ActiveFeature CALL_FORWARD_ACTIVE_FEATURE = ActiveFeature.Builder.start()
            .setType(FeatureType.CALL_FORWARD)
            .setId(FeatureId.from("fwd_1").get())
            .setLabel("Call Forward")
            .setValue1("+44 800 328 9393")
            .setValue2("immediate")
            .build();
}
