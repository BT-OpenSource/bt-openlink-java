package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GetCallHistoryRequestBuilderTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private GetCallHistoryRequestBuilder<GetCallHistoryRequestBuilder, String, String> builder;

    @Before
    public void setUp() throws Exception {

        builder = new GetCallHistoryRequestBuilder<GetCallHistoryRequestBuilder, String, String>() {
            @Nonnull
            @Override
            public String getExpectedIQType() {
                return "set";
            }
        };

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
        builder.setIQType("set");
    }

    @Test
    public void willValidateAnUnpopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate();
        builder.validate(errors);

        assertThat(errors,is(empty()));

        assertThat(builder.getJID(), is(Optional.empty()));
        assertThat(builder.getCaller(), is(Optional.empty()));
        assertThat(builder.getCalled(), is(Optional.empty()));
        assertThat(builder.getCallType(), is(Optional.empty()));
        assertThat(builder.getFromDate(), is(Optional.empty()));
        assertThat(builder.getFromDate(), is(Optional.empty()));
        assertThat(builder.getUpToDate(), is(Optional.empty()));
        assertThat(builder.getStart(), is(Optional.empty()));
        assertThat(builder.getCount(), is(Optional.empty()));
    }

    @Test
    public void willValidateThatTheEndDateIsNotBeforeTheUpToDate() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-call-history request upToDate cannot be before the fromDate");

        builder.setUpToDate(LocalDate.now());
        builder.setFromDate(LocalDate.now().plusWeeks(1));

        builder.validate();
    }
}