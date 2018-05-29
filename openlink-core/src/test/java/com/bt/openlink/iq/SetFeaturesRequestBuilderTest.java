package com.bt.openlink.iq;

import com.bt.openlink.CoreFixtures;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

@SuppressWarnings("ConstantConditions")
public class SetFeaturesRequestBuilderTest {

    private static class Builder extends SetFeaturesRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.setFeatureId(CoreFixtures.FEATURE_ID);
        builder.setValue1(true);
        builder.setProfileId(CoreFixtures.PROFILE_ID);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getFeatureId().get(), is(CoreFixtures.FEATURE_ID));
    }

    @Test
    public void willValidateTheFeatureIdIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The set-features request 'featureId' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckthatAllMandatoryValuesAreSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid set-features request stanza; missing featureId",
                "Invalid set-features request stanza; missing value1",
                "Invalid set-features request stanza; missing profileId"
                ));
        assertThat(builder.getFeatureId(), is(Optional.empty()));
    }
}