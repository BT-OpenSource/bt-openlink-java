package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetProfilesRequestBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;

public class GetProfilesRequest extends OpenlinkIQ {
    @Nullable private final Jid jid;

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN, "jid");
        final String jidString;
        if ("jid".equals(parser.getName())) {
            jidString = parser.nextText();
        } else {
            jidString = null;
        }
        final Optional<Jid> jidOptional = SmackPacketUtil.getSmackJid(jidString);
        final Builder builder = Builder.start();
        jidOptional.ifPresent(builder::setJID);
        return builder.build(new ArrayList<>());
    }

    private GetProfilesRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
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
    public Optional<Jid> getJID() {
        return Optional.ofNullable(jid);
    }

    public static final class Builder extends GetProfilesRequestBuilder<Builder, Jid, IQ.Type> {

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public GetProfilesRequest build() {
            super.validate();
            return new GetProfilesRequest(this, null);
        }

        @Nonnull
        private GetProfilesRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new GetProfilesRequest(this, errors);
        }

    }
}
