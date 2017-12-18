package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.type.SubscriptionState;

@SuppressWarnings("ConstantConditions")
public class PubSubSubscriptionResultBuilderTest {
    private static class Builder extends PubSubSubscriptionResultBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
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

        final List<String> errors = new ArrayList<>();
        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setJID("jid")
                .setSubscriptionState(SubscriptionState.SUBSCRIBED)
                .validate();

        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getJID().get(), is("jid"));
        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(builder.getPubSubNodeId().get(), is(CoreFixtures.INTEREST_ID.toPubSubNodeId()));
        assertThat(builder.getSubscriptionState().get(), is(SubscriptionState.SUBSCRIBED));
    }

    @Test
    public void willValidateTheNodeIdIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");

        builder.setJID("jid");

        builder.validate();
    }

    @Test
    public void willValidateTheJidIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'jid' has not been set");

        builder.setInterestId(CoreFixtures.INTEREST_ID);

        builder.validate();
    }

    @Test
    public void willValidateTheStateIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'subscriptionState' has not been set");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setJID(CoreFixtures.FROM_JID_STRING);

        builder.validate();
    }

    @Test
    public void willCheckMandatoryFields() {
        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(builder.getJID(), is(Optional.empty()));
        assertThat(builder.getInterestId(), is(Optional.empty()));
        assertThat(builder.getPubSubNodeId(), is(Optional.empty()));

        assertThat(errors, contains(
                "Invalid pub-sub subscription request stanza; missing node id/interest id",
                "Invalid pub-sub subscription request stanza; missing jid",
                "Invalid pub-sub subscription request stanza; missing subscriptionState"
                ));
    }

    @Test
    public void willCheckThatOnlySubscribedOrNoneIsSupported() {
        final List<String> errors = new ArrayList<>();

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setJID("jid")
                .setSubscriptionState(SubscriptionState.PENDING)
                .validate(errors);

        assertThat(errors, contains(
                "Invalid pub-sub subscription request stanza; subscriptionState should only be subscribed or none"
                ));
    }

    @Test
    public void willNotBuildAPendingRequest() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The only supported requests states are 'subscribed' and 'none'");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setJID("jid")
                .setSubscriptionState(SubscriptionState.PENDING)
                .validate();

    }
}