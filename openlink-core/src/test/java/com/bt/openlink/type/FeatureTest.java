package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class FeatureTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAFeatureWithAnEnabledFlag() throws Exception {

        final Feature feature = Feature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-feature-label")
                .build();

        assertThat(feature.getId().get(), is(Fixtures.FEATURE_ID));
        assertThat(feature.getType().get(), is(FeatureType.CALL_FORWARD));
        assertThat(feature.getLabel().get(), is("test-feature-label"));
    }

    @Test
    public void willNotBuildAFeatureWithoutAnId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The feature id has not been set");

        Feature.Builder.start()
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-feature-label")
                .build();
    }

    @Test
    public void willNotBuildAFeatureWithoutAType() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The feature type has not been set");

        Feature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setLabel("test-feature-label")
                .build();
    }

    @Test
    public void willNotBuildAFeatureWithoutALabel() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The feature label has not been set");

        Feature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .build();
    }

    @Test
    public void willBuildAFeatureWithoutMandatoryValues() throws Exception {

        final List<String> errors = new ArrayList<>();

        final Feature feature = Feature.Builder.start()
                .build(errors);

        assertThat(feature.getId(), is(Optional.empty()));
        assertThat(feature.getType(), is(Optional.empty()));
        assertThat(feature.getLabel(), is(Optional.empty()));
        assertThat(errors, containsInAnyOrder(
                "Invalid feature; missing feature id is mandatory",
                "Invalid feature; missing feature type is mandatory",
                "Invalid feature; missing feature label is mandatory"
                ));
    }
}