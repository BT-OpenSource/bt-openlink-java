package com.bt.openlink.smack.internal;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.xmlpull.v1.XmlPullParser;

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

}
