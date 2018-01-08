package com.bt.openlink.smack.iq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetFeaturesRequestBuilder;
import com.bt.openlink.type.ProfileId;


public class GetFeaturesRequest extends OpenlinkIQ {
	 @Nullable private final ProfileId profileId;

	    private GetFeaturesRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
	    	super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
	        this.profileId = builder.getProfileId().orElse(null);
	    }

	    @Nonnull
	    public Optional<ProfileId> getProfileId() {
	        return Optional.ofNullable(profileId);
	    }
	    
	    @Nonnull
	    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

	        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN, "profile");
	        final String profileIdString;
	        if ("profile".equals(parser.getName())) {
	        	profileIdString = parser.nextText();
	        } else {
	        	profileIdString = null;
	        }
	        final Optional<ProfileId> profileIdOptional = ProfileId.from(profileIdString);
	        final Builder builder = Builder.start();
	        profileIdOptional.ifPresent(builder::setProfileId);
	        return builder.build(new ArrayList<>());
	    }
	    

	    @Override
	    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
	        xml.attribute("action", "execute")
	                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_FEATURES.uri())
	                .rightAngleBracket();
	        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
	                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
	                .attribute("type", "input")
	                .rightAngleBracket();
	        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();
	        xml.optElement("profile", profileId); //edited
	        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
	        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
	        return xml;
	    }

	    public static final class Builder extends GetFeaturesRequestBuilder<Builder, Jid, IQ.Type> {

	        @Nonnull
	        public static Builder start() {
	            return new Builder();
	        }

	        private Builder() {
	            super(IQ.Type.class);
	        }

	        @Nonnull
	        public GetFeaturesRequest build() {
	            super.validate();
	            return new GetFeaturesRequest(this, null);
	        }

	        @Nonnull
	        private GetFeaturesRequest build(@Nonnull final List<String> errors) {
	            super.validate(errors, false);
	            return new GetFeaturesRequest(this, errors);
	        }
	    }


}
