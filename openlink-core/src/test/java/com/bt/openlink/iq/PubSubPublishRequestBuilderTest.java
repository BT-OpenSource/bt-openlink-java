package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.RequestAction;

@SuppressWarnings("ConstantConditions")
public class PubSubPublishRequestBuilderTest {

    private static class Builder extends PubSubPublishRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() throws Exception {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .addCall(CoreFixtures.CALL);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(builder.getCalls(), contains(CoreFixtures.CALL));
    }

    @Test
    public void willValidateTheNodeIdIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");

        builder.validate();
    }

    @Test
    public void willValidateCallIdUniqueness() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each call id must be unique - test-call-id appears more than once");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .addCall(CoreFixtures.CALL)
                .addCall(CoreFixtures.CALL);

        builder.validate();
    }

    @Test
    public void willValidateCallsAreOnTheRightInterest() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id test-interest-id-2");

        builder.setInterestId(InterestId.from("test-interest-id-2").get())
                .addCall(CoreFixtures.CALL);

        builder.validate();
    }

    @Test
    public void willCheckThePubSubNodeId() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.addCall(CoreFixtures.CALL);

        builder.validate(errors);

        assertThat(errors, contains("Invalid pub-sub publish request stanza; missing node id/interest id"));
    }

    @Test
    public void willCheckUniqueness() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .addCall(CoreFixtures.CALL)
                .addCall(Call.Builder.start()
                        .setId(CoreFixtures.CALL_ID)
                        .setSite(CoreFixtures.SITE)
                        .setProfileId(CoreFixtures.PROFILE_ID)
                        .setInterestId(InterestId.from("another-interest-id").get())
                        .setState(CallState.CALL_ORIGINATED)
                        .setDirection(CallDirection.INCOMING)
                        .setStartTime(CoreFixtures.START_TIME)
                        .setDuration(CoreFixtures.DURATION)
                        .addAction(RequestAction.ANSWER_CALL)
                        .addParticipant(CoreFixtures.PARTICIPANT)
                        .build());

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid pub-sub publish request stanza; each call id must be unique - test-call-id appears more than once",
                "Invalid pub-sub publish request stanza; the call with id test-call-id is on interest another-interest-id which differs from the pub-sub node id test-interest-id"
                ));
    }

}