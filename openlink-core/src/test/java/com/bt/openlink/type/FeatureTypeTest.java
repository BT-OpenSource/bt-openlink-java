package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class FeatureTypeTest {

    @Test
    public void willParseFeatureTypes() throws Exception {

        assertThat(FeatureType.from("Handset").get(), is(FeatureType.HANDSET));
        assertThat(FeatureType.from(null), is(Optional.empty()));
    }

}