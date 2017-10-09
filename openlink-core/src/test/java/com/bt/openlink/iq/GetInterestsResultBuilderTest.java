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

public class GetInterestsResultBuilderTest {

    private static class Builder extends GetInterestsResultBuilder<Builder, String, Fixtures.typeEnum> {
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
        builder.addInterest(Fixtures.INTEREST);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getInterests(), contains(Fixtures.INTEREST));
    }

    @Test
    public void willValidateProfileUniqueness() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each interest id must be unique - test-interest-id appears more than once");

        builder.addInterest(Fixtures.INTEREST);
        builder.addInterest(Fixtures.INTEREST);

        builder.validate();
    }

    @Test
    public void willCheckProfileUniqueness() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.addInterest(Fixtures.INTEREST);
        builder.addInterest(Fixtures.INTEREST);

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-interests result stanza; each interest id must be unique - test-interest-id appears more than once"));
    }
}