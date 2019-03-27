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

public class GetProfileRequestBuilderTest {

    private static class Builder extends GetProfileRequestBuilder<GetProfileRequestBuilderTest.Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private GetProfileRequestBuilderTest.Builder builder;

    @Before
    public void setUp() throws Exception {

        builder = new GetProfileRequestBuilderTest.Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setProfileId(CoreFixtures.PROFILE_ID);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void willValidateTheProfileIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-profile request 'profileId' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatTheProfileIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-profile request stanza; missing 'profile'"));
        assertThat(builder.getProfileId(), is(Optional.empty()));
    }

}