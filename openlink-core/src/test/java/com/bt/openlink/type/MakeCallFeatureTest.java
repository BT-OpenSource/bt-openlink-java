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

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public class MakeCallFeatureTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAMakeCallFeature() {

        final MakeCallFeature makeCallFeature = MakeCallFeature.Builder.start()
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1("value1")
                .setValue2("value2")
                .build();

        assertThat(makeCallFeature.getFeatureId().get(), is(CoreFixtures.FEATURE_ID));
        assertThat(makeCallFeature.getValue1().get(), is("value1"));
        assertThat(makeCallFeature.getBooleanValue1(), is(Optional.empty()));
        assertThat(makeCallFeature.getLongValue1(), is(Optional.empty()));
        assertThat(makeCallFeature.getValue2().get(), is("value2"));
        assertThat(makeCallFeature.getBooleanValue2(), is(Optional.empty()));
        assertThat(makeCallFeature.getPhoneNumberValue2(), is(PhoneNumber.from("value2")));
    }

    @Test
    public void willValidateAMakeCallFeatureHasATypeSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The feature id has not been set");

        MakeCallFeature.Builder.start()
                .build();
    }

    @Test
    public void willBuildAMakeCallFeatureWithoutAType() {

        final List<String> errors = new ArrayList<>();

        final MakeCallFeature makeCallFeature = MakeCallFeature.Builder.start()
                .build(errors);

        assertThat(errors, contains("The feature id has not been set"));
        assertThat(makeCallFeature.getFeatureId(), is(Optional.empty()));
    }

    @Test
    public void willReturnBooleans() {
        final MakeCallFeature makeCallFeature = MakeCallFeature.Builder.start()
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1(true)
                .setValue2(false)
                .build();

        assertThat(makeCallFeature.getValue1().get(), is("true"));
        assertThat(makeCallFeature.getBooleanValue1().get(), is(true));
        assertThat(makeCallFeature.getValue2().get(), is("false"));
        assertThat(makeCallFeature.getBooleanValue2().get(), is(false));
    }

    @Test
    public void willReturnLong() {
        final MakeCallFeature makeCallFeature = MakeCallFeature.Builder.start()
                .setFeatureId(CoreFixtures.FEATURE_ID)
                .setValue1(42)
                .build();

        assertThat(makeCallFeature.getValue1().get(), is("42"));
        assertThat(makeCallFeature.getLongValue1().get(), is(42L));
    }
}