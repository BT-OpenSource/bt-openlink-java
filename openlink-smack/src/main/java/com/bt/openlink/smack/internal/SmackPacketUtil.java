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
	
    private static final String ATTRIBUTE_DIRECTION = "direction";
    private static final String ATTRIBUTE_START_TIME = "start";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    private static final String ATTRIBUTE_DURATION = "duration";
    private static final String ELEMENT_NUMBER = "number";
    private static final DateTimeFormatter JAVA_UTIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
        if ("true".equals(attributeValue)) {
            return Optional.of(Boolean.TRUE);
        } else if ("false".equals(attributeValue)) {
            return Optional.of(Boolean.FALSE);
        } else {
            return Optional.empty();
        }
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
    
    public static IQChildElementXmlStringBuilder addCalls(IQChildElementXmlStringBuilder xml,  @Nonnull final Collection<Call> calls) {
		xml.openElement("call");
		for (final Call call : calls) {
			xml.optElement("id", call.getId().orElse(null));
			xml.optElement("conference", call.getConferenceId().orElse(null));
			call.getSite().ifPresent(site -> {
				xml.halfOpenElement("site");
				site.getId().ifPresent(id -> xml.attribute("id", String.valueOf(id)));
				site.isDefault().ifPresent(
						isDefault -> xml.attribute(OpenlinkXmppNamespace.TAG_DEFAULT, String.valueOf(isDefault)));
				site.getType().ifPresent(type -> xml.attribute("type", type.name()));
				xml.rightAngleBracket();
				site.getName().ifPresent(xml::escape);
				xml.closeElement("site");

			});
			xml.optElement("profile", call.getProfileId().orElse(null));
			xml.optElement("user", call.getUserId().orElse(null));
			xml.optElement("interest", call.getInterestId().orElse(null));
			String changed=null;
			if(call.getChanged().isPresent())
				changed = call.getChanged().get().getId();
			xml.optElement("changed", changed);
			
			String state=null;
			if(call.getState().isPresent())
				state =  call.getState().get().getLabel();
			xml.optElement("state", state);
			
			String direction=null;
			if(call.getDirection().isPresent())
				direction = call.getDirection().get().getLabel();
			xml.optElement("direction", direction);

			xml.openElement("caller");
			xml.halfOpenElement("number");
			final String callerE164Numbers = String.join(",",
					call.getCallerE164Numbers().stream().map(PhoneNumber::value).collect(Collectors.toList()));
			xml.attribute("e164", callerE164Numbers);
			xml.rightAngleBracket();
			xml.escape(call.getCallerNumber().get().value());
			xml.closeElement("number");
			xml.optElement("name", call.getCallerName().get());
			xml.closeElement("caller");

			xml.openElement("called");
			xml.halfOpenElement("number");
			xml.attribute("destination", call.getCalledDestination().get().value());
			final String calledE164Numbers = String.join(",",
					call.getCalledE164Numbers().stream().map(PhoneNumber::value).collect(Collectors.toList()));
			xml.attribute("e164", calledE164Numbers);
			xml.rightAngleBracket();
			xml.escape(call.getCalledNumber().get().value());
			xml.closeElement("number");
			xml.optElement("name", call.getCalledName().get());
			xml.closeElement("called");

			final List<OriginatorReference> originatorReferences = call.getOriginatorReferences();
			if (!originatorReferences.isEmpty()) {
				xml.openElement("originator-ref");
				originatorReferences.forEach(originatorReference -> {
					xml.halfOpenElement("property").attribute("id", originatorReference.getKey()).rightAngleBracket();
					xml.optElement("value", originatorReference.getValue());
					xml.closeElement("property");
				});
				xml.closeElement("originator-ref");
			}

			xml.optElement("start", (ISO_8601_FORMATTER.format(call.getStartTime().get().atZone(ZoneOffset.UTC))));
			xml.optElement("duration", String.valueOf(call.getDuration().get().toMillis()));

			final Collection<RequestAction> actions = call.getActions();
			if (!actions.isEmpty()) {
				xml.openElement("actions");
				actions.forEach(action -> {xml.halfOpenElement(action.getId()).rightAngleBracket();
				xml.closeElement(action.getId());
				});
				xml.closeElement("actions");
			}

			final List<CallFeature> features = call.getFeatures();
			if (!features.isEmpty()) {
				xml.openElement("features");
				features.forEach(feature -> {
					xml.halfOpenElement("feature");
					xml.attribute("id", feature.getId().get().value());
					xml.attribute("label", feature.getLabel().get());
					xml.attribute("type", feature.getType().get().getId());
					xml.rightAngleBracket();
					feature.getDeviceKey().ifPresent(deviceKey -> {
						xml.halfOpenElement("devicekeys")
								.attribute("xmlns", "http://xmpp.org/protocol/openlink:01:00:00/features#device-keys")
								.rightAngleBracket();
						xml.optElement("key", deviceKey.value());
						xml.closeElement("devicekeys");
					});
					if(feature.isEnabled().isPresent())
					xml.escape(String.valueOf(feature.isEnabled().get()));
					xml.closeElement("feature");
				});
				xml.closeElement("features");
			}

			final List<Participant> participants = call.getParticipants();
			if (!participants.isEmpty()) {
				xml.openElement("participants");
				participants.forEach(participant -> {
					xml.halfOpenElement("participant");
					xml.attribute("jid", participant.getJID().get());
					xml.attribute("type", participant.getType().get().getId());
					xml.attribute("direction", participant.getDirection().get().getLabel());
					participant.getStartTime().ifPresent(startTime -> {
						final ZonedDateTime startTimeInUTC = startTime.atZone(TimeZone.getTimeZone("UTC").toZoneId());
						xml.attribute("start", ISO_8601_FORMATTER.format(startTimeInUTC));
						// Include the legacy timestamp attribute too
						xml.attribute("timestamp", JAVA_UTIL_DATE_FORMATTER.format(startTimeInUTC));
					});
					xml.attribute("duration", String.valueOf(participant.getDuration().get().toMillis()));
					xml.rightAngleBracket();
					xml.closeElement("participant");
				});
				xml.closeElement("participants");
			}

		}
		xml.closeElement("call");
		xml.closeElement("callstatus");
		xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
		xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
		return xml;
	}
    
	@SuppressWarnings("unchecked")
	public static List<Call> getCalls(final XmlPullParser parser, final List<String> errors)
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
			if (parser.getName().equals("caller")) {
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
					final Optional<PhoneNumber> destination = PhoneNumber.from(SmackPacketUtil.getStringAttribute(parser, "destination").get());
					destination.ifPresent(callBuilder::setCalledDestination);	
					final String calledNumberString = parser.nextText();
					final Optional<PhoneNumber> calledNumberOptional = PhoneNumber.from(calledNumberString);
					calledNumberOptional.ifPresent(callBuilder::setCalledNumber);				 
					parser.nextTag();
				}
				if (parser.getName().equals("name")) {
					final Optional<String> callerName = Optional.ofNullable(parser.nextText());
					callerName.ifPresent(callBuilder::setCalledName);
					parser.nextTag();  // moves to end of caller tag
					parser.nextTag();  // moves to start of next tag
				}
			}

			getOriginatorReferences(parser, callBuilder);
			getChildElementISO8601(ATTRIBUTE_START_TIME, parser, errors).ifPresent(callBuilder::setStartTime);
			getChildElementLong(ATTRIBUTE_DURATION, parser, errors).map(Duration::ofMillis)
					.ifPresent(callBuilder::setDuration);
			getActions(callBuilder, parser, errors);
			getFeatures(callBuilder, parser, errors);
			getParticipants(callBuilder, parser, errors);
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
	private static void getParticipants(@Nonnull final Call.Builder callBuilder, final XmlPullParser parser,
			@Nonnull final List<String> parseErrors) throws IOException, XmlPullParserException {

		parser.nextTag();
		if (parser.getName().equals("participants")) {
			parser.nextTag();
			while (parser.getName().equals("participant")) {
				final Participant.Builder participantBuilder = Participant.Builder.start();
				SmackPacketUtil.getStringAttribute(parser, "jid").ifPresent(participantBuilder::setJID);
				ParticipantType.from(SmackPacketUtil.getStringAttribute(parser, "type").orElse(null))
						.ifPresent(participantBuilder::setType);
				CallDirection.from(SmackPacketUtil.getStringAttribute(parser, ATTRIBUTE_DIRECTION).orElse(null))
						.ifPresent(participantBuilder::setDirection);
				final Optional<Instant> participantTimestamp = getJavaUtilDateAttribute(parser, ATTRIBUTE_TIMESTAMP,
						parseErrors);
				participantTimestamp.ifPresent(participantBuilder::setStartTime);
				final Optional<Instant> participantStartTime = getISO8601Attribute(parser, ATTRIBUTE_START_TIME,
						parseErrors);
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

	private static Optional<Instant> getJavaUtilDateAttribute(final XmlPullParser parser, final String attributeName,
			final List<String> parseErrors) {
		final Optional<String> stringValue = SmackPacketUtil.getStringAttribute(parser, attributeName);
		try {
			return stringValue.map(string -> Instant.from(JAVA_UTIL_DATE_FORMATTER.parse(string)));
		} catch (final DateTimeParseException ignored) {
			parseErrors.add(String.format(
					"Invalid %s; invalid %s '%s'; format should be 'dow mon dd hh:mm:ss zzz yyyy'", attributeName));
			return Optional.empty();
		}
	}

	private static Optional<Instant> getISO8601Attribute(final XmlPullParser parser, final String attributeName,
			@Nonnull final List<String> parseErrors) {

		final Optional<String> stringValue = SmackPacketUtil.getStringAttribute(parser, attributeName);
		try {
			return stringValue.map(Instant::parse);
		} catch (final DateTimeParseException ignored) {
			parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082",
					attributeName));
			return Optional.empty();
		}
	}

	@SuppressWarnings("unchecked")
	private static void getActions(@Nonnull final Call.Builder callBuilder, final XmlPullParser parser,
			@Nonnull final List<String> parseErrors) throws IOException, XmlPullParserException {
		parser.nextTag();
		if (parser.getName().equals("actions")) {
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
	private static void getFeatures(@Nonnull final Call.Builder callBuilder, final XmlPullParser parser,
			@Nonnull final List<String> parseErrors) throws IOException, XmlPullParserException {
		parser.nextTag();

		if (parser.getName().equals("features")) {

			parser.nextTag();
			while (OpenlinkXmppNamespace.TAG_FEATURE.equals(parser.getName())) {
				final CallFeature.Builder callFeatureBuilder = CallFeature.Builder.start();

				final Optional<FeatureId> featureId = FeatureId.from(parser.getAttributeValue("", "id"));
				featureId.ifPresent(callFeatureBuilder::setId);
				final Optional<String> featureTypeString = SmackPacketUtil.getStringAttribute(parser, "type");
				featureTypeString.ifPresent(featureType -> {
					final Optional<FeatureType> type = FeatureType.from(featureType);
					if (type.isPresent())
						callFeatureBuilder.setType(type.get());
					else
						parseErrors.add("Invalid %s; invalid feature type - '%s'");
				});
				final Optional<String> label = SmackPacketUtil.getStringAttribute(parser,
						OpenlinkXmppNamespace.TAG_LABEL);
				label.ifPresent(callFeatureBuilder::setLabel);

				parser.next();
				String featureText = parser.getText();
				parser.nextTag();
				if (parser.getName().equals("devicekeys")) {
					parser.nextTag();
					final String keyString;
					featureText=null;
					if (parser.getName().equals("key")) {
						keyString = parser.nextText().trim();
						if (!keyString.isEmpty()) {
							DeviceKey.from(keyString).ifPresent(callFeatureBuilder::setDeviceKey);
						}
						parser.nextTag(); // ends key tag
					}
					parser.nextTag(); // ends devicekey tag

				} else {
					getBoolean(featureText).ifPresent(callFeatureBuilder::setEnabled);
				}

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
		if (!parser.getName().equals("originator-ref")) {
			return;
		}
		parser.nextTag();

		while (parser.getName().equals("property")) {
			final String key = SmackPacketUtil.getStringAttribute(parser, "id").orElse("");
			parser.nextTag();
			String value = "";
			if (parser.getName().equals("value"))
				value = parser.nextText();
			callBuilder.addOriginatorReference(key, value);
			ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
			parser.nextTag();  // end of property tag
			parser.nextTag();  // start of next tag
		}

	}

	@Nonnull
	private static Optional<Instant> getChildElementISO8601(@Nonnull final String childElementName,
			final XmlPullParser parser, @Nonnull final List<String> parseErrors)
			throws XmlPullParserException, IOException {
		parser.nextTag();
		if (parser.getName().equals(childElementName)) {
			try {
				return Optional.of(Instant.parse(parser.nextText()));
			} catch (final DateTimeParseException ignored) {
				parseErrors.add(String.format("Invalid %s; invalid %s '%s'; format should be compliant with XEP-0082",
						childElementName));
			}
		}
		return Optional.empty();
	}

	@Nonnull
	public static Optional<Long> getChildElementLong(@Nonnull final String childElementName, final XmlPullParser parser,
			@Nonnull final List<String> parseErrors) throws XmlPullParserException, IOException {
		parser.nextTag();
		if (parser.getName().equals(childElementName)) {
			try {
				return Optional.of(Long.parseLong(parser.nextText()));
			} catch (final NumberFormatException ignored) {
				parseErrors
						.add(String.format("Invalid %s; invalid %s '%s'; please supply an integer", childElementName));
			}
		}
		return Optional.empty();
	}
}
