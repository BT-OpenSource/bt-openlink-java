package com.bt.openlink.smack.internal;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ.IQChildElementXmlStringBuilder;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallFeature;
import com.bt.openlink.type.CallFeatureBoolean;
import com.bt.openlink.type.CallFeatureDeviceKey;
import com.bt.openlink.type.CallFeatureSpeakerChannel;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.ConferenceId;
import com.bt.openlink.type.DeviceKey;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.OriginatorReference;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;
import com.bt.openlink.type.UserId;

public final class SmackPacketUtil {

    private static final DateTimeFormatter JAVA_UTIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final String ATTRIBUTE_DIRECTION = "direction";
    private static final String ATTRIBUTE_START_TIME = "start";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    private static final String ATTRIBUTE_DURATION = "duration";
    private static final String ATTRIBUTE_LABEL = "label";
    private static final String ATTRIBUTE_DEVICEKEYS = "devicekeys";
    private static final String ELEMENT_NUMBER = "number";
    private static final String ELEMENT_CALLER = "caller";
    private static final String ELEMENT_CALLED = "called";
    private static final String ELEMENT_ORIGINATOR_REF = "originator-ref";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ELEMENT_ACTIONS = "actions";
    private static final String ELEMENT_PARTICIPANTS = "participants";
    private static final String ELEMENT_PARTICIPANT = "participant";
    private static final String ELEMENT_FEATURES = "features";
    private static final String ELEMENT_SPEAKERCHANNEL = "speakerchannel";
    private static final String ELEMENT_CHANNEL = "channel";
    private static final String ELEMENT_MICROPHONE = "microphone";
    private static final String ELEMENT_MUTE = "mute";

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
        return getBoolean(attributeValue);
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

    public static IQChildElementXmlStringBuilder addCalls(IQChildElementXmlStringBuilder xml, @Nonnull final Collection<Call> calls) {
        xml.openElement("call");
        for (final Call call : calls) {
            xml.optElement("id", call.getId().orElse(null));
            xml.optElement("conference", call.getConferenceId().orElse(null));
            call.getSite().ifPresent(site -> {
                xml.halfOpenElement("site");
                site.getId().ifPresent(id -> xml.attribute("id", String.valueOf(id)));
                site.isDefault().ifPresent(isDefault -> xml.attribute(OpenlinkXmppNamespace.TAG_DEFAULT, String.valueOf(isDefault)));
                site.getType().ifPresent(type -> xml.attribute("type", type.name()));
                xml.rightAngleBracket();
                site.getName().ifPresent(xml::escape);
                xml.closeElement("site");
            });
            xml.optElement("profile", call.getProfileId().orElse(null));
            xml.optElement("user", call.getUserId().orElse(null));
            xml.optElement("interest", call.getInterestId().orElse(null));
            call.getChanged().ifPresent(changed -> xml.optElement("changed", changed.getId()));
            call.getState().ifPresent(changed -> xml.optElement("state", changed.getLabel()));
            call.getDirection().ifPresent(changed -> xml.optElement(ATTRIBUTE_DIRECTION, changed.getLabel()));

            xml.openElement(ELEMENT_CALLER);
            xml.halfOpenElement(ELEMENT_NUMBER);
            final String callerE164Numbers = String.join(",", call.getCallerE164Numbers().stream().map(PhoneNumber::value).collect(Collectors.toList()));
            xml.attribute("e164", callerE164Numbers);
            xml.rightAngleBracket();
            call.getCallerNumber().ifPresent(callerNumber -> xml.escape(callerNumber.value()));
            xml.closeElement(ELEMENT_NUMBER);
            call.getCallerName().ifPresent(callerName -> xml.optElement("name", callerName));
            xml.closeElement(ELEMENT_CALLER);

            xml.openElement(ELEMENT_CALLED);
            xml.halfOpenElement(ELEMENT_NUMBER);
            call.getCalledDestination().ifPresent(destination -> xml.attribute("destination", destination.value()));
            final String calledE164Numbers = String.join(",", call.getCalledE164Numbers().stream().map(PhoneNumber::value).collect(Collectors.toList()));
            xml.attribute("e164", calledE164Numbers);
            xml.rightAngleBracket();
            call.getCalledNumber().ifPresent(calledNumber -> xml.escape(calledNumber.value()));
            xml.closeElement(ELEMENT_NUMBER);
            call.getCalledName().ifPresent(callerName -> xml.optElement("name", callerName));
            xml.closeElement("called");

            final List<OriginatorReference> originatorReferences = call.getOriginatorReferences();
            if (!originatorReferences.isEmpty()) {
                xml.openElement(ELEMENT_ORIGINATOR_REF);
                originatorReferences.forEach(originatorReference -> {
                    xml.halfOpenElement(ELEMENT_PROPERTY).attribute("id", originatorReference.getKey()).rightAngleBracket();
                    xml.optElement("value", originatorReference.getValue());
                    xml.closeElement(ELEMENT_PROPERTY);
                });
                xml.closeElement(ELEMENT_ORIGINATOR_REF);
            }

            call.getStartTime().ifPresent(startTime -> xml.optElement(ATTRIBUTE_START_TIME, (ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC)))));
            call.getDuration().ifPresent(duration -> xml.optElement(ATTRIBUTE_DURATION, String.valueOf(duration.toMillis())));

            final Collection<RequestAction> actions = call.getActions();
            if (!actions.isEmpty()) {
                xml.openElement(ELEMENT_ACTIONS);
                actions.forEach(action -> {
                    xml.halfOpenElement(action.getId()).rightAngleBracket();
                    xml.closeElement(action.getId());
                });
                xml.closeElement(ELEMENT_ACTIONS);
            }

            addFeatures(call, xml);

            final List<Participant> participants = call.getParticipants();
            if (!participants.isEmpty()) {
                xml.openElement(ELEMENT_PARTICIPANTS);
                participants.forEach(participant -> {
                    xml.halfOpenElement(ELEMENT_PARTICIPANT);
                    participant.getJID().ifPresent(jid -> xml.attribute("jid", jid));
                    participant.getType().ifPresent(type -> xml.attribute("type", type.getId()));
                    participant.getDirection().ifPresent(direction -> xml.attribute("direction", direction.getLabel()));
                    participant.getStartTime().ifPresent(startTime -> {
                        final ZonedDateTime startTimeInUTC = startTime.atZone(TimeZone.getTimeZone("UTC").toZoneId());
                        xml.attribute(ATTRIBUTE_START_TIME, ISO_8601_FORMATTER.format(startTimeInUTC));
                        xml.attribute(ATTRIBUTE_TIMESTAMP, JAVA_UTIL_DATE_FORMATTER.format(startTimeInUTC));
                    });
                    participant.getDuration().ifPresent(duration -> xml.attribute("duration", String.valueOf(duration.toMillis())));
                    xml.rightAngleBracket();
                    xml.closeElement(ELEMENT_PARTICIPANT);
                });
                xml.closeElement(ELEMENT_PARTICIPANTS);
            }

        }
        xml.closeElement("call");
        xml.closeElement("callstatus");
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    private static void addFeatures(final Call call, final IQChildElementXmlStringBuilder xml) {
        final List<CallFeature> features = call.getFeatures();
        if (!features.isEmpty()) {
            xml.openElement(ELEMENT_FEATURES);
            features.forEach(feature -> {
                xml.halfOpenElement("feature");
                feature.getId().ifPresent(id -> xml.attribute("id", id.value()));
                feature.getType().ifPresent(type -> xml.attribute("type", type.getId()));
                if (feature instanceof CallFeatureBoolean) {
                    feature.getLabel().ifPresent(label -> xml.attribute(ATTRIBUTE_LABEL, label));
                    xml.rightAngleBracket();
                    final CallFeatureBoolean callFeatureBoolean = (CallFeatureBoolean) feature;
                    callFeatureBoolean.isEnabled().ifPresent(enabled -> xml.escape(String.valueOf(enabled)));
                } else if (feature instanceof CallFeatureDeviceKey) {
                    feature.getLabel().ifPresent(label -> xml.attribute(ATTRIBUTE_LABEL, label));
                    xml.rightAngleBracket();
                    final CallFeatureDeviceKey callFeatureDeviceKey = (CallFeatureDeviceKey) feature;
                    xml.halfOpenElement(ATTRIBUTE_DEVICEKEYS);
                    xml.attribute("xmlns", OpenlinkXmppNamespace.OPENLINK_DEVICE_KEY.uri());
                    xml.rightAngleBracket();
                    callFeatureDeviceKey.getDeviceKey().ifPresent(deviceKey -> {
                        xml.openElement("key");
                        xml.escape(deviceKey.value());
                        xml.closeElement("key");
                    });
                    xml.closeElement(ATTRIBUTE_DEVICEKEYS);
                } else if (feature instanceof CallFeatureSpeakerChannel) {
                    xml.rightAngleBracket();
                    final CallFeatureSpeakerChannel callFeatureSpeakerChannel = (CallFeatureSpeakerChannel) feature;
                    xml.halfOpenElement(ELEMENT_SPEAKERCHANNEL);
                    xml.attribute("xmlns", OpenlinkXmppNamespace.OPENLINK_SPEAKER_CHANNEL.uri());
                    xml.rightAngleBracket();
                    callFeatureSpeakerChannel.getChannel().ifPresent(channel -> {
                        xml.openElement(ELEMENT_CHANNEL);
                        xml.escape(String.valueOf(channel));
                        xml.closeElement(ELEMENT_CHANNEL);
                    });
                    callFeatureSpeakerChannel.isMicrophoneActive().ifPresent(microphone -> {
                        xml.openElement(ELEMENT_MICROPHONE);
                        xml.escape(String.valueOf(microphone));
                        xml.closeElement(ELEMENT_MICROPHONE);
                    });
                    callFeatureSpeakerChannel.isMuteRequested().ifPresent(muteRequested -> {
                        xml.openElement(ELEMENT_MUTE);
                        xml.escape(String.valueOf(muteRequested));
                        xml.closeElement(ELEMENT_MUTE);
                    });
                    xml.closeElement(ELEMENT_SPEAKERCHANNEL);
                } else {
                    feature.getLabel().ifPresent(label -> xml.attribute(ATTRIBUTE_LABEL, label));
                    xml.rightAngleBracket();
                }
                xml.closeElement("feature");
            });
            xml.closeElement(ELEMENT_FEATURES);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Call> getCalls(
            @Nonnull final XmlPullParser parser,
            @Nonnull final String description,
            @Nonnull final List<String> errors)
            throws IOException, XmlPullParserException {
        final List<Call> calls = new ArrayList<>();
        final Call.Builder callBuilder = Call.Builder.start();
        if (parser.getName().equals("call")) {
            parser.nextTag();
            if (parser.getName().equals("id")) {
                final String callIdString = parser.nextText();
                final Optional<CallId> callIdOptional = CallId.from(callIdString);
                callIdOptional.ifPresent(callBuilder::setId);
                parser.nextTag();
            }
            if (parser.getName().equals("conference")) {
                final String conferenceIdString = parser.nextText();
                final Optional<ConferenceId> conferenceIdOptional = ConferenceId.from(conferenceIdString);
                conferenceIdOptional.ifPresent(callBuilder::setConferenceId);
                parser.nextTag();
            }

            Optional<Site> site = getSite(parser, errors);
            site.ifPresent(callBuilder::setSite);

            if (parser.getName().equals("profile")) {
                final String profileIdString = parser.nextText();
                final Optional<ProfileId> profileIdOptional = ProfileId.from(profileIdString);
                profileIdOptional.ifPresent(callBuilder::setProfileId);
                parser.nextTag();
            }
            if (parser.getName().equals("user")) {
                final String userIdString = parser.nextText();
                final Optional<UserId> userIdOptional = UserId.from(userIdString);
                userIdOptional.ifPresent(callBuilder::setUserId);
                parser.nextTag();
            }
            if (parser.getName().equals("interest")) {
                final String interestIdString = parser.nextText();
                final Optional<InterestId> interestIdOptional = InterestId.from(interestIdString);
                interestIdOptional.ifPresent(callBuilder::setInterestId);
                parser.nextTag();
            }
            if (parser.getName().equals("changed")) {
                final String changedString = parser.nextText();
                final Optional<Changed> changedOptional = Changed.from(changedString);
                changedOptional.ifPresent(callBuilder::setChanged);
                parser.nextTag();
            }
            if (parser.getName().equals("state")) {
                final String callStateString = parser.nextText();
                final Optional<CallState> callStateOptional = CallState.from(callStateString);
                callStateOptional.ifPresent(callBuilder::setState);
                parser.nextTag();
            }
            if (parser.getName().equals(ATTRIBUTE_DIRECTION)) {
                final String callDirectionString = parser.nextText();
                final Optional<CallDirection> callDirectionOptional = CallDirection.from(callDirectionString);
                callDirectionOptional.ifPresent(callBuilder::setDirection);
                parser.nextTag();
            }
            if (parser.getName().equals(ELEMENT_CALLER)) {
                parser.nextTag();
                if (parser.getName().equals(ELEMENT_NUMBER)) {
                    callBuilder.addCallerE164Numbers(getPhoneNumbers(parser));
                    final String callerNumberString = parser.nextText();
                    final Optional<PhoneNumber> callerNumberOptional = PhoneNumber.from(callerNumberString);
                    callerNumberOptional.ifPresent(callBuilder::setCallerNumber);
                    parser.nextTag();
                }
                if (parser.getName().equals("name")) {
                    final Optional<String> callerName = Optional.ofNullable(parser.nextText());
                    callerName.ifPresent(callBuilder::setCallerName);
                    parser.nextTag(); // moves to end of caller tag
                    parser.nextTag(); // moves to start of next tag
                }
            }
            if (parser.getName().equals("called")) {
                parser.nextTag();
                if (parser.getName().equals(ELEMENT_NUMBER)) {
                    callBuilder.addCalledE164Numbers(getPhoneNumbers(parser));
                    final Optional<PhoneNumber> destination = PhoneNumber.from(SmackPacketUtil.getStringAttribute(parser, "destination").orElse(null));
                    destination.ifPresent(callBuilder::setCalledDestination);
                    final String calledNumberString = parser.nextText();
                    final Optional<PhoneNumber> calledNumberOptional = PhoneNumber.from(calledNumberString);
                    calledNumberOptional.ifPresent(callBuilder::setCalledNumber);
                    parser.nextTag();
                }
                if (parser.getName().equals("name")) {
                    final Optional<String> callerName = Optional.ofNullable(parser.nextText());
                    callerName.ifPresent(callBuilder::setCalledName);
                    parser.nextTag(); // moves to end of caller tag
                    parser.nextTag(); // moves to start of next tag
                }
            }

            getOriginatorReferences(parser, callBuilder);
            getChildElementISO8601(ATTRIBUTE_START_TIME, parser, description, errors).ifPresent(callBuilder::setStartTime);
            getChildElementLong(ATTRIBUTE_DURATION, parser, description, errors).map(Duration::ofMillis).ifPresent(callBuilder::setDuration);
            getActions(callBuilder, parser, errors);
            getFeatures(callBuilder, parser, description, errors);
            getParticipants(callBuilder, parser, description, errors);
            calls.add(callBuilder.build(errors));
        }

        return calls;
    }

    private static List<PhoneNumber> getPhoneNumbers(final XmlPullParser parser) {
        final Optional<String> e164String = SmackPacketUtil.getStringAttribute(parser, "e164");
        final List<PhoneNumber> phoneNumbers = new ArrayList<>();
        e164String.ifPresent(string -> Arrays.stream(string.split(",")).map(String::trim).map(PhoneNumber::from)
                .filter(Optional::isPresent).map(Optional::get).forEach(phoneNumbers::add));
        return phoneNumbers;
    }

    @SuppressWarnings("unchecked")
    private static void getParticipants(
            @Nonnull final Call.Builder callBuilder,
            @Nonnull final XmlPullParser parser,
            @Nonnull final String description,
            @Nonnull final List<String> parseErrors) throws IOException, XmlPullParserException {

        parser.nextTag();
        if (parser.getName().equals(ELEMENT_PARTICIPANTS)) {
            parser.nextTag();
            while (parser.getName().equals(ELEMENT_PARTICIPANT)) {
                final Participant.Builder participantBuilder = Participant.Builder.start();
                SmackPacketUtil.getStringAttribute(parser, "jid").ifPresent(participantBuilder::setJID);
                ParticipantType.from(SmackPacketUtil.getStringAttribute(parser, "type").orElse(null))
                        .ifPresent(participantBuilder::setType);
                CallDirection.from(SmackPacketUtil.getStringAttribute(parser, ATTRIBUTE_DIRECTION).orElse(null))
                        .ifPresent(participantBuilder::setDirection);
                final Optional<Instant> participantTimestamp = getJavaUtilDateAttribute(parser, ATTRIBUTE_TIMESTAMP, description, parseErrors);
                participantTimestamp.ifPresent(participantBuilder::setStartTime);
                final Optional<Instant> participantStartTime = getISO8601Attribute(parser, ATTRIBUTE_START_TIME, description, parseErrors);
                participantStartTime.ifPresent(participantBuilder::setStartTime);
                if (participantStartTime.isPresent() && participantTimestamp.isPresent()
                        && !participantStartTime.equals(participantTimestamp)) {
                    parseErrors
                            .add("Invalid participant; the legacy timestamp field does not match the start time field");
                }
                final Optional<Long> participantDuration = SmackPacketUtil.getLongAttribute(parser, ATTRIBUTE_DURATION);
                participantDuration.ifPresent(millis -> participantBuilder.setDuration(Duration.ofMillis(millis)));
                callBuilder.addParticipant(participantBuilder.build(parseErrors));
                ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
                parser.nextTag();
            }
        }
    }

    private static Optional<Instant> getJavaUtilDateAttribute(
            final XmlPullParser parser,
            final String attributeName,
            final String description,
            final List<String> parseErrors) {
        final Optional<String> stringValue = SmackPacketUtil.getStringAttribute(parser, attributeName);
        try {
            return stringValue.map(string -> Instant.from(JAVA_UTIL_DATE_FORMATTER.parse(string)));
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be 'dow mon dd hh:mm:ss zzz yyyy'", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    private static Optional<Instant> getISO8601Attribute(
            @Nonnull final XmlPullParser parser,
            @Nonnull final String attributeName,
            @Nonnull final String description,
            @Nonnull final List<String> parseErrors) {

        final Optional<String> stringValue = SmackPacketUtil.getStringAttribute(parser, attributeName);
        try {
            return stringValue.map(Instant::parse);
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private static void getActions(@Nonnull final Call.Builder callBuilder, final XmlPullParser parser,
            @Nonnull final List<String> parseErrors) throws IOException, XmlPullParserException {
        parser.nextTag();
        if (parser.getName().equals(ELEMENT_ACTIONS)) {
            do {
                parser.nextTag();
                if (parser.getName().equals(OpenlinkXmppNamespace.TAG_ACTION)) {
                    final String actionString = parser.nextText();
                    final Optional<RequestAction> requestAction = RequestAction.from(actionString);
                    if (requestAction.isPresent()) {
                        callBuilder.addAction(requestAction.get());
                    } else {
                        parseErrors.add("Invalid %s: %s is not a valid action");
                    }
                }
                ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
            } while (parser.getName().equals(OpenlinkXmppNamespace.TAG_ACTION));
            ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
            parser.nextTag();

        }
    }

    @SuppressWarnings("unchecked")
    private static void getFeatures(
            @Nonnull final Call.Builder callBuilder,
            @Nonnull final XmlPullParser parser,
            @Nonnull final String description,
            @Nonnull final List<String> parseErrors) throws IOException, XmlPullParserException {
        parser.nextTag();

        if (parser.getName().equals(ELEMENT_FEATURES)) {

            parser.nextTag();
            while (OpenlinkXmppNamespace.TAG_FEATURE.equals(parser.getName())) {
                final CallFeature.AbstractCallFeatureBuilder callFeatureBuilder;

                final Optional<FeatureId> featureId = FeatureId.from(parser.getAttributeValue("", "id"));
                final Optional<String> featureTypeString = SmackPacketUtil.getStringAttribute(parser, "type");
                final Optional<String> label = SmackPacketUtil.getStringAttribute(parser, OpenlinkXmppNamespace.TAG_LABEL);
                String text = "";
                while (parser.next() == XmlPullParser.TEXT) {
                    text = parser.getText();
                }
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                    case ATTRIBUTE_DEVICEKEYS:
                        final CallFeatureDeviceKey.Builder deviceKeyBuilder = CallFeatureDeviceKey.Builder.start();
                        if (parser.nextTag() == XmlPullParser.START_TAG && "key".equals(parser.getName())) {
                            DeviceKey.from(parser.nextText()).ifPresent(deviceKeyBuilder::setDeviceKey);
                        }
                        callFeatureBuilder = deviceKeyBuilder;
                        break;

                    case "speakerchannel":
                        final CallFeatureSpeakerChannel.Builder speakerChannelBuilder = CallFeatureSpeakerChannel.Builder.start();
                        getChildElementLong(ELEMENT_CHANNEL, parser, description, parseErrors).ifPresent(speakerChannelBuilder::setChannel);
                        getChildElementBoolean(ELEMENT_MICROPHONE, parser, description, parseErrors).ifPresent(speakerChannelBuilder::setMicrophoneActive);
                        getChildElementBoolean(ELEMENT_MUTE, parser, description, parseErrors).ifPresent(speakerChannelBuilder::setMuteRequested);
                        callFeatureBuilder = speakerChannelBuilder;
                        break;

                    default:
                        // Assume a simple true/false feature
                        final CallFeatureBoolean.Builder booleanBuilder = CallFeatureBoolean.Builder.start();
                        getBoolean(text).ifPresent(booleanBuilder::setEnabled);
                        callFeatureBuilder = booleanBuilder;
                        break;
                    }
                } else {
                    // Assume a simple true/false feature
                    final CallFeatureBoolean.Builder booleanBuilder = CallFeatureBoolean.Builder.start();
                    getBoolean(text).ifPresent(booleanBuilder::setEnabled);
                    callFeatureBuilder = booleanBuilder;
                }
                featureId.ifPresent(callFeatureBuilder::setId);
                featureTypeString.ifPresent(featureType -> {
                    final Optional<FeatureType> type = FeatureType.from(featureType);
                    if (type.isPresent()) {
                        callFeatureBuilder.setType(type.get());
                    } else {
                        parseErrors.add("Invalid %s; invalid feature type - '%s'");
                    }
                });
                label.ifPresent(callFeatureBuilder::setLabel);
                callBuilder.addFeature(callFeatureBuilder.build(parseErrors));
                ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
                parser.nextTag();
            }

        }
    }

    private static Optional<Boolean> getBoolean(String featureText) {
        if ("true".equals(featureText)) {
            return Optional.of(Boolean.TRUE);
        } else if ("false".equals(featureText)) {
            return Optional.of(Boolean.FALSE);
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private static void getOriginatorReferences(final XmlPullParser parser, final Call.Builder callBuilder)
            throws XmlPullParserException, IOException {

        //parser.nextTag();
        if (!parser.getName().equals(ELEMENT_ORIGINATOR_REF)) {
            return;
        }
        parser.nextTag();

        while (parser.getName().equals(ELEMENT_PROPERTY)) {
            final String key = SmackPacketUtil.getStringAttribute(parser, "id").orElse("");
            parser.nextTag();
            String value = "";
            if (parser.getName().equals("value"))
                value = parser.nextText();
            callBuilder.addOriginatorReference(key, value);
            ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
            parser.nextTag(); // end of property tag
            parser.nextTag(); // start of next tag
        }

    }

    @Nonnull
    private static Optional<Instant> getChildElementISO8601(
            @Nonnull final String childElementName,
            @Nonnull final XmlPullParser parser,
            @Nonnull final String description,
            @Nonnull final List<String> parseErrors)
            throws XmlPullParserException, IOException {
        parser.nextTag();
        if (parser.getName().equals(childElementName)) {
            final String childElementText = parser.nextText();
            try {
                return Optional.of(Instant.parse(childElementText));
            } catch (final DateTimeParseException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", description, childElementName, childElementText));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<Long> getChildElementLong(
            @Nonnull final String childElementName,
            @Nonnull final XmlPullParser parser,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors)
            throws XmlPullParserException, IOException {
        parser.nextTag();
        if (parser.getName().equals(childElementName)) {
            final String childElementText = parser.nextText();
            try {
                return Optional.of(Long.parseLong(childElementText));
            } catch (final NumberFormatException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; please supply an integer", stanzaDescription, childElementName, childElementText));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<Boolean> getChildElementBoolean(
            @Nonnull final String childElementName,
            @Nonnull final XmlPullParser parser,
            @Nonnull final String description,
            @Nonnull final List<String> parseErrors) throws XmlPullParserException, IOException {
        parser.nextTag();
        if (parser.getName().equals(childElementName)) {
            final String value = parser.nextText();
            try {
                return getBoolean(value);
            } catch (final NumberFormatException ignored) {
                parseErrors.add(String.format("Invalid %s: %s is neither true or false", description, value));
            }
        }
        return Optional.empty();
    }
}
