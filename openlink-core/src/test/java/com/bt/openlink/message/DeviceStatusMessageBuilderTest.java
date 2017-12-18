package com.bt.openlink.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.type.DeviceStatus;

@SuppressWarnings("ConstantConditions")
public class DeviceStatusMessageBuilderTest {

    private static class Builder extends DeviceStatusMessageBuilder<Builder, String> {
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
        builder.setPubSubNodeId(CoreFixtures.INTEREST_ID);
    }

    @Test
    public void willValidateAPopulatedBuilder() {

        builder.setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .validate();

        final DeviceStatus deviceStatus = builder.getDeviceStatus().get();
        assertThat(deviceStatus.isOnline().get(), is(true));
        assertThat(deviceStatus.getProfileId().get(), is(CoreFixtures.PROFILE_ID));

    }

    @Test
    public void willNotValidateABuilderWithoutADeviceStatus() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'deviceStatus' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckABuilderHasADeviceStatus() {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors,containsInAnyOrder("Invalid devicestatus message stanza; the 'deviceStatus' has not been set"));
    }

}