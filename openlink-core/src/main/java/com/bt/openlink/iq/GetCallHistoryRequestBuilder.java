package com.bt.openlink.iq;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.CallType;

public abstract class GetCallHistoryRequestBuilder<B extends GetCallHistoryRequestBuilder, J, T extends Enum<T>> extends IQBuilder<B, J, T> {

    @Nullable J jid;
    @Nullable private String caller;
    @Nullable private String called;
    @Nullable private CallType callType;
    @Nullable private LocalDate fromDate;
    @Nullable private LocalDate upToDate;
    @Nullable private Long start;
    @Nullable private Long count;

    protected GetCallHistoryRequestBuilder(final Class<T> typeClass) {
        super(typeClass);
    }

    @Nonnull
    @Override
    public String getExpectedIQType() {
        return "set";
    }

    @Override
    protected void validate() {
        super.validate();
        if (fromDate != null && upToDate != null && upToDate.isBefore(fromDate)) {
            throw new IllegalStateException("The get-call-history request upToDate cannot be before the fromDate");
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
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setJID(@Nonnull final J jid) {
        this.jid = jid;
        return (B) this;
    }

    @Nonnull
    public Optional<J> getJID() {
        return Optional.ofNullable(jid);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCaller(@Nonnull final String caller) {
        this.caller = caller;
        return (B) this;
    }

    @Nonnull
    public Optional<String> getCaller() {
        return Optional.ofNullable(caller);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCalled(@Nonnull final String called) {
        this.called = called;
        return (B) this;
    }

    @Nonnull
    public Optional<String> getCalled() {
        return Optional.ofNullable(called);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCallType(@Nonnull final CallType callType) {
        this.callType = callType;
        return (B) this;
    }

    @Nonnull
    public Optional<CallType> getCallType() {
        return Optional.ofNullable(callType);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setFromDate(@Nonnull final LocalDate fromDate) {
        this.fromDate = fromDate;
        return (B) this;
    }

    @Nonnull
    public Optional<LocalDate> getFromDate() {
        return Optional.ofNullable(fromDate);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setUpToDate(@Nonnull final LocalDate upToDate) {
        this.upToDate = upToDate;
        return (B) this;
    }

    @Nonnull
    public Optional<LocalDate> getUpToDate() {
        return Optional.ofNullable(upToDate);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setStart(@Nonnull final Long start) {
        this.start = start;
        return (B) this;
    }

    @Nonnull
    public Optional<Long> getStart() {
        return Optional.ofNullable(start);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public B setCount(@Nonnull final Long count) {
        this.count = count;
        return (B) this;
    }

    @Nonnull
    public Optional<Long> getCount() {
        return Optional.ofNullable(count);
    }
}