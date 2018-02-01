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

@SuppressWarnings("ConstantConditions")
public class ProfileTest {

    private static final Site SITE = Site.Builder.start()
            .setDefault(true)
            .setId(42)
            .setType(Site.Type.CISCO)
            .setName("test-site-name")
            .build();
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willCreateAProfile() {

        final ProfileId profileId = ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new);
        final Profile profile = Profile.Builder.start()
                .setSite(SITE)
                .setId(profileId)
                .setDefault(true)
                .setDeviceType("test-device-type")
                .setDeviceId("test-device-id")
                .setLabel("test-label")
                .addAction(RequestAction.ANSWER_CALL)
                .setOnline(true)
                .build();

        assertThat(profile.getSite().get(), is(SITE));
        assertThat(profile.getId().get(), is(profileId));
        assertThat(profile.isDefaultProfile().get(), is(true));
        assertThat(profile.getDeviceType().get(), is("test-device-type"));
        assertThat(profile.getDeviceId().get(), is("test-device-id"));
        assertThat(profile.getLabel().get(), is("test-label"));
        assertThat(profile.isOnline().get(), is(true));
        assertThat(profile.getActions().size(), is(1));
        assertThat(profile.getActions().get(0), is(RequestAction.ANSWER_CALL));
    }

    @Test
    public void willNotCreateAProfileWithoutASite() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site has not been set");

        Profile.Builder.start()
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutAProfileId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The profile id has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutADefaultIndicator() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The default indicator has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutALabel() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The label has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .setDefault(false)
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutAnOnlineIndicator() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The online indicator has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .setDefault(false)
                .setLabel("test-label")
                .build();
    }

    @Test
    public void willCreateAProfileWithoutMandatoryFields() {

        final List<String> errors = new ArrayList<>();

        final Profile profile = Profile.Builder.start()
                .build(errors);

        assertThat(profile.getSite(), is(Optional.empty()));
        assertThat(profile.getId(), is(Optional.empty()));
        assertThat(profile.isDefaultProfile(), is(Optional.empty()));
        assertThat(profile.getDeviceType(), is(Optional.empty()));
        assertThat(profile.getDeviceId(), is(Optional.empty()));
        assertThat(profile.getLabel(), is(Optional.empty()));
        assertThat(profile.isOnline(), is(Optional.empty()));
        assertThat(errors, contains(
                "Invalid profile; missing profile id is mandatory",
                "Invalid profile; missing site is mandatory",
                "Invalid profile; missing default indicator is mandatory",
                "Invalid profile; missing label is mandatory",
                "Invalid profile; missing online indicator is mandatory"));
    }
}