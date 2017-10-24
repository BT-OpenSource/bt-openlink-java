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

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class InterestTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAnInterest() throws Exception {

        final Interest interest = Interest.Builder.start()
                .setId(Fixtures.INTEREST_ID)
                .setType(Fixtures.INTEREST_TYPE)
                .setLabel("test-interest-label")
                .setDefault(true)
                .build();

        assertThat(interest.getId().get(), is(Fixtures.INTEREST_ID));
        assertThat(interest.getType().get(), is(Fixtures.INTEREST_TYPE));
        assertThat(interest.getLabel().get(), is("test-interest-label"));
        assertThat(interest.isDefaultInterest().get(), is(true));
    }

    @Test
    public void willNotBuildAnInterestWithoutAnId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest id has not been set");

        Interest.Builder.start()
                .setType(Fixtures.INTEREST_TYPE)
                .setLabel("test-interest-label")
                .setDefault(true)
                .build();
    }

    @Test
    public void willNotBuildAnInterestWithoutAType() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest type has not been set");

        Interest.Builder.start()
                .setId(Fixtures.INTEREST_ID)
                .setLabel("test-interest-label")
                .setDefault(false)
                .build();
    }

    @Test
    public void willNotBuildAnInterestWithoutALabel() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest label has not been set");

        Interest.Builder.start()
                .setId(Fixtures.INTEREST_ID)
                .setType(Fixtures.INTEREST_TYPE)
                .setDefault(false)
                .build();
    }

    @Test
    public void willNotBuildAnInterestWithoutADefaultIndicator() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest default indicator has not been set");

        Interest.Builder.start()
                .setId(Fixtures.INTEREST_ID)
                .setType(Fixtures.INTEREST_TYPE)
                .setLabel("test-interest-label")
                .build();
    }

    @Test
    public void willBuildAnInterestWithoutMandatoryValues() throws Exception {

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
    }

}