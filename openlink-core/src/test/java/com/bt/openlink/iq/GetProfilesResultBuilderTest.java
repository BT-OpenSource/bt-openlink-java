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

import com.bt.openlink.CoreFixtures;

public class GetProfilesResultBuilderTest {

    private static class Builder extends GetProfilesResultBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.addProfile(CoreFixtures.PROFILE);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getProfiles(), contains(CoreFixtures.PROFILE));
    }

    @Test
    public void willValidateProfileUniqueness() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each profile id must be unique - test-profile-id appears more than once");

        builder.addProfile(CoreFixtures.PROFILE);
        builder.addProfile(CoreFixtures.PROFILE);

        builder.validate();
    }

    @Test
    public void willCheckProfileUniqueness() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.addProfile(CoreFixtures.PROFILE);
        builder.addProfile(CoreFixtures.PROFILE);

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-profiles result stanza; each profile id must be unique - test-profile-id appears more than once"));
    }
}