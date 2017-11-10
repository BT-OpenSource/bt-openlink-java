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
public class CallFeatureTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildACallFeatureWithAnEnabledFlag() throws Exception {

        final CallFeature feature = CallFeature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-feature-label")
                .setEnabled(true)
                .build();

        assertThat(feature.isEnabled().get(), is(true));
        assertThat(feature.getDeviceKey(), is(Optional.empty()));
    }

    @Test
    public void willBuildACallFeatureWithADeviceKey() throws Exception {

        final CallFeature feature = CallFeature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-feature-label")
                .setDeviceKey(DeviceKey.from("test-device-key").get())
                .build();

        assertThat(feature.getDeviceKey(), is(DeviceKey.from("test-device-key")));
        assertThat(feature.isEnabled(), is(Optional.empty()));
    }

    @Test
    public void willNotBuildACallFeatureWithAnEnableAndADeviceKey() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The enabled flag and the device keys cannot both be set");

        CallFeature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-label")
                .setEnabled(true)
                .setDeviceKey(DeviceKey.from("test-device-key").get())
                .build();
    }

    @Test
    public void willNotBuildACallFeatureWithoutEnabledOrADeviceKey() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Either the enabled flag or the device keys must be set");

        CallFeature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-label")
                .build();
    }

    @Test
    public void willBuildACallFeatureWithoutMandatoryValues() throws Exception {

        final List<String> errors = new ArrayList<>();

        final CallFeature feature = CallFeature.Builder.start()
                .build(errors);

        assertThat(feature.getDeviceKey(),is(Optional.empty()));
        assertThat(feature.isEnabled(),is(Optional.empty()));
        assertThat(errors, containsInAnyOrder(
                "Invalid feature; missing feature id is mandatory",
                "Invalid feature; missing feature type is mandatory",
                "Invalid feature; missing feature label is mandatory",
                "Invalid feature; either the enabled flag or the device keys must be set"
                ));
    }

    @Test
    public void willBuildACallFeatureWithMultipleAttributes() throws Exception {

        final List<String> errors = new ArrayList<>();

        final CallFeature feature = CallFeature.Builder.start()
                .setId(Fixtures.FEATURE_ID)
                .setType(FeatureType.CALL_FORWARD)
                .setLabel("test-label")
                .setEnabled(true)
                .setDeviceKey(DeviceKey.from("test-device-key").get())
                .build(errors);

        assertThat(feature.getDeviceKey(),is(DeviceKey.from("test-device-key")));
        assertThat(feature.isEnabled().get(),is(true));
        assertThat(errors, containsInAnyOrder(
                "Invalid feature; the enabled flag and the device keys cannot both be set"
                ));
    }
}