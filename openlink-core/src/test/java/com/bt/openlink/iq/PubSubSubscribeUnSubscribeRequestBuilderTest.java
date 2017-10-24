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

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class PubSubSubscribeUnSubscribeRequestBuilderTest {
    private static class Builder extends PubSubSubscribeUnSubscribeRequestBuilder<Builder, String, Fixtures.typeEnum> {
        protected Builder() {
            super(Fixtures.typeEnum.class);
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
        builder.setInterestId(Fixtures.INTEREST_ID)
                .setJID("jid");

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getInterestId().get(), is(Fixtures.INTEREST_ID));
        assertThat(builder.getJID().get(), is("jid"));
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

        builder.setInterestId(Fixtures.INTEREST_ID);

        builder.validate();
    }

    @Test
    public void willCheckMandatoryFields() throws Exception {
        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid pub-sub subscription request stanza; missing node id/interest id",
                "Invalid pub-sub subscription request stanza; missing jid"
        ));
    }
}