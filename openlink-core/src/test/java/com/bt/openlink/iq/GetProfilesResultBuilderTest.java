package com.bt.openlink.iq;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.Fixtures;

public class GetProfilesResultBuilderTest {

    private static class ConcreteGetProfilesResultBuilder extends GetProfilesResultBuilder<GetProfilesResultBuilder, String, Fixtures.typeEnum> {
        protected ConcreteGetProfilesResultBuilder() {
            super(Fixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private ConcreteGetProfilesResultBuilder builder;

    @Before
    public void setUp() throws Exception {

        builder = new ConcreteGetProfilesResultBuilder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        builder.addProfile(Fixtures.PROFILE);

        builder.validate();

        assertThat(builder.getProfiles(), contains(Fixtures.PROFILE));
    }

    @Test
    public void willValidateProfileUniqueness() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each profile id must be unique - test-profile-id appears more than once");

        builder.addProfile(Fixtures.PROFILE);
        builder.addProfile(Fixtures.PROFILE);

        builder.validate();
    }

    @Test
    public void willCheckProfileUniqueness() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.addProfile(Fixtures.PROFILE);
        builder.addProfile(Fixtures.PROFILE);

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-profiles request stanza; each profile id must be unique - test-profile-id appears more than once"));
    }
}