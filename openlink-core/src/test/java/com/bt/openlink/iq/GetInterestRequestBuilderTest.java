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
public class GetInterestRequestBuilderTest {

    private static class Builder extends GetInterestRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.setInterestId(CoreFixtures.INTEREST_ID);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
    }

    @Test
    public void willValidateTheInterestIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-interest request 'interestId' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatTheInterestIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-interest request stanza; missing 'interest'"));
        assertThat(builder.getInterestId(), is(Optional.empty()));
    }

}