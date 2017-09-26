package com.bt.openlink.tinder.iq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.CallType;

public class GetCallHistoryRequest extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "get-call-history request";
    private static final String DATE_PATTERN = "MM/dd/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Nullable private final JID jid;
    @Nullable private final String caller;
    @Nullable private final String called;
    @Nullable private final CallType callType;
    @Nullable private final LocalDate fromDate;
    @Nullable private final LocalDate upToDate;
    @Nullable private final Integer start;
    @Nullable private final Integer count;

    private GetCallHistoryRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.jid;
        this.caller = builder.caller;
        this.called = builder.called;
        this.callType = builder.callType;
        this.fromDate = builder.fromDate;
        this.upToDate = builder.upToDate;
        this.start = builder.start;
        this.count = builder.count;
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "jid", jid);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "caller", caller);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "called", called);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "calltype", callType == null ? null : callType.getLabel());
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "fromdate", fromDate, DATE_FORMATTER);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "uptodate", upToDate, DATE_FORMATTER);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "start", start);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "count", count);
    }

    @Nonnull
    public Optional<JID> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public Optional<String> getCaller() {
        return Optional.ofNullable(caller);
    }

    @Nonnull
    public Optional<String> getCalled() {
        return Optional.ofNullable(called);
    }

    @Nonnull
    public Optional<CallType> getCallType() {
        return Optional.ofNullable(callType);
    }

    @Nonnull
    public Optional<LocalDate> getFromDate() {
        return Optional.ofNullable(fromDate);
    }

    @Nonnull
    public Optional<LocalDate> getUpToDate() {
        return Optional.ofNullable(upToDate);
    }

    @Nonnull
    public Optional<Integer> getStart() {
        return Optional.ofNullable(start);
    }

    @Nonnull
    public Optional<Integer> getCount() {
        return Optional.ofNullable(count);
    }

    @Nonnull
    public static GetCallHistoryRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getChildElementString(inElement,
                "jid", false, STANZA_DESCRIPTION, parseErrors));
        final Optional<String> caller = Optional.ofNullable(TinderPacketUtil.getChildElementString(inElement,
                "caller", false, STANZA_DESCRIPTION, parseErrors));
        final Optional<String> called = Optional.ofNullable(TinderPacketUtil.getChildElementString(inElement,
                "called", false, STANZA_DESCRIPTION, parseErrors));
        final String callTypeString = TinderPacketUtil.getChildElementString(inElement,
                "calltype", false, STANZA_DESCRIPTION, parseErrors);
        final Optional<CallType> callType = CallType.from(callTypeString);
        if(!callType.isPresent() && callTypeString != null && !callTypeString.isEmpty()) {
            parseErrors.add(String.format("Invalid %s; invalid calltype - '%s' should be 'in', 'out' or 'missed'", STANZA_DESCRIPTION, callTypeString));
        }
        final Optional<LocalDate> fromDate = Optional.ofNullable(TinderPacketUtil.getChildElementLocalDate(inElement,
                "fromdate", DATE_FORMATTER, false, STANZA_DESCRIPTION, DATE_PATTERN, parseErrors));
        final Optional<LocalDate> upToDate = Optional.ofNullable(TinderPacketUtil.getChildElementLocalDate(inElement,
                "uptodate", DATE_FORMATTER, false, STANZA_DESCRIPTION, DATE_PATTERN, parseErrors));
        final Optional<Integer> start = Optional.ofNullable(TinderPacketUtil.getChildElementInteger(inElement,
                "start", false, STANZA_DESCRIPTION, parseErrors));
        final Optional<Integer> count = Optional.ofNullable(TinderPacketUtil.getChildElementInteger(inElement,
                "count", false, STANZA_DESCRIPTION, parseErrors));
        final Builder builder = Builder.start(iq);
        jid.ifPresent(builder::setJID);
        caller.ifPresent(builder::setCaller);
        called.ifPresent(builder::setCalled);
        callType.ifPresent(builder::setCallType);
        fromDate.ifPresent(builder::setFromDate);
        upToDate.ifPresent(builder::setUpToDate);
        start.ifPresent(builder::setStart);
        count.ifPresent(builder::setCount);
        final GetCallHistoryRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            return new Builder(iq);
        }

        @Nullable JID jid;
        @Nullable private String caller;
        @Nullable private String called;
        @Nullable private CallType callType;
        @Nullable private LocalDate fromDate;
        @Nullable private LocalDate upToDate;
        @Nullable private Integer start;
        @Nullable private Integer count;

        private Builder() {
        }

        public Builder(@Nonnull final IQ iq) {
            super(iq);
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.set;
        }

        @Nonnull
        public GetCallHistoryRequest build() {
            super.validateBuilder();
            return build(Collections.emptyList());
        }

        @Nonnull
        private GetCallHistoryRequest build(@Nonnull final List<String> parseErrors) {
            return new GetCallHistoryRequest(this, parseErrors);
        }

        @Nonnull
        public Builder setJID(@Nonnull final JID jid) {
            this.jid = jid;
            return this;
        }

        @Nonnull
        public Builder setCaller(@Nonnull final String caller) {
            this.caller = caller;
            return this;
        }

        @Nonnull
        public Builder setCalled(@Nonnull final String called) {
            this.called = called;
            return this;
        }

        @Nonnull
        public Builder setCallType(@Nonnull final CallType callType) {
            this.callType = callType;
            return this;
        }

        @Nonnull
        public Builder setFromDate(@Nonnull final LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        @Nonnull
        public Builder setUpToDate(@Nonnull final LocalDate upToDate) {
            this.upToDate = upToDate;
            return this;
        }

        @Nonnull
        public Builder setStart(@Nonnull final Integer start) {
            this.start = start;
            return this;
        }

        @Nonnull
        public Builder setCount(@Nonnull final Integer count) {
            this.count = count;
            return this;
        }
    }

}
