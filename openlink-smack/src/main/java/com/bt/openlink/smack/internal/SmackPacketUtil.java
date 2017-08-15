package com.bt.openlink.smack.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public final class SmackPacketUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private SmackPacketUtil() {
    }

    @Nonnull
    public static Optional<Jid> getSmackJid(@Nullable String jidString) {
        try {
            return jidString == null || jidString.isEmpty() ? Optional.empty() : Optional.of(JidCreate.from(jidString));
        } catch (final XmppStringprepException e) {
            LOGGER.warn("Unexpected exception attempting to create JID [jidString={}]", jidString, e);
            return Optional.empty();
        }
    }

}
