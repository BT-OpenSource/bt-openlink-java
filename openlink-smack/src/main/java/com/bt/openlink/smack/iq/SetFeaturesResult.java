package com.bt.openlink.smack.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetProfilesResultBuilder;
import com.bt.openlink.iq.IQBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        @Override
        public String getExpectedIQType() {
            return "result";
        }
    }
}
