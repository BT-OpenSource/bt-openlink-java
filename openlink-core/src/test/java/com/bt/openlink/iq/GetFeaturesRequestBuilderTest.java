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
public class GetFeaturesRequestBuilderTest {

    private static class Builder extends GetFeaturesRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.setProfileId(CoreFixtures.PROFILE_ID);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
    }

    @Test
    public void willValidateTheProfileIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-features request 'profileId' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatProfileIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-features request stanza; missing profile id"));
        assertThat(builder.getProfileId(), is(Optional.empty()));
    }

}