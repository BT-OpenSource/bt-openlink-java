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
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallFeature;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.Changed;
import com.bt.openlink.type.DeviceKey;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.FeatureType;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.PhoneNumber;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

/**
 * This class is for internal use by the library only; users of the API should not access this class directly.
 */
public final class TinderPacketUtil {

    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter JAVA_UTIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
    private static final String ATTRIBUTE_DIRECTION = "direction";
    private static final String ATTRIBUTE_START_TIME = "starttime";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    private static final String ATTRIBUTE_DURATION = "duration";
    private static final String ELEMENT_NUMBER = "number";

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

    @Nullable
    public static String getChildElementString(@Nullable final Element parentElement, @Nonnull final String childElementName) {
        final Element childElement = getChildElement(parentElement, childElementName);
        if (childElement != null) {
            final String childElementText = childElement.getText().trim();
            if (!childElementText.isEmpty()) {
                return childElementText;
            }
        }
        return null;
    }

    @Nullable
    public static LocalDate getChildElementLocalDate(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final DateTimeFormatter dateTimeFormatter,
            final String stanzaDescription,
            @Nonnull final String dateFormat,
            final List<String> parseErrors) {
        final String dateText = getChildElementString(parentElement, childElementName);
        if (dateText != null) {
            try {
                return LocalDate.parse(dateText, dateTimeFormatter);
            } catch (final DateTimeParseException e) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; date format is '%s'", stanzaDescription, childElementName, dateText, dateFormat));
            }
        }
        return null;
    }

    @Nonnull
    private static Optional<Instant> getChildElementISO8601(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getChildElementString(parentElement, childElementName);
        if (childElementText != null) {
            try {
                return Optional.of(Instant.parse(childElementText));
            } catch (final DateTimeParseException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", stanzaDescription, childElementName, childElementText));
            }
        }
        return Optional.empty();
    }

    @Nullable
    public static Long getChildElementLong(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getChildElementString(parentElement, childElementName);
        if (childElementText != null) {
            try {
                return Long.parseLong(childElementText);
            } catch (final NumberFormatException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; please supply an integer", stanzaDescription, childElementName, childElementText));
                return null;
            }
        }
        return null;
    }

    @Nonnull
    public static Optional<String> getStringAttribute(@Nullable final Element element, @Nonnull final String attributeName) {
        return getStringAttribute(element, attributeName, false, "", Collections.emptyList());
    }

    @Nonnull
    public static Optional<String> getStringAttribute(
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
        return Optional.ofNullable(attributeValue);
    }

    @Nonnull
    private static Optional<Long> getLongAttribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, false, description, parseErrors);
        try {
            return stringValue.map(Long::valueOf);
        } catch (final NumberFormatException e) {
            parseErrors.add(String.format("Invalid %s; Unable to parse number attribute %s: '%s'", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    private static Optional<Instant> getISO8601Attribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, false, description, parseErrors);
        try {
            if (stringValue.isPresent()) {
                return Optional.ofNullable(Instant.parse(stringValue.get()));
            }
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", description, attributeName, stringValue));
        }
        return Optional.empty();
    }

    private static Optional<Instant> getJavaUtilDateAttribute(final Element parentElement, final String attributeName, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, false, description, parseErrors);
        try {
            if (stringValue.isPresent()) {
                return Optional.of(Instant.from(JAVA_UTIL_DATE_FORMATTER.parse(stringValue.get())));
            }
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be 'dow mon dd hh:mm:ss zzz yyyy'", description, attributeName, stringValue));
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<Boolean> getBooleanAttribute(final Element element, final String id, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(element, id, false, description, parseErrors);
        return stringValue.map(Boolean::valueOf);
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

    public static void addItemCallStatusCalls(@Nonnull final Element parentElement, @Nullable final Boolean callStatusBusy, @Nonnull final Collection<Call> calls) {
        final Element callStatusElement = parentElement.addElement("callstatus", OpenlinkXmppNamespace.OPENLINK_CALL_STATUS.uri());
        if (callStatusBusy != null) {
            callStatusElement.addAttribute("busy", String.valueOf(callStatusBusy));
        }
        calls.forEach(call -> {
            final Element callElement = callStatusElement.addElement("call");
            call.getId().ifPresent(callId -> callElement.addElement("id").setText(callId.value()));
            call.getSite().ifPresent(site -> addSite(callElement, site));
            call.getProfileId().ifPresent(profileId -> callElement.addElement("profile").setText(profileId.value()));
            call.getInterestId().ifPresent(interestId -> callElement.addElement("interest").setText(interestId.value()));
            call.getChanged().ifPresent(changed -> callElement.addElement("changed").setText(changed.getId()));
            call.getState().ifPresent(state -> callElement.addElement("state").setText(state.getLabel()));
            call.getDirection().ifPresent(direction -> callElement.addElement(ATTRIBUTE_DIRECTION).setText(direction.getLabel()));
            final Element callerElement = callElement.addElement("caller");
            final Element callerNumberElement = callerElement.addElement(ELEMENT_NUMBER);
            call.getCallerNumber().ifPresent(callerNumber -> callerNumberElement.setText(callerNumber.value()));
            final String callerE164Numbers =String.join(",", call.getCallerE164Numbers().stream().map(PhoneNumber::value).collect(Collectors.toList()));
            if(!callerE164Numbers.isEmpty()) {
                callerNumberElement.addAttribute("e164", callerE164Numbers);
            }
            final Element callerNameElement = callerElement.addElement("name");
            call.getCallerName().ifPresent(callerNameElement::setText);
            final Element calledElement = callElement.addElement("called");
            final Element calledNumberElement = calledElement.addElement(ELEMENT_NUMBER);
            call.getCalledNumber().ifPresent(calledNumber -> calledNumberElement.setText(calledNumber.value()));
            call.getCalledDestination().ifPresent(calledDestination -> calledNumberElement.addAttribute("destination", calledDestination.value()));
            final String calledE164Numbers =String.join(",", call.getCalledE164Numbers().stream().map(PhoneNumber::value).collect(Collectors.toList()));
            if(!calledE164Numbers.isEmpty()) {
                calledNumberElement.addAttribute("e164", calledE164Numbers);
            }
            final Element calledNameElement = calledElement.addElement("name");
            call.getCalledName().ifPresent(calledNameElement::setText);
            call.getStartTime().ifPresent(startTime -> callElement.addElement(ATTRIBUTE_START_TIME).setText(ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC))));
            call.getDuration().ifPresent(duration -> callElement.addElement(ATTRIBUTE_DURATION).setText(String.valueOf(duration.toMillis())));
            addActions(call, callElement);
            addFeatures(call, callElement);
            addParticipants(call, callElement);
        });
    }

    private static void addFeatures(@Nonnull final Call call, @Nonnull final Element callElement) {
        final List<CallFeature> features = call.getFeatures();
        if (!features.isEmpty()) {
            final Element featuresElement = callElement.addElement("features");
            features.forEach(feature -> {
                final Element featureElement = featuresElement.addElement("feature");
                feature.getId().ifPresent(id -> featureElement.addAttribute("id", id.value()));
                feature.getLabel().ifPresent(label -> featureElement.addAttribute("label", label));
                feature.getType().ifPresent(type -> featureElement.addAttribute("type", type.getId()));
                feature.isEnabled().ifPresent(enabled -> featureElement.setText(String.valueOf(enabled)));
                feature.getDeviceKey().ifPresent(deviceKey -> {
                    final Element deviceKeysElement = featureElement.addElement("devicekeys", "http://xmpp.org/protocol/openlink:01:00:00/features#device-keys");
                    deviceKeysElement.addElement("key").setText(deviceKey.value());
                });
            });
        }
    }

    private static void addActions(@Nonnull final Call call, @Nonnull final Element callElement) {
        final Collection<RequestAction> actions = call.getActions();
        if (!actions.isEmpty()) {
            final Element actionsElement = callElement.addElement("actions");
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
                participant.getType().ifPresent(type -> participantElement.addAttribute("type", type.getId()));
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
        site.getType().ifPresent(type -> siteElement.addAttribute("type", type.name()));
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
        final Optional<Site.Type> type = Site.Type.from(getStringAttribute(siteElement, "type", false, description, parseErrors).orElse(null));
        type.ifPresent(siteBuilder::setType);
        return Optional.of(siteBuilder.build(parseErrors));
    }

    @SuppressWarnings("unchecked")
    public static List<Call> getCalls(@Nullable final Element parentElement, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final List<Call> calls = new ArrayList<>();
        final Element callStatusElement = getChildElement(parentElement, "callstatus");
        if(callStatusElement!=null) {
            final List<Element> callElements = callStatusElement.elements("call");
            for (final Element callElement : callElements) {
                final Call.Builder callBuilder = Call.Builder.start();
                final Optional<CallId> callId = CallId.from(getChildElementString(callElement, "id"));
                callId.ifPresent(callBuilder::setId);
                final Optional<Site> site = getSite(callElement, description, parseErrors);
                site.ifPresent(callBuilder::setSite);
                final Optional<ProfileId> profileId = ProfileId.from(getChildElementString(callElement, "profile"));
                profileId.ifPresent(callBuilder::setProfileId);
                final Optional<InterestId> interestId = InterestId.from(getChildElementString(callElement, "interest"));
                interestId.ifPresent(callBuilder::setInterestId);
                final Optional<Changed> changed = Changed.from(getChildElementString(callElement, "changed"));
                changed.ifPresent(callBuilder::setChanged);
                final Optional<CallState> state = CallState.from(getChildElementString(callElement, "state"));
                state.ifPresent(callBuilder::setState);
                final Optional<CallDirection> direction = CallDirection.from(getChildElementString(callElement, ATTRIBUTE_DIRECTION));
                direction.ifPresent(callBuilder::setDirection);
                final Element callerElement = getChildElement(callElement, "caller");
                final Optional<PhoneNumber> callerNumber = PhoneNumber.from(getChildElementString(callerElement, ELEMENT_NUMBER));
                callerNumber.ifPresent(callBuilder::setCallerNumber);
                final Optional<String> callerName = Optional.ofNullable(getChildElementString(callerElement, "name"));
                callerName.ifPresent(callBuilder::setCallerName);
                final Optional<String> callerE164 = getStringAttribute(getChildElement(callerElement, ELEMENT_NUMBER), "e164");
                callerE164.ifPresent(e164List -> {
                    final List<PhoneNumber> callerE164Numbers = Arrays.stream(e164List.split(","))
                            .map(String::trim)
                            .map(PhoneNumber::from)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    callBuilder.addCallerE164Numbers(callerE164Numbers);
                });
                final Element calledElement = getChildElement(callElement, "called");
                final Optional<PhoneNumber> calledNumber = PhoneNumber.from(getChildElementString(calledElement, ELEMENT_NUMBER));
                calledNumber.ifPresent(callBuilder::setCalledNumber);
                final Optional<String> calledName = Optional.ofNullable(getChildElementString(calledElement, "name"));
                calledName.ifPresent(callBuilder::setCalledName);
                final Element calledNumberElement = getChildElement(calledElement, ELEMENT_NUMBER);
                final Optional<PhoneNumber> calledDestination = PhoneNumber.from(getStringAttribute(calledNumberElement, "destination").orElse(null));
                calledDestination.ifPresent(callBuilder::setCalledDestination);
                final Optional<String> calledE164 = getStringAttribute(calledNumberElement, "e164");
                calledE164.ifPresent(e164List -> {
                    final List<PhoneNumber> calledE164Numbers = Arrays.stream(e164List.split(","))
                            .map(String::trim)
                            .map(PhoneNumber::from)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    callBuilder.addCalledE164Numbers(calledE164Numbers);
                });
                final Optional<Instant> startTime = getChildElementISO8601(callElement, ATTRIBUTE_START_TIME, description, parseErrors);
                startTime.ifPresent(callBuilder::setStartTime);
                final Optional<Long> duration = Optional.ofNullable(getChildElementLong(callElement, ATTRIBUTE_DURATION, description, parseErrors));
                duration.ifPresent(millis -> callBuilder.setDuration(Duration.ofMillis(millis)));
                getActions(callElement, callBuilder);
                getFeatures(callElement, callBuilder, parseErrors);
                getParticipants(callElement, callBuilder, description, parseErrors);
                calls.add(callBuilder.build(parseErrors));
            }
        }
        return calls;
    }

    @SuppressWarnings("unchecked")
    private static void getFeatures(@Nonnull final Element callElement, @Nonnull final Call.Builder callBuilder, List<String> parseErrors) {
        final Element featuresElement = callElement.element("features");
        if (featuresElement != null) {
            final List<Element> featureElements = featuresElement.elements("feature");
            for (final Element featureElement : featureElements) {
                final CallFeature.Builder callFeatureBuilder = CallFeature.Builder.start();
                FeatureId.from(featureElement.attributeValue("id")).ifPresent(callFeatureBuilder::setId);
                Optional.ofNullable(featureElement.attributeValue("label")).ifPresent(callFeatureBuilder::setLabel);
                FeatureType.from(featureElement.attributeValue("type")).ifPresent(callFeatureBuilder::setType);
                getBoolean(featureElement.getText()).ifPresent(callFeatureBuilder::setEnabled);
                final Element keyElement = getChildElement(featureElement, "devicekeys", "key");
                if (keyElement != null) {
                    DeviceKey.from(keyElement.getText()).ifPresent(callFeatureBuilder::setDeviceKey);
                }
                callBuilder.addFeature(callFeatureBuilder.build(parseErrors));
            }
        }
    }

    @Nonnull
    private static Optional<Boolean> getBoolean(@Nullable final String value) {
        if( "true".equalsIgnoreCase(value) ) {
            return Optional.of(Boolean.TRUE);
        } else if ( "false".equalsIgnoreCase(value) ) {
            return Optional.of(Boolean.FALSE);
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private static void getParticipants(@Nonnull final Element callElement, @Nonnull final Call.Builder callBuilder, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final Element participantsElement = callElement.element("participants");
        if (participantsElement != null) {
            final List<Element> participantElements = participantsElement.elements("participant");
            for (final Element participantElement : participantElements) {
                final Participant.Builder participantBuilder = Participant.Builder.start();
                final Optional<String> jid = getStringAttribute(participantElement, "jid", true, description, parseErrors);
                jid.ifPresent(participantBuilder::setJID);
                final Optional<ParticipantType> participantType = ParticipantType.from(getStringAttribute(participantElement, "type", true, description, parseErrors).orElse(null));
                participantType.ifPresent(participantBuilder::setType);
                final Optional<CallDirection> callDirection = CallDirection.from(getStringAttribute(participantElement, ATTRIBUTE_DIRECTION, true, description, parseErrors).orElse(null));
                callDirection.ifPresent(participantBuilder::setDirection);
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
    private static void getActions(@Nonnull final Element callElement, @Nonnull final Call.Builder callBuilder) {
        final Element actionsElement = callElement.element("actions");
        if (actionsElement != null) {
            final List<Element> actionElements = actionsElement.elements();
            for (final Element actionElement : actionElements) {
                final Optional<RequestAction> action = RequestAction.from(actionElement.getName());
                action.ifPresent(callBuilder::addAction);
            }
        }
    }

}
