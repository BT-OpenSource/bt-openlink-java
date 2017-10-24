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
public class MakeCallResultBuilderTest {

    private static class Builder extends MakeCallResultBuilder<Builder, String, Fixtures.typeEnum> {
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
        builder.addCall(Fixtures.CALL)
                .validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getCalls(), contains(Fixtures.CALL));
    }

    @Test
    public void willAddMultipleCalls() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.addCall(Fixtures.CALL)
                .addCall(Fixtures.CALL)
                .validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getCalls(), contains(Fixtures.CALL, Fixtures.CALL));
    }

    @Test
    public void willValidateTheCallIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");

        builder.validate();
    }

    @Test
    public void willCheckThatTheCallIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid make-call result stanza; missing or invalid calls"));
        assertThat(builder.getCalls(), is(empty()));
    }

}