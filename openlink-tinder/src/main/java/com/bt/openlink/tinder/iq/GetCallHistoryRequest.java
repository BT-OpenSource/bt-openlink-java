package com.bt.openlink.tinder.iq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetCallHistoryRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.CallType;

public class GetCallHistoryRequest extends OpenlinkIQ2 {
    private static final String STANZA_DESCRIPTION = "get-call-history request";
    private static final String DATE_PATTERN = "MM/dd/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Nullable private final JID jid;
    @Nullable private final String caller;
    @Nullable private final String called;
    @Nullable private final CallType callType;
    @Nullable private final LocalDate fromDate;
    @Nullable private final LocalDate upToDate;
    @Nullable private final Long start;
    @Nullable private final Long count;

    private GetCallHistoryRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        this.caller = builder.getCaller().orElse(null);
        this.called = builder.getCalled().orElse(null);
        this.callType = builder.getCallType().orElse(null);
        this.fromDate = builder.getFromDate().orElse(null);
        this.upToDate = builder.getUpToDate().orElse(null);
        this.start = builder.getStart().orElse(null);
        this.count = builder.getCount().orElse(null);
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY);
        getJID().ifPresent(jid -> TinderPacketUtil.addElementWithText(inElement, "jid", jid));
        getCaller().ifPresent(caller -> TinderPacketUtil.addElementWithText(inElement, "caller", caller));
        getCalled().ifPresent(caller -> TinderPacketUtil.addElementWithText(inElement, "called", caller));
        getCallType().ifPresent(callType -> TinderPacketUtil.addElementWithText(inElement, "calltype", callType.getLabel()));
        getFromDate().ifPresent(fromDate -> TinderPacketUtil.addElementWithText(inElement, "fromdate", DATE_FORMATTER.format(fromDate)));
        getUpToDate().ifPresent(fromDate -> TinderPacketUtil.addElementWithText(inElement, "uptodate", DATE_FORMATTER.format(fromDate)));
        getStart().ifPresent(start -> TinderPacketUtil.addElementWithText(inElement, "start", start));
        getCount().ifPresent(count -> TinderPacketUtil.addElementWithText(inElement, "count", count));
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
        if (!callType.isPresent() && callTypeString != null && !callTypeString.isEmpty()) {
            parseErrors.add(String.format("Invalid %s; invalid calltype - '%s' should be 'in', 'out' or 'missed'", STANZA_DESCRIPTION, callTypeString));
        }
        final Optional<LocalDate> fromDate = Optional.ofNullable(TinderPacketUtil.getChildElementLocalDate(inElement,
                "fromdate", DATE_FORMATTER, false, STANZA_DESCRIPTION, DATE_PATTERN, parseErrors));
        final Optional<LocalDate> upToDate = Optional.ofNullable(TinderPacketUtil.getChildElementLocalDate(inElement,
                "uptodate", DATE_FORMATTER, false, STANZA_DESCRIPTION, DATE_PATTERN, parseErrors));
        final Optional<Long> start = Optional.ofNullable(TinderPacketUtil.getChildElementLong(inElement,
                "start", false, STANZA_DESCRIPTION, parseErrors));
        final Optional<Long> count = Optional.ofNullable(TinderPacketUtil.getChildElementLong(inElement,
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
    public Optional<Long> getStart() {
        return Optional.ofNullable(start);
    }

    @Nonnull
    public Optional<Long> getCount() {
        return Optional.ofNullable(count);
    }

    public static final class Builder extends GetCallHistoryRequestBuilder<Builder, JID, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            final Builder builder = start();
            TinderIQBuilder.setIQBuilder(builder, iq);
            return builder;
        }

        private Builder() {
        }

        @Nonnull
        @Override
        public Type getExpectedIQType() {
            return Type.set;
        }

        @Nonnull
        public GetCallHistoryRequest build() {
            super.validate();
            return new GetCallHistoryRequest(this, null);
        }

        @Nonnull
        private GetCallHistoryRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new GetCallHistoryRequest(this, errors);
        }

    }

}
