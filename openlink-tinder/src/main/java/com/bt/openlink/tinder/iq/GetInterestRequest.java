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
import com.bt.openlink.iq.GetInterestRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.InterestId;

public class GetInterestRequest extends OpenlinkIQ2 {
    @Nullable private final InterestId interestId;

    private GetInterestRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.interestId = builder.getInterestId().orElse(null);
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
        final Builder builder = Builder.start(iq);
        final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getChildElementString(inElement,
                "interest",
                false,
                "get-interest request",
                parseErrors));
        interestId.ifPresent(builder::setInterestId);

        final GetInterestRequest request = builder.build(parseErrors);

        request.setID(iq.getID());

        return request;
    }

    public static final class Builder extends GetInterestRequestBuilder<Builder, JID, Type> {

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
        public GetInterestRequest build() {
            validate();
            return new GetInterestRequest(this, null);
        }

        @Nonnull
        private GetInterestRequest build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetInterestRequest(this, parseErrors);
        }
    }
}
