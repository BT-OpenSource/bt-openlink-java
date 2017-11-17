package com.bt.openlink.smack;

import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public final class Fixtures {

    private Fixtures() {
    }

    public static final Jid USER_JID;
    public static final Jid TO_JID;
    public static final Jid FROM_JID;
    static {
        try {
            USER_JID = JidCreate.fullFrom(CoreFixtures.USER_JID_STRING);
            TO_JID = JidCreate.fullFrom(CoreFixtures.TO_JID_STRING);
            FROM_JID = JidCreate.fullFrom(CoreFixtures.FROM_JID_STRING);
        } catch (final XmppStringprepException e) {
            throw new IllegalArgumentException("Invalid test jid", e);
        }
    }

}
