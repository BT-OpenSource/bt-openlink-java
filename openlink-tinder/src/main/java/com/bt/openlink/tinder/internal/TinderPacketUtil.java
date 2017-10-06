package com.bt.openlink.tinder.internal;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.Participant;
import com.bt.openlink.type.ParticipantType;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

/**
 * This class is for internal use by the library only; users of the API should not access this class directly.
 */
public final class TinderPacketUtil {

    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
    public static String getChildElementString(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            final boolean isRequired,
            final String stanzaDescription,
            final List<String> parseErrors) {
        final Element childElement = getChildElement(parentElement, childElementName);
        if (childElement != null) {
            final String childElementText = childElement.getText().trim();
            if (!childElementText.isEmpty()) {
                return childElementText;
            }
        }
        if (isRequired) {
            parseErrors.add(String.format("Invalid %s; missing '%s' field is mandatory", stanzaDescription, childElementName));
        }
        return null;
    }

    @Nullable
    public static LocalDate getChildElementLocalDate(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final DateTimeFormatter dateTimeFormatter,
            final boolean isRequired,
            final String stanzaDescription,
            @Nonnull final String dateFormat,
            final List<String> parseErrors) {
        final String dateText = getChildElementString(parentElement, childElementName, isRequired, stanzaDescription, parseErrors);
        if (dateText != null) {
            try {
                return LocalDate.parse(dateText, dateTimeFormatter);
            } catch (final DateTimeParseException e) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; date format is '%s'", stanzaDescription, childElementName, dateText, dateFormat));
            }
        }
        return null;
    }

    @Nullable
    public static Integer getChildElementInteger(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            final boolean isRequired,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getChildElementString(parentElement, childElementName, isRequired, stanzaDescription, parseErrors);
        if (childElementText != null) {
            try {
                return Integer.parseInt(childElementText);
            } catch (final NumberFormatException ignored) {
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; please supply an integer", stanzaDescription, childElementName, childElementText));
                return null;
            }
        }
        return null;
    }

    @Nonnull
    private static Optional<Instant> getChildElementISO8601(
            @Nullable final Element parentElement,
            @Nonnull final String childElementName,
            final boolean isRequired,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getChildElementString(parentElement, childElementName, isRequired, stanzaDescription, parseErrors);
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
            final boolean isRequired,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getChildElementString(parentElement, childElementName, isRequired, stanzaDescription, parseErrors);
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
    public static Optional<Long> getLongAttribute(final Element parentElement, final String attributeName, final boolean isRequired, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, isRequired, description, parseErrors);
        try {
            return stringValue.map(Long::valueOf);
        } catch (final NumberFormatException e) {
            parseErrors.add(String.format("Invalid %s; Unable to parse number attribute %s: '%s'", description, attributeName, stringValue));
            return Optional.empty();
        }
    }

    private static Optional<Instant> getISO8601Attribute(final Element parentElement, final String attributeName, final boolean isRequired, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(parentElement, attributeName, isRequired, description, parseErrors);
        try {
            if (stringValue.isPresent()) {
                return Optional.ofNullable(Instant.parse(stringValue.get()));
            }
        } catch (final DateTimeParseException ignored) {
            parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082", description, attributeName, stringValue));
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<Boolean> getBooleanAttribute(final Element siteElement, final String id, final boolean isRequired, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(siteElement, id, isRequired, description, parseErrors);
        return stringValue.map(Boolean::valueOf);
    }

    @Nonnull
    public static Element addCommandElement(@Nonnull final IQ request) {
        return addChildElement(request, "command", OpenlinkXmppNamespace.XMPP_COMMANDS);
    }

    @Nonnull
    public static Element addChildElement(@Nonnull final IQ request, @Nonnull final String elementName, @Nonnull final OpenlinkXmppNamespace namespace) {
        return request.getElement().addElement(elementName, namespace.uri());
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

    public static void addItemCallStatusCalls(final Element parentElement, final Collection<Call> calls) {
        final Element itemElement = parentElement.addElement("item");
        final Element callStatusElement = itemElement.addElement("callstatus", OpenlinkXmppNamespace.OPENLINK_CALL_STATUS.uri());
        calls.forEach(call -> {
            final Element callElement = callStatusElement.addElement("call");
            call.getId().ifPresent(callId -> callElement.addElement("id").setText(callId.value()));
            call.getSite().ifPresent(site -> addSite(callElement, site));
            call.getProfileId().ifPresent(profileId -> callElement.addElement("profile").setText(profileId.value()));
            call.getInterestId().ifPresent(interestId -> callElement.addElement("interest").setText(interestId.value()));
            call.getState().ifPresent(state -> callElement.addElement("state").setText(state.getLabel()));
            call.getDirection().ifPresent(direction -> callElement.addElement("direction").setText(direction.getLabel()));
            call.getStartTime().ifPresent(startTime -> callElement.addElement("starttime").setText(ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC))));
            call.getDuration().ifPresent(duration -> callElement.addElement("duration").setText(String.valueOf(duration.toMillis())));
            final Collection<RequestAction> actions = call.getActions();
            if (!actions.isEmpty()) {
                final Element actionsElement = callElement.addElement("actions");
                actions.forEach(action -> actionsElement.addElement(action.getId()));
            }
            final List<Participant> participants = call.getParticipants();
            if (!participants.isEmpty()) {
                final Element participantsElement = callElement.addElement("participants");
                participants.forEach(participant -> {
                    final Element participantElement = participantsElement.addElement("participant");
                    participant.getJID().ifPresent(jid -> participantElement.addAttribute("jid", jid));
                    participant.getType().ifPresent(type -> participantElement.addAttribute("type", type.getId()));
                    participant.getDirection().ifPresent(direction -> participantElement.addAttribute("direction", direction.getLabel()));
                    participant.getStartTime().ifPresent(startTime -> participantElement.addAttribute("starttime", ISO_8601_FORMATTER.format(startTime.atZone(ZoneOffset.UTC))));
                    participant.getDuration().ifPresent(duration -> participantElement.addAttribute("duration", String.valueOf(duration.toMillis())));
                });
            }
        });
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
        final Optional<Long> id = getLongAttribute(siteElement, "id", true, description, parseErrors);
        id.ifPresent(siteBuilder::setId);
        final Optional<Boolean> isDefaultSite = getBooleanAttribute(siteElement, "default", false, description, parseErrors);
        isDefaultSite.ifPresent(siteBuilder::setDefault);
        final Optional<Site.Type> type = Site.Type.from(getStringAttribute(siteElement, "type", true, description, parseErrors).orElse(null));
        type.ifPresent(siteBuilder::setType);
        return Optional.of(siteBuilder.buildWithoutValidating());
    }

    @SuppressWarnings("unchecked")
    public static List<Call> getCalls(@Nonnull final Element parentElement, @Nonnull final String description, @Nonnull final List<String> parseErrors) {
        final List<Call> calls = new ArrayList<>();
        final Element itemElement = parentElement.element("item");
        final Element callStatusElement = itemElement.element("callstatus");
        final List<Element> callElements = callStatusElement.elements("call");
        for (final Element callElement : callElements) {
            final Call.Builder callBuilder = Call.Builder.start();
            final Optional<CallId> callId = CallId.from(getChildElementString(callElement, "id", true, description, parseErrors));
            callId.ifPresent(callBuilder::setId);
            final Optional<Site> site = getSite(callElement, description, parseErrors);
            site.ifPresent(callBuilder::setSite);
            final Optional<ProfileId> profileId = ProfileId.from(getChildElementString(callElement, "profile", true, description, parseErrors));
            profileId.ifPresent(callBuilder::setProfileId);
            final Optional<InterestId> interestId = InterestId.from(getChildElementString(callElement, "interest", true, description, parseErrors));
            interestId.ifPresent(callBuilder::setInterestId);
            final Optional<CallState> state = CallState.from(getChildElementString(callElement, "state", true, description, parseErrors));
            state.ifPresent(callBuilder::setState);
            final Optional<CallDirection> direction = CallDirection.from(getChildElementString(callElement, "direction", true, description, parseErrors));
            direction.ifPresent(callBuilder::setDirection);
            final Optional<Instant> startTime = getChildElementISO8601(callElement, "starttime", true, description, parseErrors);
            startTime.ifPresent(callBuilder::setStartTime);
            final Optional<Long> duration = Optional.ofNullable(getChildElementLong(callElement, "duration", true, description, parseErrors));
            duration.ifPresent(millis -> callBuilder.setDuration(Duration.ofMillis(millis)));
            final Element actionsElement = callElement.element("actions");
            if (actionsElement != null) {
                final List<Element> actionElements = actionsElement.elements();
                for (final Element actionElement : actionElements) {
                    final Optional<RequestAction> action = RequestAction.from(actionElement.getName());
                    action.ifPresent(callBuilder::addAction);
                }
            }
            final Element participantsElement = callElement.element("participants");
            if (participantsElement != null) {
                final List<Element> participantElements = participantsElement.elements("participant");
                for (final Element participantElement : participantElements) {
                    final Participant.Builder participantBuilder = Participant.Builder.start();
                    final Optional<String> jid = getStringAttribute(participantElement, "jid", true, description, parseErrors);
                    jid.ifPresent(participantBuilder::setJID);
                    final Optional<ParticipantType> participantType = ParticipantType.from(getStringAttribute(participantElement, "type", true, description, parseErrors).orElse(null));
                    participantType.ifPresent(participantBuilder::setType);
                    final Optional<CallDirection> callDirection = CallDirection.from(getStringAttribute(participantElement, "direction", true, description, parseErrors).orElse(null));
                    callDirection.ifPresent(participantBuilder::setDirection);
                    final Optional<Instant> participantStart = getISO8601Attribute(participantElement, "starttime", true, description, parseErrors);
                    participantStart.ifPresent(participantBuilder::setStartTime);
                    final Optional<Long> participantDuration = getLongAttribute(participantElement, "duration", true, description, parseErrors);
                    participantDuration.ifPresent(millis -> participantBuilder.setDuration(Duration.ofMillis(millis)));
                    callBuilder.addParticipant(participantBuilder.buildWithoutValidating());
                }

            }
            calls.add(callBuilder.buildWithoutValidating());
        }
        return calls;
    }

}
