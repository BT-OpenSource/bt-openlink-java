package com.bt.openlink.tinder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.PubSubNodeId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

@SuppressWarnings("ConstantConditions")
public final class Fixtures {

    private Fixtures() {
    }

    public static final Instant START_TIME = LocalDateTime.parse("2017-10-09T08:07:00").atZone(ZoneId.of("UTC")).toInstant();
    public static final Duration DURATION = Duration.ofMinutes(1);
    public static final String STANZA_ID = "test-stanza-id";
    public static final JID TO_JID = new JID("test-to@test-domain/test-resource");
    public static final JID FROM_JID = new JID("test-from@test-domain/test-resource");
    public static final JID USER_JID = new JID("test-user@test-domain/test-resource");
    public static final String BARE_JID = USER_JID.toBareJID();
    public static final ProfileId PROFILE_ID = ProfileId.from("test-profile-id").get();
    public static final InterestId INTEREST_ID = InterestId.from("test-interest-id").get();
    public static final CallId CALL_ID = CallId.from("test-call-id").get();
    public static final Participant PARTICIPANT = Participant.Builder.start()
            .setJID(BARE_JID)
            .setType(ParticipantType.ACTIVE)
            .setDirection(CallDirection.INCOMING)
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .build();
    public static final Interest INTEREST = Interest.Builder.start()
            .setId(INTEREST_ID)
            .setDefault(true)
            .setLabel("test-default-interest")
            .setType(InterestType.from("test-interest-type").get())
            .build();
    public static Site SITE = Site.Builder.start()
            .setId(42)
            .setType(Site.Type.BTSM)
            .setName("test-site-name")
            .setDefault(true)
            .build();
    public static final Call CALL = Call.Builder.start()
            .setId(CALL_ID)
            .setSite(SITE)
            .setProfileId(PROFILE_ID)
            .setInterestId(INTEREST_ID)
            .setState(CallState.CALL_ORIGINATED)
            .setDirection(CallDirection.INCOMING)
            .setStartTime(START_TIME)
            .setDuration(DURATION)
            .addAction(RequestAction.ANSWER_CALL)
            .addParticipant(PARTICIPANT)
            .build();

    public static final PubSubNodeId NODE_ID = INTEREST_ID.toPubSubNodeId();

    //    public static final FeatureId FEATURE_ID = FeatureId.from("test-feature-id");
    //    public static final CallId CALL_ID = CallId.from("test-call-id");

    // Note; this is also found in collab-test-framework, but to reduce dependencies it's also copied here
    @SuppressWarnings("Duplicates")
    private static Element elementFrom(final InputStream is) {
        try {
            final SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            return document.getRootElement();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static IQ iqFrom(final String stanzaString) {
        return new IQ(elementFrom(new ByteArrayInputStream(stanzaString.getBytes(StandardCharsets.UTF_8))));
    }

    public static Message messageFrom(final String stanzaString) {
        return new Message(elementFrom(new ByteArrayInputStream(stanzaString.getBytes(StandardCharsets.UTF_8))));
    }

}
