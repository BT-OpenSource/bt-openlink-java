package com.bt.openlink.tinder.iq;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketError;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.tinder.internal.TinderPacketUtil;

public final class OpenlinkIQParser {

    private OpenlinkIQParser() {
    }

    private interface StanzaFactory {
        IQ from(final IQ iq);
    }

    private static class IQMatcher {

        private final String namespace;
        private final String attribute;
        private final String value;
        private final StanzaFactory stanzaFactory;

        IQMatcher(final OpenlinkXmppNamespace namespace, final String attribute, final String value, final StanzaFactory stanzaFactory) {
            this.namespace = namespace.uri();
            this.attribute = attribute;
            this.value = value;
            this.stanzaFactory = stanzaFactory;
        }

        boolean matches(final Element commandElement) {
            return namespace.equals(TinderPacketUtil.getNullableStringAttribute(commandElement, "node"))
                    && value.equals(TinderPacketUtil.getNullableStringAttribute(commandElement, attribute));
        }
    }

    private static final String ATTRIBUTE_ACTION = "action";
    private static final String ATTRIBUTE_STATUS = "status";
    private static final String ACTION_EXECUTE = "execute";
    private static final String ACTION_COMPLETED = "completed";
    private static final List<IQMatcher> STANZA_TYPE_MATCHER_LIST = Arrays.asList(
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_PROFILES, ATTRIBUTE_ACTION, ACTION_EXECUTE, GetProfilesRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_PROFILES, ATTRIBUTE_STATUS, ACTION_COMPLETED, GetProfilesResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_INTERESTS, ATTRIBUTE_ACTION, ACTION_EXECUTE, GetInterestsRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_INTERESTS, ATTRIBUTE_STATUS, ACTION_COMPLETED, GetInterestsResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_INTEREST, ATTRIBUTE_ACTION, ACTION_EXECUTE, GetInterestRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_INTEREST, ATTRIBUTE_STATUS, ACTION_COMPLETED, GetInterestResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_FEATURES, ATTRIBUTE_ACTION, ACTION_EXECUTE, GetFeaturesRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_FEATURES, ATTRIBUTE_STATUS, ACTION_COMPLETED, GetFeaturesResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY, ATTRIBUTE_ACTION, ACTION_EXECUTE, GetCallHistoryRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_GET_CALL_HISTORY, ATTRIBUTE_STATUS, ACTION_COMPLETED, GetCallHistoryResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_MAKE_CALL, ATTRIBUTE_ACTION, ACTION_EXECUTE, MakeCallRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_MAKE_CALL, ATTRIBUTE_STATUS, ACTION_COMPLETED, MakeCallResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION, ATTRIBUTE_ACTION, ACTION_EXECUTE, RequestActionRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION, ATTRIBUTE_STATUS, ACTION_COMPLETED, RequestActionResult::from));

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <P extends Packet> P parse(@Nonnull final IQ iq) {
        final String namespace = iq.getChildElement().getNamespaceURI();
        switch (namespace) {
        case "http://jabber.org/protocol/commands":
            return (P) parseCommand(iq);
        case "http://jabber.org/protocol/pubsub":
            return (P) parsePubSub(iq);
        default:
            return (P) iq;

        }
    }

    private static IQ parseCommand(@Nonnull final IQ iq) {
        final Element commandElement = iq.getChildElement();
        for (final IQMatcher iqMatcher : STANZA_TYPE_MATCHER_LIST) {
            if (iqMatcher.matches(commandElement)) {
                final IQ parsedIQ = iqMatcher.stanzaFactory.from(iq);
                final PacketError packetError = iq.getError();
                if (packetError != null) {
                    parsedIQ.setError(new PacketError(packetError.getElement().createCopy()));
                }
                return parsedIQ;
            }
        }
        return iq;
    }

    private static IQ parsePubSub(@Nonnull final IQ iq) {

        final Element pubSubElement = iq.getChildElement();
        final Element childElement = (Element) pubSubElement.elements().get(0);
        switch (childElement.getName()) {
        case "subscribe":
        case "unsubscribe":
            return PubSubSubscriptionRequest.from(iq);
        case "subscription":
            return PubSubSubscriptionResult.from(iq);
        case "publish":
            return PubSubPublishRequest.from(iq);
        default:
            return iq;
        }
    }
}
