package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.IQBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;

public class SetFeaturesResult extends OpenlinkIQ {

    private SetFeaturesResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_SET_FEATURES.uri())
                .rightAngleBracket();
        return xml;
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT);

        final SetFeaturesResult.Builder builder = SetFeaturesResult.Builder.start();
        final List<String> parseErrors = new ArrayList<>();
        return builder.build(parseErrors);
    }

    public static final class Builder extends IQBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
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
            return SmackPacketUtil.createResultBuilder(start(), request);
        }

        protected Builder() {
            super(Type.class);
        }

        @Override
        public void validate(final List<String> errors) {
            validate(errors, true);
        }

        protected void validate(List<String> errors, boolean checkIQFields) {
            if (checkIQFields) {
                super.validate(errors);
            }
        }

        @Nonnull
        public SetFeaturesResult build() {
            super.validate();
            return new SetFeaturesResult(this, null);
        }

        @Nonnull
        private SetFeaturesResult build(@Nonnull final List<String> errors) {
            validate(errors, false);
            return new SetFeaturesResult(this, errors);
        }

        @Nonnull
        @Override
        public String getExpectedIQType() {
            return "result";
        }
    }
}
