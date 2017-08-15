package com.bt.openlink.smack;

import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.ProfileId;
import org.jxmpp.jid.Jid;

@SuppressWarnings("ConstantConditions")
public final class Fixtures {

    private Fixtures() {
    }

    public static final String STANZA_ID = "test-stanza-id";
    public static final Jid TO_JID = SmackPacketUtil.getSmackJid("test-to@test-domain/test-resource").orElseThrow(()->new IllegalArgumentException("Bad JID"));
    public static final Jid FROM_JID = SmackPacketUtil.getSmackJid("test-from@test-domain/test-resource").orElseThrow(()->new IllegalArgumentException("Bad JID"));
    public static final Jid USER_JID = SmackPacketUtil.getSmackJid("test-user@test-domain/test-resource").orElseThrow(()->new IllegalArgumentException("Bad JID"));
    public static final ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
//    public static final     InterestId INTEREST_ID = InterestId.from("test-interest-id");
//    public static final PubSubNodeId NODE_ID = PubSubNodeId.from("test-node-id");
//    public static final FeatureId FEATURE_ID = FeatureId.from("test-feature-id");
//    public static final CallId CALL_ID = CallId.from("test-call-id");

    // Note; this is also found in collab-test-framework, but to reduce dependencies it's also copied here
//    @SuppressWarnings("Duplicates")
//    private static Element elementFrom(final InputStream is) {
//        try {
//            final SAXReader reader = new SAXReader();
//            Document document = reader.read(is);
//            return document.getRootElement();
//        } catch (Exception e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//
//    public static IQ iqFrom(final String stanzaString) {
//        return new IQ(elementFrom(new ByteArrayInputStream(stanzaString.getBytes(StandardCharsets.UTF_8))));
//    }

//    public static Message messageFrom(final String stanzaString) {
//        return new Message(elementFrom(new ByteArrayInputStream(stanzaString.getBytes(StandardCharsets.UTF_8))));
//    }


}
