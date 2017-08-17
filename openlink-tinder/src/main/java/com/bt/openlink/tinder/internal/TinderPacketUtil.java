package com.bt.openlink.tinder.internal;

import com.bt.openlink.OpenlinkXmppNamespace;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This class is for internal use by the library only; users of the API should not access this class directly.
 */
public final class TinderPacketUtil {

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
    public static Optional<Long> getLongAttribute(final Element siteElement, final String id, final boolean isRequired, final String description, final List<String> parseErrors) {
        final Optional<String> stringValue = getStringAttribute(siteElement, id, isRequired, description, parseErrors);
        try {
            return stringValue.map(Long::valueOf);
        } catch (final NumberFormatException e) {
            return Optional.empty();
        }
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

}
