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
import com.bt.openlink.GetFeaturesFixtures;

@SuppressWarnings("ConstantConditions")
public class GetFeaturesResultBuilderTest {

    private static class Builder extends GetFeaturesResultBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(builder.getFeatures(), contains(GetFeaturesFixtures.FEATURE_HANDSET_1));
    }

    @Test
    public void willValidateProfileIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-features result profile has not been set");

        builder.validate();
    }

    @Test
    public void willValidateProfileUniqueness() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each feature id must be unique - hs_1 appears more than once");

        builder.setProfileId(CoreFixtures.PROFILE_ID)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1)
                .addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1);

        builder.validate();
    }

    @Test
    public void willCheckProfileAndUniqueness() {

        final List<String> errors = new ArrayList<>();

        builder.addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1);
        builder.addFeature(GetFeaturesFixtures.FEATURE_HANDSET_1);

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid get-features result stanza; missing profile",
                "Invalid get-features result stanza; each feature id must be unique - hs_1 appears more than once"
        ));
    }
}