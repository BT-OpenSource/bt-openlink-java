package com.bt.openlink.tinder.iq;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetCallHistoryResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.HistoricalCall;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.UserId;

public class GetCallHistoryResult extends OpenlinkIQ {
    private static final String STANZA_DESCRIPTION = "get-call-history result";

    @Nullable private final Long totalRecordCount;
    @Nullable private final Long firstRecordNumber;
    @Nullable private final Long recordCountInBatch;
    @Nonnull private List<HistoricalCall<JID>> calls;

    private GetCallHistoryResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.totalRecordCount = builder.getTotalRecordCount().orElse(null);
        this.firstRecordNumber = builder.getFirstRecordNumber().orElse(null);
        this.recordCountInBatch = builder.getRecordCountInBatch().orElse(null);
        this.calls = new ArrayList<>(builder.getCalls());
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY);
        final Element callHistoryElement = outElement.addElement("callhistory", OpenlinkXmppNamespace.OPENLINK_CALL_HISTORY.uri());
        getTotalRecordCount().ifPresent(total -> callHistoryElement.addAttribute("total", String.valueOf(total)));
        getFirstRecordNumber().ifPresent(start -> callHistoryElement.addAttribute("start", String.valueOf(start)));
        getRecordCountInBatch().ifPresent(count -> callHistoryElement.addAttribute("count", String.valueOf(count)));
        this.calls.forEach(call -> {
            final Element callElement = callHistoryElement.addElement("call");
            call.getId().ifPresent(id -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "id", id));
            call.getUserId().ifPresent(profileId -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "profile", profileId));
            call.getInterestId().ifPresent(interest -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "interest", interest));
            call.getState().ifPresent(state -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "state", state.getLabel()));
            call.getDirection().ifPresent(direction -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "direction", direction.getLabel()));
            call.getCallerNumber().ifPresent(caller -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "caller", caller));
            call.getCallerName().ifPresent(callerName -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "callername", callerName));
            call.getCalledNumber().ifPresent(called -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "called", called));
            call.getCalledName().ifPresent(calledName -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "calledname", calledName));
            call.getStartTime().map(Timestamp::from).ifPresent(timestamp -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "timestamp", timestamp));
            call.getStartTime().map(startTime -> TinderPacketUtil.ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC)))
                    .ifPresent(startTime -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "starttime", startTime));
            call.getDuration().ifPresent(duration -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "duration", duration.toMillis()));
            call.getTsc().ifPresent(tsc -> TinderPacketUtil.addElementWithTextIfNotNull(callElement, "tsc", tsc));
        });
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetCallHistoryResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final GetCallHistoryResult.Builder builder = GetCallHistoryResult.Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element callHistoryElement = TinderPacketUtil.getChildElement(outElement, "callhistory");
        TinderPacketUtil.getIntegerAttribute(callHistoryElement, "total", STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setTotalRecordCount);
        TinderPacketUtil.getIntegerAttribute(callHistoryElement, "start", STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setFirstRecordNumber);
        TinderPacketUtil.getIntegerAttribute(callHistoryElement, "count", STANZA_DESCRIPTION, parseErrors).ifPresent(builder::setRecordCountInBatch);
        if (callHistoryElement != null) {
            final List<Element> calls = callHistoryElement.elements("call");
            calls.forEach(callElement -> {
                final HistoricalCall.Builder<JID> historicalCallBuilder = HistoricalCall.Builder.start();
                TinderPacketUtil.getOptionalChildElementString(callElement, "id").flatMap(CallId::from).ifPresent(historicalCallBuilder::setId);
                TinderPacketUtil.getOptionalChildElementString(callElement, "profile").flatMap(UserId::from).ifPresent(historicalCallBuilder::setUserId);
                TinderPacketUtil.getOptionalChildElementString(callElement, "interest").flatMap(InterestId::from).ifPresent(historicalCallBuilder::setInterestId);
                TinderPacketUtil.getOptionalChildElementString(callElement, "state").flatMap(CallState::from).ifPresent(historicalCallBuilder::setState);
                TinderPacketUtil.getOptionalChildElementString(callElement, "direction").flatMap(CallDirection::from).ifPresent(historicalCallBuilder::setDirection);
                TinderPacketUtil.getOptionalChildElementString(callElement, "caller").flatMap(PhoneNumber::from).ifPresent(historicalCallBuilder::setCallerNumber);
                TinderPacketUtil.getOptionalChildElementString(callElement, "callername").ifPresent(historicalCallBuilder::setCallerName);
                TinderPacketUtil.getOptionalChildElementString(callElement, "called").flatMap(PhoneNumber::from).ifPresent(historicalCallBuilder::setCalledNumber);
                TinderPacketUtil.getOptionalChildElementString(callElement, "calledname").ifPresent(historicalCallBuilder::setCalledName);
                TinderPacketUtil.getChildElementLong(callElement, "duration", STANZA_DESCRIPTION, parseErrors).map(Duration::ofMillis).ifPresent(historicalCallBuilder::setDuration);
                if (historicalCallBuilder.isStartTimeNull()) {
                    final Optional<String> optionalTimestamp = TinderPacketUtil.getOptionalChildElementString(callElement, "timestamp");
                    try {
                        optionalTimestamp
                                .map(Timestamp::valueOf)
                                .map(Timestamp::toInstant)
                                .ifPresent(historicalCallBuilder::setStartTime);
                    } catch (final IllegalArgumentException ignored) {
                        parseErrors.add(String.format("Invalid %s; invalid timestamp '%s'; please supply a valid timestamp", STANZA_DESCRIPTION, optionalTimestamp.get()));
                    }
                }
                final Optional<String> optionalStartTime = TinderPacketUtil.getOptionalChildElementString(callElement, "starttime");
                try {
                    optionalStartTime.map(Instant::parse).ifPresent(historicalCallBuilder::setStartTime);
                } catch (final DateTimeParseException ignored) {
                    parseErrors.add(String.format("Invalid %s; invalid starttime '%s'; please supply a valid starttime", STANZA_DESCRIPTION, optionalStartTime.get()));
                }
                TinderPacketUtil.getJID(TinderPacketUtil.getNullableChildElementString(callElement, "tsc")).ifPresent(historicalCallBuilder::setTsc);
                builder.addCall(historicalCallBuilder.build(parseErrors));
            });
        }

        return builder.build(parseErrors);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getTotalRecordCount() {
        return Optional.ofNullable(totalRecordCount);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getFirstRecordNumber() {
        return Optional.ofNullable(firstRecordNumber);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public Optional<Long> getRecordCountInBatch() {
        return Optional.ofNullable(recordCountInBatch);
    }

    @SuppressWarnings("WeakerAccess")
    @Nonnull
    public List<HistoricalCall<JID>> getCalls() {
        return calls;
    }

    public static final class Builder extends GetCallHistoryResultBuilder<Builder, JID, Type> {

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

        /**
         * Convenience method to create a new {@link Builder} based
         * on a {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ. The new
         * packet will be initialized with:<ul>
         *
         *      <li>The sender set to the recipient of the originating IQ.
         *      <li>The recipient set to the sender of the originating IQ.
         *      <li>The type set to {@link Type#result IQ.Type.result}.
         *      <li>The id set to the id of the originating IQ.
         * </ul>
         *
         * @param request the {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ packet.
         * @throws IllegalArgumentException if the IQ packet does not have a type of
         *      {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set}.
         * @return a new {@link Builder} based on the originating IQ.
         */
        @SuppressWarnings("WeakerAccess")
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return start(IQ.createResultIQ(request));
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public GetCallHistoryResult build() {
            super.validate();
            return new GetCallHistoryResult(this, null);
        }

        @Nonnull
        private GetCallHistoryResult build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new GetCallHistoryResult(this, errors);
        }
    }

}
