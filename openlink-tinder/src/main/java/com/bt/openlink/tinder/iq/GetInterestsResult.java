package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;

public class GetInterestsResult extends OpenlinkIQ {
    private static final String DESCRIPTION = "get-interests result";

    @Nonnull private final List<Interest> interests;

    private GetInterestsResult(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.interests = Collections.unmodifiableList(builder.interests);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_INTERESTS);
        final Element interestsElement = outElement.addElement("interests", OpenlinkXmppNamespace.OPENLINK_INTERESTS.uri());
        for (final Interest interest : interests) {
            final Element interestElement = interestsElement.addElement("interest");
            interest.getId().ifPresent(interestId -> interestElement.addAttribute("id", interestId.value()));
            interest.getType().ifPresent(interestType -> interestElement.addAttribute("type", interestType.value()));
            interest.getLabel().ifPresent(label -> interestElement.addAttribute("label", label));
            interest.isDefault().ifPresent(isDefault -> interestElement.addAttribute("default", String.valueOf(isDefault)));
        }
    }

    @Nonnull
    public List<Interest> getInterests() {
        return interests;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetInterestsResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start()
                .setTo(iq.getTo())
                .setFrom(iq.getFrom())
                .setID(iq.getID())
                .setType(iq.getType());
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element interestsElement = TinderPacketUtil.getChildElement(outElement, "interests");
        if (interestsElement == null) {
            parseErrors.add("Invalid get-interests result; missing 'interests' element is mandatory");
        } else {
            final List<Element> interestElements = interestsElement.elements("interest");
            for (final Element interestElement : interestElements) {
                final Interest.Builder interestBuilder = Interest.Builder.start();
                final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getStringAttribute(interestElement, "id", true, DESCRIPTION, parseErrors));
                interestId.ifPresent(interestBuilder::setInterestId);
                final Optional<InterestType> interestType = InterestType.from(TinderPacketUtil.getStringAttribute(interestElement, "type", true, DESCRIPTION, parseErrors));
                interestType.ifPresent(interestBuilder::setInterestType);
                final Optional<String> label = Optional.ofNullable(TinderPacketUtil.getStringAttribute(interestElement, "label", true, DESCRIPTION, parseErrors));
                label.ifPresent(interestBuilder::setLabel);
                final Optional<Boolean> isDefault = Optional.ofNullable(TinderPacketUtil.getBooleanAttribute(interestElement, "default", true, DESCRIPTION, parseErrors));
                isDefault.ifPresent(interestBuilder::setDefault);
                builder.addInterest(interestBuilder.build());
            }
        }

        final GetInterestsResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nonnull private final List<Interest> interests = new ArrayList<>();

        private Builder() {
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.result;
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public static GetInterestsResult.Builder start(@Nonnull final GetInterestsRequest request) {
            return new GetInterestsResult.Builder()
                    .setID(request.getID())
                    .setFrom(request.getTo())
                    .setTo(request.getFrom());
        }

        @Nonnull
        public GetInterestsResult build() {
            validateBuilder();
            return new GetInterestsResult(this, Collections.emptyList());
        }

        @Nonnull
        private GetInterestsResult build(final List<String> parseErrors) {
            return new GetInterestsResult(this, parseErrors);
        }

        public Builder addInterest(@Nonnull Interest interest) {
            if (interest.getId().isPresent()) {
                this.interests.forEach(existingInterest -> {
                    if (existingInterest.getId().equals(interest.getId())) {
                        throw new IllegalArgumentException("The interest id must be unique");
                    }
                });
            }
            this.interests.add(interest);
            return this;
        }

    }

}
