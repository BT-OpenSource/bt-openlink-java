package com.bt.openlink.type;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class ProfileTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willCreateAProfile() throws Exception {

        final Site site = Site.Builder.start()
                .setDefault(true)
                .setId(42)
                .setType(Site.Type.CISCO)
                .setName("test-site-name")
                .build();
        final ProfileId profileId = ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new);
        final Profile profile = Profile.Builder.start()
                .setSite(site)
                .setProfileId(profileId)
                .build();

        assertThat(profile.getSite().get(), is(site));
        assertThat(profile.profileId().get(), is(profileId));
    }

    @Test
    public void willNotCreateAProfileWithoutASite() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site has not been set");

        Profile.Builder.start()
                .setProfileId(ProfileId.from("test-profile-id").orElseThrow(IllegalArgumentException::new))
                .build();

    }

    @Test
    public void willNotCreateAProfileWithoutAProfileId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The profileId has not been set");

        final Site site = Site.Builder.start()
                .setDefault(true)
                .setId(42)
                .setType(Site.Type.CISCO)
                .setName("test-site-name")
                .build();
        Profile.Builder.start()
                .setSite(site)
                .build();

    }
}