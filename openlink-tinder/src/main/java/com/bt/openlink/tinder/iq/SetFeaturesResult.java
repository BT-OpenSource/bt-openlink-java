package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.IQBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;

public class SetFeaturesResult extends OpenlinkIQ {

    private SetFeaturesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        final Element commandElement = TinderPacketUtil.addCommandElement(this);
        commandElement.addAttribute("status", "completed");
        commandElement.addAttribute("node", OpenlinkXmppNamespace.OPENLINK_SET_FEATURES.uri());
    }

    @Nonnull
    public static SetFeaturesResult from(@Nonnull IQ iq) {
        final SetFeaturesResult request = Builder.start(iq).build(new ArrayList<>());
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends IQBuilder<Builder, JID, Type> {

        @Nonnull
        @Override
        public String getExpectedIQType() {
            return "result";
        }

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
        @SuppressWarnings("WeakerAccess")
        @Nonnull
        public static Builder createResultBuilder(@Nonnull final IQ request) {
            return start(IQ.createResultIQ(request));
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public SetFeaturesResult build() {
            validate();
            return new SetFeaturesResult(this, null);
        }

        @Nonnull
        private SetFeaturesResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new SetFeaturesResult(this, parseErrors);
        }
    }
}
