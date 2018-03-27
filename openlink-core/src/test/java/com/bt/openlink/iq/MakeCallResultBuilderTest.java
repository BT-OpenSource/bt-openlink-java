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
public class MakeCallResultBuilderTest {

    private static class Builder extends MakeCallResultBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.setCallStatus(CoreFixtures.CALL_STATUS)
                .validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willValidateTheCallStatusIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");

        builder.validate();
    }

    @Test
    public void willCheckThatTheCallStatusIsSet() {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid make-call result stanza; missing or invalid callstatus"));
        assertThat(builder.getCallStatus(), is(Optional.empty()));
    }

}