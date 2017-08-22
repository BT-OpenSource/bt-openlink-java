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
import com.bt.openlink.tinder.internal.TinderPacketUtil;

public class GetProfilesRequest extends OpenlinkIQ {
    @Nullable private final JID jid;

    private GetProfilesRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.jid;
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "jid", jid);
    }

    @Nonnull
    public Optional<JID> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public static GetProfilesRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start(iq);
        final Optional<JID> jid = TinderPacketUtil.getJID(TinderPacketUtil.getChildElementString(inElement,
                "jid",
                true,
                "get-profiles request",
                parseErrors));
        jid.ifPresent(builder::setJID);
        final GetProfilesRequest request = builder.build(parseErrors);
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

        @Nullable JID jid;

        private Builder() {
        }

        public Builder(@Nonnull final IQ iq) {
            super(iq);
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.set;
        }

        @Nonnull
        public GetProfilesRequest build() {
            super.validateBuilder();
            if (jid == null) {
                throw new IllegalStateException("The stanza 'jid' has not been set");
            }
            return new GetProfilesRequest(this, Collections.emptyList());
        }

        @Nonnull
        private GetProfilesRequest build(@Nonnull final List<String> parseErrors) {
            return new GetProfilesRequest(this, parseErrors);
        }

        @Nonnull
        public Builder setJID(@Nonnull final JID jid) {
            this.jid = jid;
            return this;
        }

    }

}
