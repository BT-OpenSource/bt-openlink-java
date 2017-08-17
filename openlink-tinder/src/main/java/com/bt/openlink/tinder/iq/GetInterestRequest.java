package com.bt.openlink.tinder.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.InterestId;
import org.dom4j.Element;
import org.xmpp.packet.IQ;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GetInterestRequest extends OpenlinkIQ {
    @Nullable private final InterestId interestId;

    private GetInterestRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.interestId = builder.interestId;
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_INTEREST);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "interest", interestId);

    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public static GetInterestRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start()
                .setTo(iq.getTo())
                .setFrom(iq.getFrom())
                .setID(iq.getID())
                .setType(iq.getType());

        final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getChildElementString(inElement,
                "interest",
                true,
                "get-interest request",
                parseErrors));
        interestId.ifPresent(builder::setInterestId);

        final GetInterestRequest request = builder.build(parseErrors);

        request.setID(iq.getID());

        return request;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nullable InterestId interestId;

        private Builder() {
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.set;
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public GetInterestRequest build() {
            super.validateBuilder();
            if (interestId == null) {
                throw new IllegalStateException("The stanza 'interestId' has not been set");
            }
            return new GetInterestRequest(this, Collections.emptyList());
        }

        @Nonnull
        private GetInterestRequest build(@Nonnull final List<String> parseErrors) {
            return new GetInterestRequest(this, parseErrors);
        }

        @Nonnull
        public Builder setInterestId(@Nonnull final InterestId interestId) {
            this.interestId = interestId;
            return this;
        }

    }

}
