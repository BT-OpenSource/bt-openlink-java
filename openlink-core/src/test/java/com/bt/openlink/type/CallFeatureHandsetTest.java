package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CallFeatureHandsetTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAHandsetFeature() {

        final CallFeatureHandset handset = CallFeatureHandset.Builder.start()
                .setId(FeatureId.from("HS1").get())
                .setLabel("Handset 1")
                .setEnabled(true)
                .setMicrophoneEnabled(true)
                .build();

        assertThat(handset.isEnabled(), is(Optional.of(true)));
        assertThat(handset.isMicrophoneEnabled(), is(Optional.of(true)));
    }

    @Test
    public void willNotBuildAHandsetThatHasNotHadTheEnabledFlagSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The handset enabled flag has not been set");

        CallFeatureHandset.Builder.start()
                .setId(FeatureId.from("HS1").get())
                .setLabel("Handset 1")
                .setMicrophoneEnabled(true)
                .build();
    }

    @Test
    public void willValidateThatTheHandsetHasHadTheEnabledFlagSet() {

        final List<String> errors = new ArrayList<>();

        final CallFeatureHandset handset = CallFeatureHandset.Builder.start()
                .setId(FeatureId.from("HS1").get())
                .setLabel("Handset 1")
                .build(errors);

        assertThat(handset.isEnabled(), is(Optional.empty()));
        assertThat(handset.isMicrophoneEnabled(), is(Optional.empty()));
        assertThat(errors, contains("Invalid feature; the handset enabled flag has not been set"));
    }
}