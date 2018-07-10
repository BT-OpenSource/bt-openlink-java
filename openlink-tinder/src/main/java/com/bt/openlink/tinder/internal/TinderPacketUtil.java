package com.bt.openlink.tinder.internal;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.bt.openlink.type.CallFeatureTextValue;
import com.bt.openlink.type.CallFeatureVoiceRecorder;
import com.bt.openlink.type.ParticipantCategory;
import com.bt.openlink.type.RecorderChannel;
import com.bt.openlink.type.RecorderNumber;
import com.bt.openlink.type.RecorderPort;
import com.bt.openlink.type.RecorderType;
import com.bt.openlink.type.VoiceRecorderInfo;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.message.PubSubMessageBuilder;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallFeature;
import com.bt.openlink.type.CallFeatureBoolean;
import com.bt.openlink.type.CallFeatureDeviceKey;
import com.bt.openlink.type.CallFeatureSpeakerChannel;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.CallStatus;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.ConferenceId;
import com.bt.openlink.type.DeviceKey;
import com.bt.openlink.type.DeviceStatus;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.ItemId;
import com.bt.openlink.type.OriginatorReference;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.PubSubNodeId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;
import com.bt.openlink.type.TelephonyCallId;
import com.bt.openlink.type.UserId;

/**
 * This class is for internal use by the library only; users of the API should not access this class directly.
 */
public final class TinderPacketUtil {

    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter JAVA_UTIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
    private static final String ATTRIBUTE_DIRECTION = "direction";
    private static final String ATTRIBUTE_DESTINATION = "destination";
    private static final String ATTRIBUTE_NUMBER = "number";
    private static final String ATTRIBUTE_START_TIME = "start";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    private static final String ATTRIBUTE_DURATION = "duration";
    private static final String ELEMENT_NUMBER = "number";
    private static final String ELEMENT_PROFILE = "profile";

    private TinderPacketUtil() {
    }

    @Nullable
    public static Element getIOInElement(@Nonnull final IQ iq) {
        return getChildElement(iq.getChildElement(), OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);
    }

    @Nullable
    public static Element getIOOutElement(@Nonnull final IQ iq) {
        return getChildElement(iq.getChildElement(), OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT);
    }

    @Nullable
    public static Element getChildElement(@Nullable final Element element, final String... elementNames) {
        Element childElement = element;
        for (final String elementName : elementNames) {
            if (childElement == null) {
                break;
            }
            childElement = childElement.element(elementName);
        }
        return childElement;
    }

    public static void addElementWithTextIfNotNull(
            @Nonnull final Element elementToAddTo,
            @Nonnull final String elementName,
            @Nullable final Object elementValue) {
        if (elementValue != null) {
            final Element callerElement = elementToAddTo.addElement(elementName);
            callerElement.setText(elementValue.toString());
        }
    }

    public static void addElementWithTextIfNotNull(
            @Nonnull final Element elementToAddTo,
            @Nonnull final String elementName,
            @Nullable final LocalDate localDate,
            @Nonnull final DateTimeFormatter dateFormatter) {
        if (localDate != null) {
            final String stringValue = dateFormatter.format(localDate);
            addElementWithTextIfNotNull(elementToAddTo, elementName, stringValue);
        }
    }

    @Nonnull
    public static Optional<String> getOptionalChildElementString(@Nullable final Element parentElement, @Nonnull final String childElementName) {
        return Optional.ofNullable(getNullableChildElementString(parentElement, childElementName));
    }

    @Nullable
    public static String getNullableChildElementString(@Nullable final Element parentElement, @Nonnull final String childElementName) {
        final Element childElement = getChildElement(parentElement, childElementName);
        if (childElement != null) {
            final String childElementText = childElement.getText().trim();
            if (!childElementText.isEmpty()) {
                return childElementText;
            }
        }
        return null;
    }

    @Nonnull
    public static Optional<LocalDate> getChildElementLocalDate(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final DateTimeFormatter dateTimeFormatter,
            final String stanzaDescription,
            @Nonnull final String dateFormat,
            final List<String> parseErrors) {
        final String dateText = getNullableChildElementString(parentElement, childElementName);
        if (dateText != null) {
            try {
                return Optional.of(LocalDate.parse(dateText, dateTimeFormatter));
            } catch (final DateTimeParseException e) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; date format is '%s'", stanzaDescription, childElementName, dateText, dateFormat));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    private static Optional<Instant> getChildElementISO8601(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getNullableChildElementString(parentElement, childElementName);
        if (childElementText != null) {
            try {
                return Optional.of(Instant.parse(childElementText));
            } catch (final DateTimeParseException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", stanzaDescription, childElementName, childElementText));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<Long> getChildElementLong(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getNullableChildElementString(parentElement, childElementName);
        if (childElementText != null) {
            try {
                return Optional.of(Long.parseLong(childElementText));
            } catch (final NumberFormatException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; please supply an integer", stanzaDescription, childElementName, childElementText));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    private static Optional<Boolean> getChildElementBoolean(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getNullableChildElementString(parentElement, childElementName);
        return getBoolean(childElementText, stanzaDescription, parseErrors);
    }

    @Nonnull
    public static Optional<String> getStringAttribute(@Nullable final Element element, @Nonnull final String attributeName) {
        return Optional.ofNullable(getNullableStringAttribute(element, attributeName));
    }

    @Nullable
    public static String getNullableStringAttribute(@Nullable final Element element, @Nonnull final String attributeName) {
        return getNullableStringAttribute(element, attributeName, false, "", Collections.emptyList());
    }

    @Nonnull
    public static Optional<String> getStringAttribute(
            @Nullable final Element element,
            @Nonnull final String attributeName,
            final boolean isRequired,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        return Optional.ofNullable(getNullableStringAttribute(element, attributeName, isRequired, stanzaDescription, parseErrors));
    }

    @Nullable
    public static String getNullableStringAttribute(
            @Nullable final Element element,
            @Nonnull final String attributeName,
            final boolean isRequired,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String attributeValue;
        if (element == null) {
            attributeValue = null;
        } else {
            final String valueString = element.attributeValue(attributeName);
            attributeValue = valueString == null || valueString.isEmpty() ? null : valueString;
        }
        if (attributeValue == null && isRequired) {
            parseErrors.add(String.format("Invalid %s; missing '%s' attribute is mandatory", stanzaDescription, attributeName));
        }
        return attributeValue;
    }

    @Nonnull
    private static Optional<Long> getLongAttribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName);
        try {
            return stringValue.map(Long::valueOf);
        } catch (final NumberFormatException e) {
            parseErrors.add(String.format("Invalid %s; Unable to parse number attribute %s: '%s'", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    @Nonnull
    public static Optional<Integer> getIntegerAttribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName);
        try {
            return stringValue.map(Integer::valueOf);
        } catch (final NumberFormatException e) {
            parseErrors.add(String.format("Invalid %s; Unable to parse number attribute %s: '%s'", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    private static Optional<Instant> getISO8601Attribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, false, description, parseErrors);
        try {
            return stringValue.map(Instant::parse);
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    private static Optional<Instant> getJavaUtilDateAttribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, false, description, parseErrors);
        try {
            return stringValue.map(string -> Instant.from(JAVA_UTIL_DATE_FORMATTER.parse(string)));
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be 'dow mon dd hh:mm:ss zzz yyyy'", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    @Nonnull
    public static Optional<Boolean> getBooleanAttribute(final Element element, final String id, final String description, final List<String> parseErrors) {
        return getBoolean(getNullableStringAttribute(element, id), description, parseErrors);
    }

    @Nonnull
    private static Element addCommandElement(@Nonnull final IQ request) {
        return request.getElement().addElement("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Nonnull
    public static Element addCommandIOInputElement(@Nonnull final IQ request, @Nonnull final OpenlinkXmppNamespace namespace) {
        final Element commandElement = addCommandElement(request);
        commandElement.addAttribute("action", "execute");
        commandElement.addAttribute("node", namespace.uri());
        final Element ioInputElement = commandElement.addElement(OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.XMPP_IO_DATA.uri());
        ioInputElement.addAttribute("type", "input");
        return ioInputElement.addElement(OpenlinkXmppNamespace.TAG_IN);
    }

    @Nonnull
    public static Element addCommandIOOutputElement(@Nonnull final IQ result, @Nonnull final OpenlinkXmppNamespace namespace) {
        final Element commandElement = addCommandElement(result);
        commandElement.addAttribute("status", "completed");
        commandElement.addAttribute("node", namespace.uri());
        final Element ioInputElement = commandElement.addElement(OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.XMPP_IO_DATA.uri());
        ioInputElement.addAttribute("type", "output");
        return ioInputElement.addElement(OpenlinkXmppNamespace.TAG_OUT);
    }

    @Nonnull
    public static Optional<JID> getJID(String jidString) {
        return jidString == null || jidString.isEmpty() ? Optional.empty() : Optional.of(new JID(jidString));
    }

    public static void addCallStatus(@Nonnull final Element parentElement, @Nonnull final CallStatus callStatus) {
        final Element callStatusElement = parentElement.addElement("callstatus", OpenlinkXmppNamespace.OPENLINK_CALL_STATUS.uri());
        callStatus.isCallStatusBusy().ifPresent(callStatusBusy -> callStatusElement.addAttribute("busy", String.valueOf(callStatusBusy)));
        callStatus.getCalls().forEach(call -> {
            final Element callElement = callStatusElement.addElement("call");
            final Element idElement = callElement.addElement("id");
            call.getId().ifPresent(callId -> idElement.setText(callId.value()));
            call.getTelephonyCallId().ifPresent(telephonyCallId -> idElement.addAttribute("telephony", telephonyCallId.value()));
            call.getConferenceId().ifPresent(conferenceId -> callElement.addElement("conference").setText(conferenceId.value()));
            call.getSite().ifPresent(site -> addSite(callElement, site));
            call.getProfileId().ifPresent(profileId -> callElement.addElement(ELEMENT_PROFILE).setText(profileId.value()));
            call.getUserId().ifPresent(userId -> callElement.addElement("user").setText(userId.value()));
            call.getInterestId().ifPresent(interestId -> callElement.addElement("interest").setText(interestId.value()));
            call.getChanged().ifPresent(changed -> callElement.addElement("changed").setText(changed.getId()));
            call.getState().ifPresent(state -> callElement.addElement("state").setText(state.getLabel()));
            call.getDirection().ifPresent(direction -> callElement.addElement(ATTRIBUTE_DIRECTION).setText(direction.getLabel()));
            final Element callerElement = callElement.addElement("caller");
            final Element callerNumberElement = callerElement.addElement(ELEMENT_NUMBER);
            call.getCallerNumber().ifPresent(callerNumber -> callerNumberElement.setText(callerNumber.value()));
            addList(callerNumberElement, call.getCallerE164Numbers(), "e164");
            final Element callerNameElement = callerElement.addElement("name");
            call.getCallerName().ifPresent(callerNameElement::setText);
            final Element calledElement = callElement.addElement("called");
            final Element calledNumberElement = calledElement.addElement(ELEMENT_NUMBER);
            call.getCalledNumber().ifPresent(calledNumber -> calledNumberElement.setText(calledNumber.value()));
            call.getCalledDestination().ifPresent(calledDestination -> calledNumberElement.addAttribute(ATTRIBUTE_DESTINATION, calledDestination.value()));
            addList(calledNumberElement, call.getCalledE164Numbers(), "e164");
            addOriginatorReferences(callElement, call.getOriginatorReferences());
            final Element calledNameElement = calledElement.addElement("name");
            call.getCalledName().ifPresent(calledNameElement::setText);
            call.getStartTime().ifPresent(startTime -> callElement.addElement(ATTRIBUTE_START_TIME).setText(ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC))));
            call.getDuration().ifPresent(duration -> callElement.addElement(ATTRIBUTE_DURATION).setText(String.valueOf(duration.toMillis())));
            addActions(call, callElement);
            addFeatures(call, callElement);
            addParticipants(call, callElement);
        });
    }

    private static void addList(Element element, List<?> list, String attributeName) {
        final String string = String.join(",", list.stream().map(Object::toString).collect(Collectors.toList()));
        if (!string.isEmpty()) {
            element.addAttribute(attributeName, string);
        }
    }

    public static void addOriginatorReferences(final Element callElement, final List<OriginatorReference> originatorReferences) {
        if (!originatorReferences.isEmpty()) {
            final Element originatorRefElement = callElement.addElement("originator-ref");
            originatorReferences.forEach(originatorReference -> {
                final Element propertyElement = originatorRefElement.addElement("property").addAttribute("id", originatorReference.getKey());
                propertyElement.addElement("value").setText(originatorReference.getValue());

            });
        }
    }

    public static void addDeviceStatus(@Nonnull final Element itemElement, @Nonnull final DeviceStatus deviceStatus) {
        final Element deviceStatusElement = itemElement.addElement("devicestatus", OpenlinkXmppNamespace.OPENLINK_DEVICE_STATUS.uri());
        final Element profileElement = deviceStatusElement.addElement(ELEMENT_PROFILE);
        deviceStatus.isOnline().ifPresent(online -> profileElement.addAttribute("online", String.valueOf(online)));
        deviceStatus.getProfileId().ifPresent(profileId -> profileElement.setText(profileId.value()));
        deviceStatus.getDeviceId().ifPresent(deviceId -> profileElement.addAttribute("devicenum", String.valueOf(deviceId)));
    }

    private static void addFeatures(@Nonnull final Call call, @Nonnull final Element callElement) {
        final List<CallFeature> callFeatures = call.getFeatures();
        if (!callFeatures.isEmpty()) {
            final Element featuresElement = callElement.addElement("features");
            callFeatures.forEach(feature -> {
                final Element featureElement = featuresElement.addElement("feature");
                feature.getId().ifPresent(id -> featureElement.addAttribute("id", id.value()));
                feature.getType().ifPresent(type -> featureElement.addAttribute("type", type.getId()));
                final Optional<String> featureLabel;
                if (feature instanceof CallFeatureBoolean) {
                    final CallFeatureBoolean callFeatureBoolean = (CallFeatureBoolean) feature;
                    callFeatureBoolean.isEnabled().ifPresent(enabled -> featureElement.setText(String.valueOf(enabled)));
                    featureLabel = feature.getLabel();
                } else if (feature instanceof CallFeatureTextValue) {
                    final CallFeatureTextValue callFeatureText = (CallFeatureTextValue) feature;
                    callFeatureText.getValue().ifPresent(enabled -> featureElement.setText(String.valueOf(enabled)));
                    featureLabel = feature.getLabel();
                } else if (feature instanceof CallFeatureDeviceKey) {
                    final CallFeatureDeviceKey callFeatureDeviceKey = (CallFeatureDeviceKey) feature;
                    final Element deviceKeysElement = featureElement.addElement("devicekeys", OpenlinkXmppNamespace.OPENLINK_DEVICE_KEY.uri());
                    callFeatureDeviceKey.getDeviceKeys().forEach(deviceKey -> deviceKeysElement.addElement("key").setText(deviceKey.value()));
                    featureLabel = feature.getLabel();
                } else if (feature instanceof CallFeatureSpeakerChannel) {
                    final CallFeatureSpeakerChannel callFeatureSpeakerChannel = (CallFeatureSpeakerChannel) feature;
                    final Element speakerChannelElement = featureElement.addElement("speakerchannel", OpenlinkXmppNamespace.OPENLINK_SPEAKER_CHANNEL.uri());
                    callFeatureSpeakerChannel.getChannel().ifPresent(channelNumber -> speakerChannelElement.addElement("channel").setText(String.valueOf(channelNumber)));
                    callFeatureSpeakerChannel.isMicrophoneActive().ifPresent(microphoneActive -> speakerChannelElement.addElement("microphone").setText(String.valueOf(microphoneActive)));
                    callFeatureSpeakerChannel.isMuteRequested().ifPresent(muteRequested -> speakerChannelElement.addElement("mute").setText(String.valueOf(muteRequested)));
                    featureLabel = Optional.empty();
                } else if (feature instanceof CallFeatureVoiceRecorder) {
                    final CallFeatureVoiceRecorder callFeatureVoiceRecorder = (CallFeatureVoiceRecorder) feature;
                    final Element voiceRecorderElement = featureElement.addElement("voicerecorder", OpenlinkXmppNamespace.OPENLINK_VOICE_RECORDER.uri());
                    callFeatureVoiceRecorder.getVoiceRecorderInfo()
                            .ifPresent(voiceRecorderInfo -> {
                                voiceRecorderInfo.getRecorderNumber().ifPresent(recorderChannel -> voiceRecorderElement.addElement("recnumber").setText(recorderChannel.value()));
                                voiceRecorderInfo.getRecorderPort().ifPresent(recorderPort -> voiceRecorderElement.addElement("recport").setText(recorderPort.value()));
                                voiceRecorderInfo.getRecorderChannel().ifPresent(recorderChannel -> voiceRecorderElement.addElement("recchan").setText(recorderChannel.value()));
                                voiceRecorderInfo.getRecorderType().ifPresent(recorderType -> voiceRecorderElement.addElement("rectype").setText(recorderType.value()));
                            });
                    featureLabel = Optional.empty();
                } else {
                    featureLabel = feature.getLabel();
                }
                featureLabel.ifPresent(label -> featureElement.addAttribute("label", label));
            });
        }
    }

    private static void addActions(@Nonnull final Call call, @Nonnull final Element callElement) {
        final Collection<RequestAction> actions = call.getActions();
        final Element actionsElement = callElement.addElement("actions");
        if (!actions.isEmpty()) {
            actions.forEach(action -> actionsElement.addElement(action.getId()));
        }
    }

    private static void addParticipants(@Nonnull final Call call, @Nonnull final Element callElement) {
        final List<Participant> participants = call.getParticipants();
        if (!participants.isEmpty()) {
            final Element participantsElement = callElement.addElement("participants");
            participants.forEach(participant -> {
                final Element participantElement = participantsElement.addElement("participant");
                participant.getJID().ifPresent(jid -> participantElement.addAttribute("jid", jid));
                participant.getNumber().ifPresent(number -> participantElement.addAttribute(ATTRIBUTE_NUMBER, number.value()));
                addList(participantElement, participant.getE164Numbers(), "e164Number");
                participant.getDestinationNumber().ifPresent(destination -> participantElement.addAttribute(ATTRIBUTE_DESTINATION, destination.value()));
                participant.getType().ifPresent(type -> participantElement.addAttribute("type", type.getId()));
                participant.getParticipantCategory().ifPresent(category -> participantElement.addAttribute("category", category.getId()));
                participant.getDirection().ifPresent(direction -> participantElement.addAttribute(ATTRIBUTE_DIRECTION, direction.getLabel()));
                participant.getStartTime().ifPresent(startTime -> {
                    final ZonedDateTime startTimeInUTC = startTime.atZone(TimeZone.getTimeZone("UTC").toZoneId());
                    participantElement.addAttribute(ATTRIBUTE_START_TIME, ISO_8601_FORMATTER.format(startTimeInUTC));
                    // Include the legacy timestamp attribute too
                        participantElement.addAttribute(ATTRIBUTE_TIMESTAMP, JAVA_UTIL_DATE_FORMATTER.format(startTimeInUTC));
                    });
                participant.getDuration().ifPresent(duration -> participantElement.addAttribute(ATTRIBUTE_DURATION, String.valueOf(duration.toMillis())));
            });
        }
    }

    public static void addSite(final Element parentElement, final Site site) {
        final Element siteElement = parentElement.addElement("site");
        site.getId().ifPresent(id -> siteElement.addAttribute("id", String.valueOf(id)));
        site.isDefault().ifPresent(isDefault -> siteElement.addAttribute("default", String.valueOf(isDefault)));
        site.getType().ifPresent(type -> siteElement.addAttribute("type", type.getLabel()));
        site.getName().ifPresent(siteElement::setText);
    }

    public static Optional<Site> getSite(@Nonnull final Element parentElement, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final Element siteElement = parentElement.element("site");
        if (siteElement == null) {
            return Optional.empty();
        }
        final Site.Builder siteBuilder = Site.Builder.start()
                .setName(siteElement.getText());
        final Optional<Long> id = getLongAttribute(siteElement, "id", description, parseErrors);
        id.ifPresent(siteBuilder::setId);
        final Optional<Boolean> isDefaultSite = getBooleanAttribute(siteElement, OpenlinkXmppNamespace.TAG_DEFAULT, description, parseErrors);
        isDefaultSite.ifPresent(siteBuilder::setDefault);
        final Optional<Site.Type> type = Site.Type.from(getNullableStringAttribute(siteElement, "type", false, description, parseErrors));
        type.ifPresent(siteBuilder::setType);
        return Optional.of(siteBuilder.build(parseErrors));
    }

    private static List<PhoneNumber> getPhoneNumbers(@Nullable final Element parentElement, String attributeName) {
        final Optional<String> e164String = getStringAttribute(parentElement, attributeName);
        final List<PhoneNumber> phoneNumbers = new ArrayList<>();
        e164String.ifPresent(string -> Arrays.stream(string.split(","))
                .map(String::trim)
                .map(PhoneNumber::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(phoneNumbers::add));
        return phoneNumbers;
    }

    @SuppressWarnings("unchecked")
    public static Optional<CallStatus> getCallStatus(@Nullable final Element parentElement, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final Element callStatusElement = getChildElement(parentElement, "callstatus");
        if (callStatusElement == null) {
            return Optional.empty();
        }

        final CallStatus.Builder builder = CallStatus.Builder.start();
        TinderPacketUtil.getBooleanAttribute(callStatusElement, "busy", "callstatus busy attribute", parseErrors).ifPresent(builder::setCallStatusBusy);
        final List<Element> callElements = callStatusElement.elements("call");
        for (final Element callElement : callElements) {
            final Element callerElement = getChildElement(callElement, "caller");
            final Element calledElement = getChildElement(callElement, "called");
            final Call.Builder callBuilder = Call.Builder.start();
            CallId.from(getNullableChildElementString(callElement, "id")).ifPresent(callBuilder::setId);
            TelephonyCallId.from(getNullableStringAttribute(getChildElement(callElement, "id"), "telephony")).ifPresent(callBuilder::setTelephonyCallId);
            ConferenceId.from(getNullableChildElementString(callElement, "conference")).ifPresent(callBuilder::setConferenceId);
            getSite(callElement, description, parseErrors).ifPresent(callBuilder::setSite);
            ProfileId.from(getNullableChildElementString(callElement, ELEMENT_PROFILE)).ifPresent(callBuilder::setProfileId);
            UserId.from(getNullableChildElementString(callElement, "user")).ifPresent(callBuilder::setUserId);
            InterestId.from(getNullableChildElementString(callElement, "interest")).ifPresent(callBuilder::setInterestId);
            Changed.from(getNullableChildElementString(callElement, "changed")).ifPresent(callBuilder::setChanged);
            CallState.from(getNullableChildElementString(callElement, "state")).ifPresent(callBuilder::setState);
            CallDirection.from(getNullableChildElementString(callElement, ATTRIBUTE_DIRECTION)).ifPresent(callBuilder::setDirection);
            PhoneNumber.from(getNullableChildElementString(callerElement, ELEMENT_NUMBER)).ifPresent(callBuilder::setCallerNumber);
            getOptionalChildElementString(callerElement, "name").ifPresent(callBuilder::setCallerName);
            callBuilder.addCallerE164Numbers(getPhoneNumbers(getChildElement(callerElement, ELEMENT_NUMBER), "e164"));
            PhoneNumber.from(getNullableChildElementString(calledElement, ELEMENT_NUMBER)).ifPresent(callBuilder::setCalledNumber);
            getOptionalChildElementString(calledElement, "name").ifPresent(callBuilder::setCalledName);
            PhoneNumber.from(getNullableStringAttribute(getChildElement(calledElement, ELEMENT_NUMBER), ATTRIBUTE_DESTINATION)).ifPresent(callBuilder::setCalledDestination);
            callBuilder.addCalledE164Numbers(getPhoneNumbers(getChildElement(calledElement, ELEMENT_NUMBER), "e164"));
            getOriginatorReferences(callElement).forEach(callBuilder::addOriginatorReference);
            getChildElementISO8601(callElement, ATTRIBUTE_START_TIME, description, parseErrors).ifPresent(callBuilder::setStartTime);
            getChildElementLong(callElement, ATTRIBUTE_DURATION, description, parseErrors).map(Duration::ofMillis).ifPresent(callBuilder::setDuration);
            getActions(callElement, callBuilder, description, parseErrors);
            getFeatures(callElement, callBuilder, description, parseErrors);
            getParticipants(callElement, callBuilder, description, parseErrors);
            builder.addCall(callBuilder.build(parseErrors));
        }
        return Optional.of(builder.build(parseErrors));
    }

    @SuppressWarnings("unchecked")
    public static List<OriginatorReference> getOriginatorReferences(final Element parentElement) {

        final List<OriginatorReference> originatorReferences = new ArrayList<>();

        final Element originatorRefElement = getChildElement(parentElement, "originator-ref");
        if (originatorRefElement != null) {
            final List<Element> propertyElements = originatorRefElement.elements("property");
            propertyElements.forEach(propertyElement -> {
                final String key = getStringAttribute(propertyElement, "id").orElse("");
                final String value = getOptionalChildElementString(propertyElement, "value").orElse("");
                originatorReferences.add(new OriginatorReference(key, value));
            });
        }
        return originatorReferences;
    }

    public static Optional<DeviceStatus> getDeviceStatus(@Nullable final Element deviceStatusElement, @Nonnull final String stanzaDescription, @Nonnull final List<String> parseErrors) {
        final Element profileElement = getChildElement(deviceStatusElement, ELEMENT_PROFILE);
        if (profileElement == null) {
            return Optional.empty();
        }

        final DeviceStatus.Builder builder = DeviceStatus.Builder.start();

        getBooleanAttribute(profileElement, "online", stanzaDescription, parseErrors).ifPresent(builder::setOnline);
        getStringAttribute(profileElement, "devicenum", false, stanzaDescription, parseErrors).ifPresent(builder::setDeviceId);
        ProfileId.from(getNullableChildElementString(deviceStatusElement, ELEMENT_PROFILE)).ifPresent(builder::setProfileId);

        return Optional.of(builder.build(parseErrors));
    }

    @SuppressWarnings("unchecked")
    private static void getFeatures(@Nonnull final Element callElement, @Nonnull final Call.Builder callBuilder, final String description, List<String> parseErrors) {
        final Element featuresElement = callElement.element("features");
        if (featuresElement != null) {
            final List<Element> featureElements = featuresElement.elements("feature");
            for (final Element featureElement : featureElements) {
                final Optional<FeatureId> featureId = FeatureId.from(featureElement.attributeValue("id"));
                Optional<String> label = Optional.ofNullable(featureElement.attributeValue("label"));
                final Optional<FeatureType> featureType = FeatureType.from(featureElement.attributeValue("type"));

                final Iterator<Element> elementIterator = featureElement.elementIterator();
                final boolean hasChildElement = elementIterator.hasNext();
                final CallFeature.AbstractCallFeatureBuilder callFeatureBuilder;
                if (hasChildElement) {
                    final Element childElement = elementIterator.next();
                    final String childElementName = childElement.getName();
                    switch (childElementName) {
                    case "devicekeys":
                        final CallFeatureDeviceKey.Builder deviceKeyBuilder = CallFeatureDeviceKey.Builder.start();
                        final List<Element> keysElements = childElement.elements("key");
                        for (Element keyElement : keysElements) {
                            DeviceKey.from(Optional.ofNullable(keyElement.getText()).map(String::trim).orElse(null))
                                    .ifPresent(deviceKeyBuilder::addDeviceKey);
                        }
                        callFeatureBuilder = deviceKeyBuilder;
                        break;
                    case "speakerchannel":
                        final CallFeatureSpeakerChannel.Builder speakerChannelBuilder = CallFeatureSpeakerChannel.Builder.start();
                        getChildElementLong(childElement, "channel", description, parseErrors).ifPresent(speakerChannelBuilder::setChannel);
                        getChildElementBoolean(childElement, "microphone", description, parseErrors).ifPresent(speakerChannelBuilder::setMicrophoneActive);
                        getChildElementBoolean(childElement, "mute", description, parseErrors).ifPresent(speakerChannelBuilder::setMuteRequested);
                        callFeatureBuilder = speakerChannelBuilder;
                        break;
                    case "voicerecorder":
                        final CallFeatureVoiceRecorder.Builder voiceRecorderBuilder = CallFeatureVoiceRecorder.Builder.start();
                        final VoiceRecorderInfo.Builder voiceRecorderInfoBuilder = VoiceRecorderInfo.Builder.start();

                        RecorderNumber.from(getNullableChildElementString(childElement, "recnumber"))
                                .ifPresent(voiceRecorderInfoBuilder::setRecorderNumber);
                        RecorderPort.from(getNullableChildElementString(childElement, "recport"))
                                .ifPresent(voiceRecorderInfoBuilder::setRecorderPort);
                        RecorderChannel.from(getNullableChildElementString(childElement, "recchan"))
                                .ifPresent(voiceRecorderInfoBuilder::setRecorderChannel);
                        RecorderType.from(getNullableChildElementString(childElement, "rectype"))
                                .ifPresent(voiceRecorderInfoBuilder::setRecorderType);
                        voiceRecorderBuilder.setVoiceRecorderInfo(voiceRecorderInfoBuilder.build(parseErrors));

                        callFeatureBuilder = voiceRecorderBuilder;
                        break;
                    default:

                        // Assume it's a simple true/false call feature
                        final CallFeatureBoolean.Builder booleanBuilder = CallFeatureBoolean.Builder.start();
                        getBoolean(featureElement.getText(), description, parseErrors).ifPresent(booleanBuilder::setEnabled);
                        callFeatureBuilder = booleanBuilder;
                        break;
                    }
                } else {
                    if (FeatureType.VOICE_MESSAGE.equals(featureType.orElse(null))) {
                        final CallFeatureTextValue.Builder textValueBuilder = CallFeatureTextValue.Builder.start();
                        textValueBuilder.setValue(featureElement.getText());
                        callFeatureBuilder = textValueBuilder;
                    } else {
                        // It's a simple true/false call feature
                        final CallFeatureBoolean.Builder booleanBuilder = CallFeatureBoolean.Builder.start();
                        getBoolean(featureElement.getText(), description, parseErrors).ifPresent(booleanBuilder::setEnabled);
                        callFeatureBuilder = booleanBuilder;
                    }
                }
                featureId.ifPresent(callFeatureBuilder::setId);
                label.ifPresent(callFeatureBuilder::setLabel);
                featureType.ifPresent(callFeatureBuilder::setType);
                callBuilder.addFeature(callFeatureBuilder.build(parseErrors));
            }
        }
    }

    @Nonnull
    private static Optional<Boolean> getBoolean(@Nullable final String value, String description, List<String> parseErrors) {
        if ("true".equalsIgnoreCase(value)) {
            return Optional.of(Boolean.TRUE);
        } else if ("false".equalsIgnoreCase(value)) {
            return Optional.of(Boolean.FALSE);
        }
        if (value != null) {
            parseErrors.add(String.format("Invalid %s: %s is neither true or false", description, value));
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private static void getParticipants(@Nonnull final Element callElement, @Nonnull final Call.Builder callBuilder, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final Element participantsElement = callElement.element("participants");
        if (participantsElement != null) {
            final List<Element> participantElements = participantsElement.elements("participant");
            for (final Element participantElement : participantElements) {
                final Participant.Builder participantBuilder = Participant.Builder.start();
                getStringAttribute(participantElement, "jid", false, description, parseErrors).ifPresent(participantBuilder::setJID);
                getStringAttribute(participantElement, ATTRIBUTE_NUMBER, false, description, parseErrors).flatMap(PhoneNumber::from).ifPresent(participantBuilder::setNumber);
                participantBuilder.addE164Numbers(getPhoneNumbers(participantElement, "e164Number"));
                getStringAttribute(participantElement, ATTRIBUTE_DESTINATION, false, description, parseErrors).flatMap(PhoneNumber::from).ifPresent(participantBuilder::setDestinationNumber);
                ParticipantType.from(getNullableStringAttribute(participantElement, "type")).ifPresent(participantBuilder::setType);
                ParticipantCategory.from(getNullableStringAttribute(participantElement, "category")).ifPresent(participantBuilder::setParticipantCategory);
                CallDirection.from(getNullableStringAttribute(participantElement, ATTRIBUTE_DIRECTION)).ifPresent(participantBuilder::setDirection);
                final Optional<Instant> participantTimestamp = getJavaUtilDateAttribute(participantElement, ATTRIBUTE_TIMESTAMP, description, parseErrors);
                participantTimestamp.ifPresent(participantBuilder::setStartTime);
                final Optional<Instant> participantStartTime = getISO8601Attribute(participantElement, ATTRIBUTE_START_TIME, description, parseErrors);
                participantStartTime.ifPresent(participantBuilder::setStartTime);
                if (participantStartTime.isPresent() && participantTimestamp.isPresent() && !participantStartTime.equals(participantTimestamp)) {
                    parseErrors.add("Invalid participant; the legacy timestamp field does not match the start time field");
                }
                final Optional<Long> participantDuration = getLongAttribute(participantElement, ATTRIBUTE_DURATION, description, parseErrors);
                participantDuration.ifPresent(millis -> participantBuilder.setDuration(Duration.ofMillis(millis)));
                callBuilder.addParticipant(participantBuilder.build(parseErrors));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void getActions(@Nonnull final Element callElement, @Nonnull final Call.Builder callBuilder, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final Element actionsElement = callElement.element("actions");
        if (actionsElement != null) {
            final List<Element> actionElements = actionsElement.elements();
            for (final Element actionElement : actionElements) {
                final String actionString = actionElement.getName();
                final Optional<RequestAction> action = RequestAction.from(actionString);
                if (action.isPresent()) {
                    callBuilder.addAction(action.get());
                } else {
                    parseErrors.add(String.format("Invalid %s: %s is not a valid action", description, actionString));
                }
            }
        }
    }

    @Nonnull
    public static Element addPubSubMetaData(@Nonnull final Element messageElement, @Nonnull final PubSubMessageBuilder<?, ?> builder) {
        final Element eventElement = messageElement.addElement("event", OpenlinkXmppNamespace.XMPP_PUBSUB_EVENT.uri());
        final Element itemsElement = eventElement.addElement("items");
        builder.getPubSubNodeId().ifPresent(nodeId -> itemsElement.addAttribute("node", nodeId.value()));
        final Element itemElement = itemsElement.addElement("item");
        builder.getItemId().ifPresent(id -> itemElement.addAttribute("id", id.value()));
        return itemElement;
    }

    public static void addDelay(@Nonnull final Element messageElement, @Nonnull final PubSubMessageBuilder<?, ?> builder) {
        builder.getDelay().ifPresent(stamp -> messageElement.addElement("delay", "urn:xmpp:delay").addAttribute("stamp", stamp.toString()));
    }

    @Nullable
    public static Element setPubSubMetaData(
            @Nonnull final Message message,
            @Nonnull PubSubMessageBuilder<?, JID> builder,
            @Nonnull final String description,
            @Nonnull final List<String> parseErrors) {
        builder.setId(message.getID());
        builder.setFrom(message.getFrom());
        builder.setTo(message.getTo());
        final Element itemsElement = message.getChildElement("event", "http://jabber.org/protocol/pubsub#event").element("items");
        final Element itemElement = itemsElement.element("item");
        final Element delayElement = message.getChildElement("delay", "urn:xmpp:delay");
        PubSubNodeId.from(itemsElement.attributeValue("node")).ifPresent(builder::setPubSubNodeId);
        ItemId.from(TinderPacketUtil.getNullableStringAttribute(itemElement, "id")).ifPresent(builder::setItemId);
        final Optional<String> stampOptional = TinderPacketUtil.getStringAttribute(delayElement, "stamp");
        if (stampOptional.isPresent()) {
            final String stamp = stampOptional.get();
            try {
                builder.setDelay(Instant.parse(stamp));
            } catch (final DateTimeParseException e) {
                parseErrors.add(String.format("Invalid %s; invalid timestamp '%s'; format should be compliant with XEP-0082", description, stamp));
            }
        }
        return itemElement;
    }
}
