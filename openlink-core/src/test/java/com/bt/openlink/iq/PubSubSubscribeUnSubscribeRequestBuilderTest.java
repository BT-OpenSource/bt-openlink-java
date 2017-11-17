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

@SuppressWarnings("ConstantConditions")
public class PubSubSubscribeUnSubscribeRequestBuilderTest {
    private static class Builder extends PubSubSubscribeUnSubscribeRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
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
                .setJID("jid");

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getJID().get(), is("jid"));
        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(builder.getPubSubNodeId().get(), is(CoreFixtures.INTEREST_ID.toPubSubNodeId()));
    }

    @Test
    public void willValidateTheNodeIdIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");

        builder.setJID("jid");

        builder.validate();
    }

    @Test
    public void willValidateTheJidIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'jid' has not been set");

        builder.setInterestId(CoreFixtures.INTEREST_ID);

        builder.validate();
    }

    @Test
    public void willCheckMandatoryFields() throws Exception {
        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(builder.getJID(), is(Optional.empty()));
        assertThat(builder.getInterestId(), is(Optional.empty()));
        assertThat(builder.getPubSubNodeId(), is(Optional.empty()));

        assertThat(errors, contains(
                "Invalid pub-sub subscription request stanza; missing node id/interest id",
                "Invalid pub-sub subscription request stanza; missing jid"
        ));
    }
}