package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.MakeCallRequestBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.PhoneNumber;

public class MakeCallRequest extends OpenlinkIQ {
	@Nullable private final Jid jid;
    @Nullable private final InterestId interestId;
    @Nullable private final PhoneNumber destination;
    @Nonnull private final List<FeatureId> featureIds;

    private MakeCallRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
    	super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.jid = builder.getJID().orElse(null);
        this.interestId = builder.getInterestId().orElse(null);
        this.destination = builder.getDestination().orElse(null);
        this.featureIds = Collections.unmodifiableList(builder.getFeatureIds());
    }

    @Nonnull
    public Optional<Jid> getJID() {
        return Optional.ofNullable(jid);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<PhoneNumber> getDestination() {
        return Optional.ofNullable(destination);
    }

    @Nonnull
    public List<FeatureId> getFeatureIds() {
        return featureIds;
    }
    
	@Nonnull
	static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

		moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN,
				OpenlinkXmppNamespace.TAG_JID);

		final Builder builder = Builder.start();
		if ("jid".equals(parser.getName())) {
			final String jidString = parser.nextText();
			final Optional<Jid> jidOptional = SmackPacketUtil.getSmackJid(jidString);
			jidOptional.ifPresent(builder::setJID);
			parser.nextTag();
		}
		
		if (OpenlinkXmppNamespace.TAG_INTEREST.equals(parser.getName())) {
			final String interestIdString;
			interestIdString = parser.nextText();
			final Optional<InterestId> interestIdOptional = InterestId.from(interestIdString);
			interestIdOptional.ifPresent(builder::setInterestId);
			parser.nextTag();
		}

		if ("destination".equals(parser.getName())) {
			final String destinationString;
			destinationString = parser.nextText();
			final Optional<PhoneNumber> destinationOptional = PhoneNumber.from(destinationString);
			destinationOptional.ifPresent(builder::setDestination);
			parser.nextTag();
		}

		if (OpenlinkXmppNamespace.TAG_FEATURES.equals(parser.getName())) {
			parser.nextTag();
			while (parser.getName().equals(OpenlinkXmppNamespace.TAG_FEATURE)) {
				parser.nextTag();
					if ("id".equals(parser.getName())) {
						final String featureIdString;
						featureIdString = parser.nextText();
						final Optional<FeatureId> featureId = FeatureId.from(featureIdString);
						featureId.ifPresent(builder::addFeatureId);
						ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
						parser.nextTag();
					} 

				ParserUtils.forwardToEndTagOfDepth(parser, parser.getDepth());
			}

		}
		return builder.build(new ArrayList<>());
	}

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_MAKE_CALL.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();
        xml.optElement("jid", jid);
        xml.optElement("interest", interestId);
        xml.optElement("destination", destination);

        if(!featureIds.isEmpty()) {
            xml.halfOpenElement(OpenlinkXmppNamespace.TAG_FEATURES).rightAngleBracket();

            for (final FeatureId featureId : featureIds)
            {
                xml.halfOpenElement(OpenlinkXmppNamespace.TAG_FEATURE).rightAngleBracket();
                xml.optElement("id", featureId);
                xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURE);
            }
            
            xml.closeElement(OpenlinkXmppNamespace.TAG_FEATURES);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends MakeCallRequestBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public MakeCallRequest build() {
            super.validate();
            return new MakeCallRequest(this, null);
        }

        @Nonnull
        private MakeCallRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new MakeCallRequest(this, errors);
        }

    }

}
