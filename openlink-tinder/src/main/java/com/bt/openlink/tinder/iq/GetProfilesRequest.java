package com.bt.openlink.tinder.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GetProfilesRequest extends OpenlinkIQ {
    @Nullable private final String jid;

    private GetProfilesRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super(builder, parseErrors);
        this.jid = builder.jid;
        final Element inElement = TinderPacketUtil.addCommandIOInputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_PROFILES);
        TinderPacketUtil.addElementWithTextIfNotNull(inElement, "jid", getJID().orElse(null));

    }

    @Nonnull
    public Optional<JID> getJID() {
        return TinderPacketUtil.getTinderJID(jid);
    }

    @Nonnull
    public static GetProfilesRequest from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element inElement = TinderPacketUtil.getIOInElement(iq);
        final Builder builder = Builder.start()
                .setTo(iq.getTo())
                .setFrom(iq.getFrom())
                .setID(iq.getID())
                .setType(iq.getType())
                .setJID(TinderPacketUtil.getChildElementString(inElement,
                        "jid",
                        true,
                        "get-profiles request",
                        parseErrors));

        final GetProfilesRequest request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nullable String jid;

        private Builder() {
        }

        @Override
        @Nonnull
        protected Type getExpectedType() {
            return Type.set;
        }

        @Override
        void validateBuilder() {
            super.validateBuilder();
            if (jid == null) {
                throw new IllegalStateException("The stanza 'jid' has not been set");
            }
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public GetProfilesRequest build() {
            validateBuilder();
            return new GetProfilesRequest(this, Collections.emptyList());
        }

        @Nonnull
        private GetProfilesRequest build(final List<String> parseErrors) {
            return new GetProfilesRequest(this, parseErrors);
        }

        public Builder setJID(@Nullable String jid) {
            this.jid = jid;
            return this;
        }

        @Nonnull
        public Builder setJID(@Nullable final JID jid) {
            return setJID(jid == null ? null : jid.toString());
        }

    }

}
