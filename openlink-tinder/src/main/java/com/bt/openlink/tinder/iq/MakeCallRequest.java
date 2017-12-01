package com.bt.openlink.tinder.iq;

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
import com.bt.openlink.iq.MakeCallRequestBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PhoneNumber;

public class MakeCallRequest extends OpenlinkIQ {
    @Nullable private final JID jid;
    @Nullable private final InterestId interestId;
    @Nullable private final PhoneNumber destination;
    @Nonnull private final List<FeatureId> featureIds;

    private MakeCallRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        this.interestId = builder.getInterestId().orElse(null);
        this.destination = builder.getDestination().orElse(null);
        this.featureIds = Collections.unmodifiableList(builder.getFeatureIds());
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_MAKE_CALL);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "jid", jid);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "interest", interestId);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "destination", destination);
        if (!featureIds.isEmpty()) {
            final Element featuresElement = inElement.addElement("features");
            for (final FeatureId featureId : featureIds) {
                featuresElement.addElement("feature").addElement("id").setText(featureId.value());
            }
        }
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
    public List<FeatureId> getFeatureIds() {
        return featureIds;
    }

    @Nonnull
    public static MakeCallRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getChildElementString(inElement, "jid"));
        jid.ifPresent(builder::setJID);
        final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getChildElementString(inElement, "interest"));
        interestId.ifPresent(builder::setInterestId);
        final Optional<PhoneNumber> destination = PhoneNumber.from(TinderPacketUtil.getChildElementString(inElement, "destination"));
        destination.ifPresent(builder::setDestination);
        getFeatures(builder, inElement);
        final MakeCallRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    @SuppressWarnings("unchecked")
    private static void getFeatures(Builder builder, Element inElement) {
        final Element featuresElement = TinderPacketUtil.getChildElement(inElement, "features");
        if (featuresElement != null) {
            final List<Element> featureElements = featuresElement.elements("feature");
            for (final Element featureElement : featureElements) {
                FeatureId.from(TinderPacketUtil.getChildElementString(featureElement, "id")).ifPresent(builder::addFeatureId);
            }
        }
    }

    public static final class Builder extends MakeCallRequestBuilder<Builder, JID, Type> {

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
