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
    public static GetCallHistoryRequest from(@Nonnull IQ iq) {
        final Builder builder = Builder.start(iq);
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        TinderPacketUtil.getJID(TinderPacketUtil.getChildElementString(inElement, "jid")).ifPresent(builder::setJID);
        TinderPacketUtil.getOptionalChildElementString(inElement, "caller").ifPresent(builder::setCaller);
        TinderPacketUtil.getOptionalChildElementString(inElement, "called").ifPresent(builder::setCalled);
        final Optional<String> callTypeString = TinderPacketUtil.getOptionalChildElementString(inElement, "calltype");
        callTypeString.ifPresent(string -> {
            final Optional<CallType> callType = CallType.from(string);
            if (callType.isPresent()) {
                builder.setCallType(callType.get());
            } else {
                parseErrors.add(String.format("Invalid %s; invalid calltype - '%s' should be 'in', 'out' or 'missed'", STANZA_DESCRIPTION, string));
            }
        });
        TinderPacketUtil.getChildElementLocalDate(inElement,"fromdate", DATE_FORMATTER, STANZA_DESCRIPTION, DATE_PATTERN, parseErrors)
                .ifPresent(builder::setFromDate);
        TinderPacketUtil.getChildElementLocalDate(inElement,"uptodate", DATE_FORMATTER, STANZA_DESCRIPTION, DATE_PATTERN, parseErrors)
                .ifPresent(builder::setUpToDate);
        TinderPacketUtil.getChildElementLong(inElement,"start", STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setStart);
        TinderPacketUtil.getChildElementLong(inElement,"count", STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setCount);
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
            super(IQ.Type.class);
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
