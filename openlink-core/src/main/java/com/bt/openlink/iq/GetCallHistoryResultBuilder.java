package com.bt.openlink.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.HistoricalCall;

public abstract class GetCallHistoryResultBuilder<B extends GetCallHistoryResultBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable private Long totalRecordCount;
    @Nullable private Long firstRecordNumber;
    @Nullable private Long recordCountInBatch;
    @Nonnull private List<HistoricalCall<J>> calls = new ArrayList<>();

    protected GetCallHistoryResultBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "result";
    }

    @SuppressWarnings("unchecked")
    public B setTotalRecordCount(final long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B setFirstRecordNumber(final long firstRecordNumber) {
        this.firstRecordNumber = firstRecordNumber;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B setRecordCountInBatch(final long recordCountInBatch) {
        this.recordCountInBatch = recordCountInBatch;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B addCall(@Nonnull final HistoricalCall<J> call) {
        calls.add(call);
        return (B) this;
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    @Nonnull
    public B addCalls(@Nonnull final List<HistoricalCall<J>> calls) {
        this.calls.addAll(calls);
        return (B) this;
    }

    @Nonnull
    public Optional<Long> getTotalRecordCount() {
        return Optional.ofNullable(totalRecordCount);
    }

    @Nonnull
    public Optional<Long> getFirstRecordNumber() {
        return Optional.ofNullable(firstRecordNumber);
    }

    @Nonnull
    public Optional<Long> getRecordCountInBatch() {
        return Optional.ofNullable(recordCountInBatch);
    }

    @Nonnull
    public List<HistoricalCall<J>> getCalls() {
        return calls;
    }

    @Override
    protected void validate() {
        super.validate();
        if (totalRecordCount == null) {
            throw new IllegalStateException("The total record count of the get-call-history result has not been set");
        }
        if (firstRecordNumber == null) {
            throw new IllegalStateException("The first record number of the get-call-history result has not been set");
        }
        if (recordCountInBatch == null) {
            recordCountInBatch = (long) calls.size();
        } else if (recordCountInBatch != calls.size()) {
            throw new IllegalStateException("The number of records of the get-call-history result is not correctly set");
        }
    }

    @Override
    public void validate(final List<String> errors) {
        validate(errors, true);
    }

    protected void validate(List<String> errors, boolean checkIQFields) {
        if (checkIQFields) {
            super.validate(errors);
        }
        if (totalRecordCount == null) {
            errors.add("Invalid call history; missing or invalid total record count");
        }
        if (firstRecordNumber == null) {
            errors.add("Invalid call history; missing or invalid first record number");
        }
        if (recordCountInBatch == null) {
            recordCountInBatch = (long) calls.size();
        } else if (recordCountInBatch != calls.size()) {
            errors.add("Invalid call history; incorrect batch record count");
        }
    }

}
