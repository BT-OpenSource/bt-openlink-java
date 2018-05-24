package com.bt.openlink.iq;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.type.DeviceStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class ManageVoiceMessageResultBuilderTest {

    private static class Builder extends ManageVoiceMessageResultBuilder<ManageVoiceMessageResultBuilder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ManageVoiceMessageResultBuilderTest.Builder builder;

    @Before
    public void setUp() {

        builder = new ManageVoiceMessageResultBuilderTest.Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
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