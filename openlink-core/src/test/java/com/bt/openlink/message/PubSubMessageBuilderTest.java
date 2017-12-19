package com.bt.openlink.message;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.Instant;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubMessageFixtures;

@SuppressWarnings("ConstantConditions")
public class PubSubMessageBuilderTest {

    private static class Builder extends PubSubMessageBuilder<Builder, String> {
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Test
    public void willValidateAPubSubBuilder() {

        final Instant delayedFrom = Instant.now();

        builder = new Builder();
        builder.setTo(CoreFixtures.TO_JID_STRING)
                .setFrom(CoreFixtures.FROM_JID_STRING)
                .setDelay(delayedFrom)
                .setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .validate();

        assertThat(builder.getTo().get(), is(CoreFixtures.TO_JID_STRING));
        assertThat(builder.getFrom().get(), is(CoreFixtures.FROM_JID_STRING));
        assertThat(builder.getDelay().get(), is(delayedFrom));
        assertThat(builder.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(builder.getItemId().get(), is(PubSubMessageFixtures.ITEM_ID));
    }

    @Test
    public void willValidateToIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");

        builder = new Builder();
        builder.setFrom(CoreFixtures.FROM_JID_STRING)
                .setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .validate();
    }

    @Test
    public void willValidateFromIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'from' has not been set");

        builder = new Builder();
        builder.setTo(CoreFixtures.TO_JID_STRING)
                .setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .validate();
    }

    @Test
    public void willValidateNodeIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId' has not been set");

        builder = new Builder();
        builder.setTo(CoreFixtures.TO_JID_STRING)
                .setFrom(CoreFixtures.FROM_JID_STRING)
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .validate();
    }

    @Test
    public void willSetARandomItemId() {

        builder = new Builder();
        builder.setTo(CoreFixtures.TO_JID_STRING)
                .setFrom(CoreFixtures.FROM_JID_STRING)
                .setPubSubNodeId(CoreFixtures.INTEREST_ID)
                .validate();

        assertThat(builder.getItemId().isPresent(), is(true));
    }
}