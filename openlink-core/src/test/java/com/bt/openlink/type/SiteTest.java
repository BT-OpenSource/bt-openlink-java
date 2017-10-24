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
    public void willCheckMandatoryFieldsArePresent() throws Exception {

        final List<String> errors = new ArrayList<>();

        final Site site = Site.Builder.start()
                .build(errors);

        assertThat(errors, contains(
                "Invalid site; missing site id is mandatory",
                "Invalid site; missing site type is mandatory",
                "Invalid site; missing site name is mandatory"
        ));
        assertThat(site.getName(), is(Optional.empty()));
        assertThat(site.getId(), is(Optional.empty()));
        assertThat(site.isDefault(), is(Optional.empty()));
        assertThat(site.getType(), is(Optional.empty()));
    }

    @Test
    public void willParseSiteTypes() throws Exception {

        assertThat(Site.Type.from("BTSM").get(), is(Site.Type.BTSM));
        assertThat(Site.Type.from(null), is(Optional.empty()));
    }
}