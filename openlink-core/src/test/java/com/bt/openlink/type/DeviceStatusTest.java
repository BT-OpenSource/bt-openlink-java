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

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public class DeviceStatusTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildADeviceStatus() {

        final DeviceStatus deviceStatus = DeviceStatus.Builder.start()
                .setOnline(true)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setDeviceId("test-device-id")
                .build();

        assertThat(deviceStatus.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(deviceStatus.isOnline().get(), is(true));
        assertThat(deviceStatus.getDeviceId().get(), is("test-device-id"));
    }

    @Test
    public void willNotBuildADeviceStatusWithoutAProfileId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The device profile has not been set");

        DeviceStatus.Builder.start()
                .build();
    }

    @Test
    public void willReturnParseErrors() {

        final List<String> parseErrors = new ArrayList<>();

        final DeviceStatus deviceStatus = DeviceStatus.Builder.start()
                .build(parseErrors);

        assertThat(parseErrors, containsInAnyOrder("Invalid device status; missing profile is mandatory"));
        assertThat(deviceStatus.getProfileId(), is(Optional.empty()));
        assertThat(deviceStatus.isOnline(), is(Optional.empty()));
        assertThat(deviceStatus.getDeviceId(), is(Optional.empty()));

    }
}