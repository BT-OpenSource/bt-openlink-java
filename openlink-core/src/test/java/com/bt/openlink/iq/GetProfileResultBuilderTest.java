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

public class GetProfileResultBuilderTest {

    private static class Builder extends GetProfileResultBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

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
        builder.setProfile(CoreFixtures.PROFILE);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getProfile().get(), is(CoreFixtures.PROFILE));
    }

    @Test
    public void willValidateTheProfileIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-profile result 'profile' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatTheProfileIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-profile result stanza; missing 'profile'"));
        assertThat(builder.getProfile(), is(Optional.empty()));
    }
}