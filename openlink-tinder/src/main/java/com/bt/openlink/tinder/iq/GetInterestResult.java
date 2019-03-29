package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetInterestResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;
import com.bt.openlink.type.PhoneNumber;

public class GetInterestResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-interest result";

    @Nullable private final Interest interest;

    private GetInterestResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.interest = builder.getInterest().orElse(null);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_INTEREST);
        final Element interestsElement = outElement.addElement("interests", OpenlinkXmppNamespace.OPENLINK_INTERESTS.uri());
        final Element interestElement = interestsElement.addElement("interest");
        if (interest != null) {
            interest.getId().ifPresent(interestId -> interestElement.addAttribute("id", interestId.value()));
            interest.getType().ifPresent(interestType -> interestElement.addAttribute("type", interestType.value()));
            interest.getLabel().ifPresent(label -> interestElement.addAttribute("label", label));
            interest.isDefaultInterest().ifPresent(isDefault -> interestElement.addAttribute("default", String.valueOf(isDefault)));
            interest.getMaxCalls().ifPresent(maxCalls->interestElement.addAttribute("maxCalls", String.valueOf(maxCalls)));
            interest.getNumber().ifPresent(number->interestElement.addAttribute("number", number.value()));
            interest.getCallForward().ifPresent(callForward->interestElement.addAttribute("fwd", callForward.value()));
            interest.getCallStatus().ifPresent(callStatus->TinderPacketUtil.addCallStatus(interestElement, callStatus));
        }
    }

    @Nonnull
    public Optional<Interest> getInterest() {
        return Optional.ofNullable(interest);
    }

    @Nonnull
    public static GetInterestResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element interestElement = TinderPacketUtil.getChildElement(outElement, "interests", "interest");
        if (interestElement != null) {
            final Interest.Builder interestBuilder = Interest.Builder.start();
            InterestId.from(TinderPacketUtil.getNullableStringAttribute(interestElement, "id")).ifPresent(interestBuilder::setId);
            InterestType.from(TinderPacketUtil.getNullableStringAttribute(interestElement, "type")).ifPresent(interestBuilder::setType);
            TinderPacketUtil.getStringAttribute(interestElement, "label").ifPresent(interestBuilder::setLabel);
            TinderPacketUtil.getBooleanAttribute(interestElement, "default", DESCRIPTION, parseErrors).ifPresent(interestBuilder::setDefault);
            TinderPacketUtil.getIntegerAttribute(interestElement, "maxCalls", DESCRIPTION, parseErrors).ifPresent(interestBuilder::setMaxCalls);
            TinderPacketUtil.getStringAttribute(interestElement, "number").flatMap(PhoneNumber::from).ifPresent(interestBuilder::setNumber);
            TinderPacketUtil.getStringAttribute(interestElement, "fwd").flatMap(PhoneNumber::from).ifPresent(interestBuilder::setCallForward);
            TinderPacketUtil.getCallStatus(interestElement, DESCRIPTION, parseErrors).ifPresent(interestBuilder::setCallStatus);
            builder.setInterest(interestBuilder.build(parseErrors));
        }
        final GetInterestResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends GetInterestResultBuilder<Builder, JID, Type> {

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
         * Convenience method to create a new {@link Builder} based on a {@link Type#get IQ.Type.get} or {@link Type#set
         * IQ.Type.set} IQ. The new builder will be initialized with:
         * <ul>
         *
         * <li>The sender set to the recipient of the originating IQ.
         * <li>The recipient set to the sender of the originating IQ.
         * <li>The id set to the id of the originating IQ.
         * </ul>
         *
         * @param request
         *            the {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set} IQ packet.
         * @throws IllegalArgumentException
         *             if the IQ packet does not have a type of {@link Type#get IQ.Type.get} or {@link Type#set IQ.Type.set}.
         * @return a new {@link Builder} based on the originating IQ.
         */
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return start(IQ.createResultIQ(request));
        }

        protected Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetInterestResult build() {
            validate();
            return new GetInterestResult(this, null);
        }

        @Nonnull
        private GetInterestResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetInterestResult(this, parseErrors);
        }
    }

}
