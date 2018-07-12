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
public class InterestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAnInterest() {

        final Interest interest = Interest.Builder.start()
                .setId(CoreFixtures.INTEREST_ID)
                .setType(CoreFixtures.INTEREST_TYPE)
                .setLabel("test-interest-label")
                .setDefault(true)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .setNumber(CoreFixtures.INTEREST_NUMBER)
                .build();

        assertThat(interest.getId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(interest.getType().get(), is(CoreFixtures.INTEREST_TYPE));
        assertThat(interest.getLabel().get(), is("test-interest-label"));
        assertThat(interest.isDefaultInterest().get(), is(true));
        assertThat(interest.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
        assertThat(interest.getNumber().get(), is(CoreFixtures.INTEREST_NUMBER));
    }

    @Test
    public void willNotBuildAnInterestWithoutAnId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest id has not been set");

        Interest.Builder.start()
                .setType(CoreFixtures.INTEREST_TYPE)
                .setLabel("test-interest-label")
                .setDefault(true)
                .build();
    }

    @Test
    public void willNotBuildAnInterestWithoutAType() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest type has not been set");

        Interest.Builder.start()
                .setId(CoreFixtures.INTEREST_ID)
                .setLabel("test-interest-label")
                .setDefault(false)
                .build();
    }

    @Test
    public void willNotBuildAnInterestWithoutALabel() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest label has not been set");

        Interest.Builder.start()
                .setId(CoreFixtures.INTEREST_ID)
                .setType(CoreFixtures.INTEREST_TYPE)
                .setDefault(false)
                .build();
    }

    @Test
    public void willNotBuildAnInterestWithoutADefaultIndicator() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest default indicator has not been set");

        Interest.Builder.start()
                .setId(CoreFixtures.INTEREST_ID)
                .setType(CoreFixtures.INTEREST_TYPE)
                .setLabel("test-interest-label")
                .build();
    }

    @Test
    public void willBuildAnInterestWithoutMandatoryValues() {

        final List<String> errors = new ArrayList<>();

        final Interest interest = Interest.Builder.start()
                .build(errors);

        assertThat(errors, contains(
                "Invalid interest; missing interest id is mandatory",
                "Invalid interest; missing interest type is mandatory",
                "Invalid interest; missing interest label is mandatory",
                "Invalid interest; missing interest default indicator is mandatory"
        ));
        assertThat(interest.getId(), is(Optional.empty()));
        assertThat(interest.getType(), is(Optional.empty()));
        assertThat(interest.getLabel(), is(Optional.empty()));
        assertThat(interest.isDefaultInterest(), is(Optional.empty()));
        assertThat(interest.getCallStatus(), is(Optional.empty()));
    }

}