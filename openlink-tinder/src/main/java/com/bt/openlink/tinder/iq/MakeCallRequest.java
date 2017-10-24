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
import com.bt.openlink.iq.MakeCallRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PhoneNumber;

public class MakeCallRequest extends OpenlinkIQ2 {
    private static final String STANZA_DESCRIPTION = "make-call request";

    @Nullable private final JID jid;
    @Nullable private final InterestId interestId;
    @Nullable private final PhoneNumber destination;

    private MakeCallRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        this.interestId = builder.getInterestId().orElse(null);
        this.destination = builder.getDestination().orElse(null);
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_MAKE_CALL);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "jid", jid);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "interest", interestId);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "destination", destination);
    }

    @Nonnull
    public Optional<JID> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<PhoneNumber> getDestination() {
        return Optional.ofNullable(destination);
    }

    @Nonnull
    public static MakeCallRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getChildElementString(inElement,
                "jid",
                false,
                STANZA_DESCRIPTION,
                parseErrors));
        jid.ifPresent(builder::setJID);
        final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getChildElementString(inElement,
                "interest",
                false,
                STANZA_DESCRIPTION,
                parseErrors));
        interestId.ifPresent(builder::setInterestId);
        final Optional<PhoneNumber> destination = PhoneNumber.from(TinderPacketUtil.getChildElementString(inElement,
                "destination",
                false,
                STANZA_DESCRIPTION,
                parseErrors));
        destination.ifPresent(builder::setDestination);
        final MakeCallRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends MakeCallRequestBuilder<Builder, JID ,Type> {

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
            super(Type.class);
        }

        @Nonnull
        public MakeCallRequest build() {
            super.validate();
            return new MakeCallRequest(this, null);
        }

        @Nonnull
        private MakeCallRequest build(@Nonnull final List<String> errors) {
            super.validate(errors);
            return new MakeCallRequest(this, errors);
        }

    }

}
