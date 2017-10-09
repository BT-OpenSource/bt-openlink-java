package com.bt.openlink.smack.internal;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.type.Site;

public final class SmackPacketUtil {

    private SmackPacketUtil() {
    }

    @Nonnull
    public static Optional<Jid> getSmackJid(@Nullable String jidString) {
        try {
            return jidString == null || jidString.isEmpty() ? Optional.empty() : Optional.of(JidCreate.from(jidString));
        } catch (final XmppStringprepException ignored) {
            return Optional.empty();
        }
    }

    @Nonnull
    public static Optional<Boolean> getBooleanAttribute(@Nonnull final XmlPullParser parser, @Nonnull final String attributeName) {
        final String attributeValue = parser.getAttributeValue("", attributeName);
        if ("true".equals(attributeValue)) {
            return Optional.of(Boolean.TRUE);
        } else if ("false".equals(attributeValue)) {
            return Optional.of(Boolean.FALSE);
        } else {
            return Optional.empty();
        }
    }

    @Nonnull
    public static Optional<String> getStringAttribute(@Nonnull final XmlPullParser parser, @Nonnull final String attributeName) {
        return Optional.ofNullable(parser.getAttributeValue("", attributeName));
    }

    @Nonnull
    public static Optional<Long> getLongAttribute(@Nonnull final XmlPullParser parser, @Nonnull final String attributeName) {
        final String attributeValue = parser.getAttributeValue("", attributeName);
        if (attributeValue == null || attributeValue.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.valueOf(attributeValue));
        } catch (final NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    public static Optional<Site> getSite(final XmlPullParser parser, final List<String> errors) throws IOException, XmlPullParserException {
        if (parser.getName().equals("site")) {
            final Site.Builder siteBuilder = Site.Builder.start();
            final Optional<Long> siteId = SmackPacketUtil.getLongAttribute(parser, "id");
            siteId.ifPresent(siteBuilder::setId);
            final Optional<Boolean> isDefaultSite = SmackPacketUtil.getBooleanAttribute(parser, OpenlinkXmppNamespace.TAG_DEFAULT);
            isDefaultSite.ifPresent(siteBuilder::setDefault);
            final Optional<Site.Type> siteType = Site.Type.from(SmackPacketUtil.getStringAttribute(parser, "type").orElse(null));
            siteType.ifPresent(siteBuilder::setType);
            parser.next();
            final Optional<String> siteName = Optional.ofNullable(parser.getText());
            siteName.ifPresent(siteBuilder::setName);
            ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
            parser.nextTag();
            return Optional.of(siteBuilder.build(errors));
        } else {
            return Optional.empty();
        }
    }
}
