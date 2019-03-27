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
import com.bt.openlink.iq.GetProfileRequestBuilder;
import com.bt.openlink.type.ProfileId;

public class GetProfileRequest extends OpenlinkIQ{

    private static final String ELEMENT_PROFILE = "profile";
    @Nullable private final ProfileId profileId;

	private GetProfileRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.profileId = builder.getProfileId().orElse(null);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN, ELEMENT_PROFILE);
        final String profileIdString;
        if (ELEMENT_PROFILE.equals(parser.getName())) {
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
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_PROFILE.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();
        xml.optElement(ELEMENT_PROFILE, profileId);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends GetProfileRequestBuilder<Builder, Jid, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public GetProfileRequest build() {
            super.validate();
            return new GetProfileRequest(this, null);
        }

        @Nonnull
        private GetProfileRequest build(@Nonnull final List<String> parseErrors) {
            super.validate(parseErrors, false);
            return new GetProfileRequest(this, parseErrors);
        }
    }

}
