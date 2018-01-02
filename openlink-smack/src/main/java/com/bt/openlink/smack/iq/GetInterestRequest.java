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
import com.bt.openlink.iq.GetInterestRequestBuilder;
import com.bt.openlink.type.InterestId;

public class GetInterestRequest extends OpenlinkIQ{
	
	@Nullable private final InterestId interestId;
	
	private GetInterestRequest(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.interestId = builder.getInterestId().orElse(null);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }
    
    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN, "interest");
        final String interestIdString;
        if ("interest".equals(parser.getName())) {
        	interestIdString = parser.nextText();
        } else {
        	interestIdString = null;
        }
        final Optional<InterestId> interestIdOptional = InterestId.from(interestIdString);
        final Builder builder = Builder.start();
        interestIdOptional.ifPresent(builder::setInterestId);
        return builder.build(new ArrayList<>());
    }
    

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_INTEREST.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IN).rightAngleBracket();
        xml.optElement("interest", interestId); 
        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends GetInterestRequestBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetInterestRequest build() {
            super.validate();
            return new GetInterestRequest(this, null);
        }

        @Nonnull
        private GetInterestRequest build(@Nonnull final List<String> parseErrors) {
            super.validate(parseErrors, false);
            return new GetInterestRequest(this, parseErrors);
        }
    }

}
