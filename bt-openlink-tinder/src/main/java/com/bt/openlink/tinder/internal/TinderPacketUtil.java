package com.bt.openlink.tinder.internal;

import com.bt.openlink.OpenlinkXmppNamespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * This class is for internal use by the library only; users of the API should not access this class directly.
 */
public final class TinderPacketUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    public static Element getIOInElement(@Nonnull final IQ iq) {
        return getChildElement(iq.getChildElement(), "iodata", "in");
    }

    @Nullable
    public static Element getIOOutElement(@Nonnull final IQ iq) {
        return getChildElement(iq.getChildElement(), "iodata", "out");
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

    @Nullable
    public static Element addElementWithTextIfNotNull(
            @Nonnull final Element elementToAddTo,
            @Nonnull final String elementName,
            @Nullable final Object elementValue) {
        if (elementValue != null) {
            final Element callerElement = elementToAddTo.addElement(elementName);
            callerElement.setText(elementValue.toString());
            return callerElement;
        } else {
            return null;
        }
    }

    @Nullable
    protected static Element addElementWithTextIfNotNull(
            @Nonnull final Element elementToAddTo,
            @Nonnull final String elementName,
            @Nullable final LocalDate date,
            @Nonnull final DateTimeFormatter dateTimeFormatter) {
        if (date != null) {
            final Element callerElement = elementToAddTo.addElement(elementName);
            callerElement.setText(dateTimeFormatter.format(date));
            return callerElement;
        } else {
            return null;
        }
    }

    protected static void addElementAttributeIfNotNull(
            @Nonnull final Element elementToAddTo,
            @Nonnull final String attributeName,
            @Nullable final String attributeValue) {
        if (attributeValue != null) {
            elementToAddTo.addAttribute(attributeName, attributeValue);
        }
    }

    @Nullable
    protected static Integer getChildElementInteger(
            @Nonnull final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final String stanzaDescription,
            @Nonnull final List<String> parseErrors) {
        final String childElementText = getChildElementString(parentElement, childElementName);
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

    @Nullable
    protected static LocalDate getChildElementLocalDate(
            @Nonnull final Element parentElement,
            @Nonnull final String childElementName,
            @Nonnull final DateTimeFormatter dateTimeFormatter,
            @Nonnull final String stanzaDescription,
            @Nonnull final String dateFormat,
            @Nonnull final List<String> parseErrors) {
        final String dateText = getChildElementString(parentElement, childElementName);
        if (dateText != null) {
            try {
                return LocalDate.parse(dateText, dateTimeFormatter);
            } catch (final DateTimeParseException e) {
                LOGGER.info("Supplied text is an invalid date [dateText={}]", dateText, e);
                parseErrors.add(String.format("Invalid %s; invalid %s '%s'; date format is '%s'", stanzaDescription, childElementName, dateText, dateFormat));
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static String getChildElementString(
            @Nonnull final Element parentElement,
            @Nonnull final String childElementName) {
        return getChildElementString(parentElement, childElementName, false, null, null);
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
    public static String getAttributeString(@Nullable final Element element, @Nonnull final String attributeName) {
        return getAttributeString(element, attributeName, false, null, null);
    }

    @Nonnull
    public static Optional<JID> toTinderJID(@Nullable final String textualJID) {
        try {
            return textualJID == null || textualJID.isEmpty() ? Optional.empty() : Optional.of(new JID(textualJID));
        } catch (final IllegalArgumentException e) {
            LOGGER.info("Supplied text is an invalid JID [textualJID={}]", textualJID, e);
            return Optional.empty();
        }
    }

    @Nullable
    public static String getAttributeString(
            @Nullable final Element element,
            @Nonnull final String attributeName,
            final boolean isRequired,
            final String stanzaDescription,
            final List<String> parseErrors) {
        final String attributeValue;
        if (element == null) {
            attributeValue = null;
        } else {
            attributeValue = element.attributeValue(attributeName);
        }
        if (attributeValue == null && isRequired) {
            parseErrors.add(String.format("Invalid %s; missing '%s' attribute is mandatory", stanzaDescription, attributeName));
        }
        return attributeValue;
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
        final Element ioInputElement = commandElement.addElement("iodata", OpenlinkXmppNamespace.XMPP_IO_DATA.uri());
        ioInputElement.addAttribute("type", "input");
        return ioInputElement.addElement("in");
    }

    @Nonnull
    public static Element addCommandIOOutputElement(@Nonnull final IQ result, @Nonnull final OpenlinkXmppNamespace namespace) {
        final Element commandElement = addCommandElement(result);
        commandElement.addAttribute("status", "completed");
        commandElement.addAttribute("node", namespace.uri());
        final Element ioInputElement = commandElement.addElement("iodata", OpenlinkXmppNamespace.XMPP_IO_DATA.uri());
        ioInputElement.addAttribute("type", "output");
        return ioInputElement.addElement("out");
    }

    @Nonnull
    public static Optional<JID> getTinderJID(String jidString) {
        return jidString == null || jidString.isEmpty() ? Optional.empty() : Optional.of(new JID(jidString));
    }

}
