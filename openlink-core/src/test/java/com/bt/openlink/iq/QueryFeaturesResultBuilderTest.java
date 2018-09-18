package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.QueryFeaturesFixtures;

public class QueryFeaturesResultBuilderTest {

    private static class Builder extends QueryFeaturesResultBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    private Builder builder;

    @Before
    public void setUp() {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() {

        final List<String> errors = new ArrayList<>();
        builder.addFeature(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getFeatures(), contains(QueryFeaturesFixtures.CALL_FORWARD_ACTIVE_FEATURE));
    }


}