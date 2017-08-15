package com.bt.openlink.tinder.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import org.dom4j.Element;
import org.xmpp.packet.IQ;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class OpenlinkIQParser {

    private OpenlinkIQParser() {
    }

    private interface StanzaFactory {
        IQ from(final IQ iq);
    }

    private static class IQMatcher {

        private final OpenlinkXmppNamespace namespace;
        private final IQ.Type type;
        private final StanzaFactory stanzaFactory;

        IQMatcher(final OpenlinkXmppNamespace namespace, final IQ.Type type, final StanzaFactory stanzaFactory) {
            this.namespace = namespace;
            this.type = type;
            this.stanzaFactory = stanzaFactory;
        }

        boolean matches(@Nullable final String namespace, @Nullable final IQ.Type type) {
            return this.type == type && this.namespace.uri().equals(namespace);
        }
    }

    private static final List<IQMatcher> STANZA_TYPE_MATCHER_LIST = Arrays.asList(
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_PROFILES, IQ.Type.set, GetProfilesRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_PROFILES, IQ.Type.result, GetProfilesResult::from)
    );

    @Nonnull
    public static IQ parse(@Nonnull final IQ iq) {
        final Element commandElement = iq.getChildElement();
        final String node = TinderPacketUtil.getAttributeString(commandElement, "node");
        final IQ.Type type = iq.getType();
        for (final IQMatcher iqMatcher : STANZA_TYPE_MATCHER_LIST) {
            if (iqMatcher.matches(node, type)) {
                return iqMatcher.stanzaFactory.from(iq);
            }
        }
        return iq;
    }

}
