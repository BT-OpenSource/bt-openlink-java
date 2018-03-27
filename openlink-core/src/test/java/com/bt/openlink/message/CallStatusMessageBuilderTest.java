package com.bt.openlink.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
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
        builder.setPubSubNodeId(CoreFixtures.INTEREST_ID);
        builder.setItemId(PubSubMessageFixtures.ITEM_ID);
    }

    @Test
    public void willValidateAPopulatedBuilder() {

        final Instant delayedFrom = Instant.now();

        builder.setDelay(delayedFrom)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .validate();

        assertThat(builder.getDelay().get(), is(delayedFrom));
        assertThat(builder.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(builder.getItemId().get(), is(PubSubMessageFixtures.ITEM_ID));
        assertThat(builder.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willNotValidateABuilderWithoutAnyCalls() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The callstatus message has no calls");

        builder.validate();
    }

    @Test
    public void willNotValidateABuilderWithCallsOnDifferentInterests() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id another-node");

        builder.setPubSubNodeId(PubSubNodeId.from("another-node").get())
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .validate();
    }

    @Test
    public void willCheckCallsAreOnTheRightNode() {

        final List<String> errors = new ArrayList<>();

        builder.setPubSubNodeId(PubSubNodeId.from("another-node").get())
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .validate(errors);

        assertThat(errors, contains("Invalid callstatus message stanza; the call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id another-node"));
    }
}