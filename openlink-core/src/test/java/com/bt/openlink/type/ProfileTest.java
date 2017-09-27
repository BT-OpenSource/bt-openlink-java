package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
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
    public void willCreateAProfile() throws Exception {

        final ProfileId profileId = ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new);
        final Profile profile = Profile.Builder.start()
                .setSite(SITE)
                .setId(profileId)
                .setDefault(true)
                .setDevice("test-device")
                .setLabel("test-label")
                .setOnline(true)
                .build();

        assertThat(profile.getSite().get(), is(SITE));
        assertThat(profile.getId().get(), is(profileId));
        assertThat(profile.isDefault().get(), is(true));
        assertThat(profile.getDevice().get(), is("test-device"));
        assertThat(profile.getLabel().get(), is("test-label"));
        assertThat(profile.isOnline().get(), is(true));
    }

    @Test
    public void willNotCreateAProfileWithoutASite() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site has not been set");

        Profile.Builder.start()
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutAProfileId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The profile id has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutADefaultIndicator() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The default indicator has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutALabel() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The label has not been set");

        Profile.Builder.start()
                .setSite(SITE)
                .setId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .setDefault(false)
                .build();
    }

    @Test
    public void willNotCreateAProfileWithoutAnOnlineIndicator() throws Exception {

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
    public void willCreateAProfileWithoutMandatoryFields() throws Exception {
        final Profile profile = Profile.Builder.start()
                .build(new ArrayList<>());

        assertThat(profile.getSite(), is(Optional.empty()));
        assertThat(profile.getId(), is(Optional.empty()));
        assertThat(profile.isDefault(), is(Optional.empty()));
        assertThat(profile.getDevice(), is(Optional.empty()));
        assertThat(profile.getLabel(), is(Optional.empty()));
        assertThat(profile.isOnline(), is(Optional.empty()));
    }

}