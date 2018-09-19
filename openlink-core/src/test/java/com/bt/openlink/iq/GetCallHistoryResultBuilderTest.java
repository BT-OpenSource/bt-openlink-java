package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CallHistoryFixtures;
import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public class GetCallHistoryResultBuilderTest {
    private static class Builder extends GetCallHistoryResultBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

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
        builder.setFirstRecordNumber(0)
                .setRecordCountInBatch(1)
                .setTotalRecordCount(2)
                .addCalls(Collections.singletonList(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC)))
                .validate();

        builder.validate(errors);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getFirstRecordNumber(), is(Optional.of(0L)));
        assertThat(builder.getRecordCountInBatch(), is(Optional.of(1L)));
        assertThat(builder.getTotalRecordCount(), is(Optional.of(2L)));
    }

    @Test
    public void willValidateTheFirstRecordNumberIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The first record number of the get-call-history result has not been set");

        builder.setRecordCountInBatch(1)
                .setTotalRecordCount(2)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate();
    }

    @Test
    public void willCheckTheFirstRecordNumberIsSet() {

        final List<String> errors = new ArrayList<>();

        builder.setRecordCountInBatch(1)
                .setTotalRecordCount(2)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate(errors);

        assertThat(errors, contains("Invalid call history; missing or invalid first record number"));
    }

    @Test
    public void willValidateTheBatchRecordCountIsSetCorrectly() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The number of records of the get-call-history result is not correctly set");

        builder.setFirstRecordNumber(0)
                .setRecordCountInBatch(2)
                .setTotalRecordCount(3)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate();
    }

    @Test
    public void willCheckTheBatchRecordCountIsSetCorrectly() {

        final List<String> errors = new ArrayList<>();

        builder.setFirstRecordNumber(0)
                .setRecordCountInBatch(2)
                .setTotalRecordCount(3)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate(errors);

        assertThat(errors, contains("Invalid call history; incorrect batch record count"));
    }

    @Test
    public void willSetTheBatchRecordCountWithoutAnException() {

        builder.setFirstRecordNumber(0)
                .setTotalRecordCount(2)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate();

        assertThat(builder.getRecordCountInBatch(), is(Optional.of(1L)));
    }

    @Test
    public void willSetTheBatchRecordCountWithoutAnError() {

        final List<String> errors = new ArrayList<>();

        builder.setFirstRecordNumber(0)
                .setTotalRecordCount(2)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate(errors);

        assertThat(builder.getRecordCountInBatch(), is(Optional.of(1L)));
        assertThat(errors, is(empty()));
    }

    @Test
    public void willValidateTheTotalRecordCountIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The total record count of the get-call-history result has not been set");

        builder.setFirstRecordNumber(0)
                .setRecordCountInBatch(1)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate();
    }

    @Test
    public void willCheckTheTotalRecordCountIsSetCorrectly() {

        final List<String> errors = new ArrayList<>();

        builder.setFirstRecordNumber(0)
                .setRecordCountInBatch(1)
                .addCall(CallHistoryFixtures.getHistoricalCall(CoreFixtures.TSC))
                .validate(errors);

        assertThat(errors, contains("Invalid call history; missing or invalid total record count"));
    }



}