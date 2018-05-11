package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.UnparsedIQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;

public class OpenlinkIQProvider extends IQProvider<IQ> {

    private interface StanzaFactory {
        IQ from(final XmlPullParser parser) throws IOException, XmlPullParserException;
    }

    private static class IQMatcher {

        @Nonnull private final OpenlinkXmppNamespace namespace;
        @Nonnull private final String attribute;
        @Nonnull private final String value;
        @Nonnull private final StanzaFactory stanzaFactory;

        IQMatcher(@Nonnull final OpenlinkXmppNamespace namespace, @Nonnull final String attribute, @Nonnull final String value, @Nonnull final StanzaFactory stanzaFactory) {
            this.namespace = namespace;
            this.attribute = attribute;
            this.value = value;
            this.stanzaFactory = stanzaFactory;
        }

        boolean matches(@Nullable final String namespace, @Nullable final String value) {
            return this.value.equals(value) && this.namespace.uri().equals(namespace);
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
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_MAKE_CALL, ATTRIBUTE_ACTION, ACTION_EXECUTE, MakeCallRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_MAKE_CALL, ATTRIBUTE_STATUS, ACTION_COMPLETED, MakeCallResult::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION, ATTRIBUTE_ACTION, ACTION_EXECUTE, RequestActionRequest::from),
            new IQMatcher(OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION, ATTRIBUTE_STATUS, ACTION_COMPLETED, RequestActionResult::from)
            );

    @Override
    public IQ parse(XmlPullParser xmlPullParser, int initialDepth) throws Exception {
        final String node = xmlPullParser.getAttributeValue("", "node");
        for (final IQMatcher iqMatcher : STANZA_TYPE_MATCHER_LIST) {
            final String value = xmlPullParser.getAttributeValue("", iqMatcher.attribute);
            if (iqMatcher.matches(node, value)) {
                return iqMatcher.stanzaFactory.from(xmlPullParser);
            }
        }

        return new UnparsedIQ("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), PacketParserUtils.parseElement(xmlPullParser));
    }
}
