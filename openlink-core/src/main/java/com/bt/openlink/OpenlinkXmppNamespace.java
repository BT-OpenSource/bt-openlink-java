package com.bt.openlink;

@SuppressWarnings("unused")
public enum OpenlinkXmppNamespace {

    XMPP_PUBSUB("http://jabber.org/protocol/pubsub", "Publish-Subscribe"),
    XMPP_PUBSUB_EVENT("http://jabber.org/protocol/pubsub#event", "Publish-Subscribe event"),
    XMPP_DISCO_INFO("http://jabber.org/protocol/disco#info", "Service Discovery basic information"),
    XMPP_DISCO_ITEMS("http://jabber.org/protocol/disco#items", "Service Discovery node information"),
    XMPP_COMMANDS("http://jabber.org/protocol/commands", "Ad-Hoc Command"),
    XMPP_IO_DATA("urn:xmpp:tmp:io-data", "IO data"),
    OPENLINK_GET_PROFILES("http://xmpp.org/protocol/openlink:01:00:00#get-profiles", "Get profiles"),
    OPENLINK_GET_PROFILE("http://xmpp.org/protocol/openlink:01:00:00#get-profile", "Get profile"),
    OPENLINK_PROFILES("http://xmpp.org/protocol/openlink:01:00:00/profiles", "Profiles"),
    OPENLINK_GET_INTERESTS("http://xmpp.org/protocol/openlink:01:00:00#get-interests", "Get interests"),
    OPENLINK_GET_INTEREST("http://xmpp.org/protocol/openlink:01:00:00#get-interest", "Get interest"),
    OPENLINK_INTERESTS("http://xmpp.org/protocol/openlink:01:00:00/interests", "Interests"),
    OPENLINK_GET_FEATURES("http://xmpp.org/protocol/openlink:01:00:00#get-features", "Get features"),
    OPENLINK_FEATURES("http://xmpp.org/protocol/openlink:01:00:00/features", "Features"),
    OPENLINK_MAKE_CALL("http://xmpp.org/protocol/openlink:01:00:00#make-call", "Make call"),
    OPENLINK_REQUEST_ACTION("http://xmpp.org/protocol/openlink:01:00:00#request-action", "Request action"),
    OPENLINK_MAKE_INTERCOM_CALL("http://xmpp.org/protocol/openlink:01:00:00#make-intercom-call", "Make intercom call"),
    OPENLINK_MANAGE_VOICE_MESSAGE("http://xmpp.org/protocol/openlink:01:00:00#manage-voice-message", "Manage voice message"),
    OPENLINK_MANAGE_VOICE_BLAST("http://xmpp.org/protocol/openlink:01:00:00#manage-voice-blast", "Manage voice blast"),
    OPENLINK_MANAGE_VOICE_BRIDGE("http://xmpp.org/protocol/openlink:01:00:00#manage-voice-bridge", "Manage voice bridge"),
    OPENLINK_MANAGE_INTERESTS("http://xmpp.org/protocol/openlink:01:00:00#manage-interests", "Manage interests"),
    OPENLINK_SET_FEATURES("http://xmpp.org/protocol/openlink:01:00:00#set-features", "Set features"),
    OPENLINK_QUERY_FEATURES("http://xmpp.org/protocol/openlink:01:00:00#query-features", "Query features"),
    OPENLINK_GET_CALL_HISTORY("http://xmpp.org/protocol/openlink:01:00:00#get-call-history", "Get call history"),
    OPENLINK_CALL_STATUS("http://xmpp.org/protocol/openlink:01:00:00#call-status", "Call status message"),
    OPENLINK_DEVICE_STATUS("http://xmpp.org/protocol/openlink:01:00:00#device-status", "Device status message");

    public static final String TAG_IODATA = "iodata";
    public static final String TAG_IN = "in";
    public static final String TAG_OUT = "out";
    public static final String TAG_JID = "jid";
    public static final String TAG_PROFILES = "profiles";
    public static final String TAG_PROFILE = "profile";
    public static final String TAG_DEFAULT = "default";
    public static final String TAG_LABEL = "label";
    public static final String TAG_ACTIONS = "actions";
    public static final String TAG_ACTION = "action";
    public static final String TAG_INTERESTS = "interests";
    public static final String TAG_INTEREST = "interest";
    public static final String TAG_FEATURES = "features";
    public static final String TAG_FEATURE = "feature";

    private final String uri;
    private final String label;

    OpenlinkXmppNamespace(final String uri, final String label) {
        this.uri = uri;
        this.label = label;
    }

    public String label() {
        return label;
    }

    public String uri() {
        return uri;
    }
}
