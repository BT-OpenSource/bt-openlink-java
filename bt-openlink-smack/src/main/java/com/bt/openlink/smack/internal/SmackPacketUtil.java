package com.bt.openlink.smack.internal;

import com.bt.openlink.smack.iq.GetProfilesRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.packet.IQ;
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

    @Nonnull
    public static Optional<IQ.Type> getSmackType(String typeString) {
        return typeString == null || typeString.isEmpty() ? Optional.empty() : Optional.of(IQ.Type.valueOf(typeString));
    }

    public static void setBuilderFields(GetProfilesRequest.Builder builder, IQ iq) {
        final Jid iqTo = iq.getTo();
        final Jid iqFrom = iq.getFrom();
        final IQ.Type iqType = iq.getType();
        //builder.setTo(iqTo == null ? null : iqTo.toString())
          //      .setFrom(iqFrom == null ? null : iqFrom.toString());
                //.setId(StanzaId.from(iq.getStanzaId()).orElse(null))
                //.setType(iqType == null ? null : iqType.name());
    }

}
