package com.bt.openlink.smack.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GetProfilesRequest extends OpenlinkIQ {
    @Nullable private final String jid;

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN, "jid");
        final String jid;
        if ("jid".equals(parser.getName())) {
            jid = parser.nextText();
        } else {
            jid = null;
        }
        final List<String> parseErrors = new ArrayList<>();
        if (jid == null) {
            parseErrors.add("Invalid get-profiles request; missing 'jid' field is mandatory");
        }
        return Builder.start().setJid(jid == null || jid.isEmpty() ? null : jid).build(parseErrors);
    }

    private GetProfilesRequest(@Nonnull Builder builder, @Nonnull List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.jid = builder.jid;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_PROFILES.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();
        xml.optElement("jid", jid);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @Nonnull
    public Optional<Jid> getJid() {
        return SmackPacketUtil.getSmackJid(jid);
    }

    public static final class Builder extends IQBuilder<Builder> {

        @Nullable String jid;

        private Builder() {
        }

        @Nonnull
        @Override
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

        public Builder setJid(@Nullable String jid) {
            this.jid = jid;
            return this;
        }

        @Nonnull
        public Builder setJid(@Nullable final Jid jid) {
            return setJid(jid == null ? null : jid.toString());
        }

    }
}
