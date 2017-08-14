package com.bt.openlink.type;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class SiteTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildASite() throws Exception {

        final Site site = Site.Builder.start()
                .setName("test-site-name")
                .setType(Site.Type.BTSM)
                .setId(42)
                .setDefault(true)
                .build();

        assertThat(site.getName().get(), is("test-site-name"));
        assertThat(site.getId().get(), is(42L));
        assertThat(site.isDefault().get(), is(true));
        assertThat(site.getType().get(), is(Site.Type.BTSM));
    }

    @Test
    public void willNotBuildASiteWithoutAName() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site name has not been set");

        Site.Builder.start()
                .setType(Site.Type.BTSM)
                .setId(42)
                .setDefault(true)
                .build();

    }

    @Test
    public void willNotBuildASiteWithoutAType() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site type has not been set");

        Site.Builder.start()
                .setName("test-site-name")
                .setId(42)
                .setDefault(true)
                .build();

    }

    @Test
    public void willNotBuildASiteWithoutAnId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site id has not been set");

        Site.Builder.start()
                .setName("test-site-name")
                .setType(Site.Type.CISCO)
                .setDefault(true)
                .build();

    }

    @Test
    public void willNotBuildASiteWithoutADefaultIndicator() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The site default indicator has not been set");

        Site.Builder.start()
                .setName("test-site-name")
                .setType(Site.Type.CISCO)
                .setId(42)
                .build();

    }
}