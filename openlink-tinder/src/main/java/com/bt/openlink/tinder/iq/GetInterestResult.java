package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;

public class GetInterestResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-interests result";

    @Nullable private final Interest interest;

    private GetInterestResult(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.interest = builder.interest;
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_INTEREST);
        final Element interestsElement = outElement.addElement("interests", OpenlinkXmppNamespace.OPENLINK_INTERESTS.uri());
        final Element interestElement = interestsElement.addElement("interest");
        if (interest != null) {
            interest.getId().ifPresent(interestId -> interestElement.addAttribute("id", interestId.value()));
            interest.getType().ifPresent(interestType -> interestElement.addAttribute("type", interestType.value()));
            interest.getLabel().ifPresent(label -> interestElement.addAttribute("label", label));
            interest.isDefaultInterest().ifPresent(isDefault -> interestElement.addAttribute("default", String.valueOf(isDefault)));
        }
    }

    @Nonnull
    public Optional<Interest> getInterest() {
        return Optional.ofNullable(interest);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetInterestResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element interestElement = TinderPacketUtil.getChildElement(outElement, "interests", "interest");
        if (interestElement == null) {
            parseErrors.add("Invalid get-interest result; missing 'interest' element is mandatory");
        } else {
            final Interest.Builder interestBuilder = Interest.Builder.start();
            final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getStringAttribute(interestElement, "id", true, DESCRIPTION, parseErrors).orElse(null));
            interestId.ifPresent(interestBuilder::setId);
            final Optional<InterestType> interestType = InterestType.from(TinderPacketUtil.getStringAttribute(interestElement, "type", true, DESCRIPTION, parseErrors).orElse(null));
            interestType.ifPresent(interestBuilder::setType);
            final Optional<String> label = Optional.ofNullable(TinderPacketUtil.getStringAttribute(interestElement, "label", true, DESCRIPTION, parseErrors).orElse(null));
            label.ifPresent(interestBuilder::setLabel);
            final Optional<Boolean> isDefault = TinderPacketUtil.getBooleanAttribute(interestElement, "default", true, DESCRIPTION, parseErrors);
            isDefault.ifPresent(interestBuilder::setDefault);
            builder.setInterest(interestBuilder.build());
        }

        final GetInterestResult request = builder.build(parseErrors);
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

        @Nonnull
        public static Builder start(@Nonnull final GetInterestRequest request) {
            return new Builder(IQ.createResultIQ(request));
        }

        @Nullable private Interest interest;

        private Builder() {
        }

        private Builder(@Nonnull final IQ iq) {
            super(iq);
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.result;
        }

        @Nonnull
        public GetInterestResult build() {
            validateBuilder();
            return build( Collections.emptyList());
        }

        @Nonnull
        private GetInterestResult build(@Nonnull final List<String> parseErrors) {
            return new GetInterestResult(this, parseErrors);
        }

        public Builder setInterest(@Nonnull Interest interest) {
            this.interest = interest;
            return this;
        }

    }

}
