package com.bt.openlink.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubMessageFixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class CallStatusMessageBuilderTest {

    private static class Builder extends CallStatusMessageBuilder<Builder, String> {
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() {

        final Instant delayedFrom = Instant.now();

        builder.setDelay(delayedFrom)
                .setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate();

        assertThat(builder.getDelay().get(), is(delayedFrom));
        assertThat(builder.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(builder.getItemId().get(), is(PubSubMessageFixtures.ITEM_ID));
        assertThat(builder.isCallStatusBusy().get(), is(false));
        final List<Call> calls = builder.getCalls();
        assertThat(calls.size(), is(1));
        assertThat(calls.get(0).getId().get(), is(CoreFixtures.CALL_ID));
    }

    @Test
    public void willNotValidateABuilderWithoutAPubSubNodeId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId' has not been set");

        builder.setItemId(PubSubMessageFixtures.ITEM_ID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate();
    }

    @Test
    public void willNotValidateABuilderWithoutAnyCalls() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The callstatus message has no calls");

        builder.setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .validate();
    }

    @Test
    public void willNotValidateABuilderWithDuplicateCalls() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each call id must be unique - test-call-id appears more than once");

        builder.setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate();
    }

    @Test
    public void willNotValidateABuilderWithCallsOnDifferentInterests() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id another-node");

        builder.setPubSubNodeId(PubSubNodeId.from("another-node").get())
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate();
    }

    @Test
    public void willCheckAnUnpopulatedBuilder() {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, containsInAnyOrder(
                "Invalid callstatus message stanza; missing or invalid calls",
                "Invalid callstatus message stanza; the 'pubSubNodeId' has not been set"
        ));

    }

    @Test
    public void willCheckCallUniqueness() {

        final List<String> errors = new ArrayList<>();

        builder.setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate(errors);

        assertThat(errors, contains("Invalid callstatus message stanza; each call id must be unique - test-call-id appears more than once"));
    }

    @Test
    public void willCheckCallsAreOnTheRightNodeUniqueness() {

        final List<String> errors = new ArrayList<>();

        builder.setPubSubNodeId(PubSubNodeId.from("another-node").get())
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate(errors);

        assertThat(errors, contains("Invalid callstatus message stanza; the call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id another-node"));
    }
}