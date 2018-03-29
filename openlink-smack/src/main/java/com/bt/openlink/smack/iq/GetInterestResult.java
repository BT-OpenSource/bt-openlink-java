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
import com.bt.openlink.iq.GetInterestResultBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;

public class GetInterestResult extends OpenlinkIQ {

    @Nullable private final Interest interest;

    private GetInterestResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.interest = builder.getInterest().orElse(null);
    }

    @Nonnull
    public Optional<Interest> getInterest() {
        return Optional.ofNullable(interest);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_OUT, OpenlinkXmppNamespace.TAG_INTERESTS, OpenlinkXmppNamespace.TAG_INTEREST);

        final Builder builder = Builder.start();

        final List<String> parseErrors = new ArrayList<>();
        if (parser.getName().equals(OpenlinkXmppNamespace.TAG_INTEREST)) {
            final Interest.Builder interestBuilder = Interest.Builder.start();
            final Optional<InterestId> interestId = InterestId.from(parser.getAttributeValue("", "id"));
            interestId.ifPresent(interestBuilder::setId);
            final Optional<InterestType> interestType = InterestType.from(parser.getAttributeValue("", "type"));
            interestType.ifPresent(interestBuilder::setType);
            final Optional<String> label = SmackPacketUtil.getStringAttribute(parser, OpenlinkXmppNamespace.TAG_LABEL);
            label.ifPresent(interestBuilder::setLabel);
            final Optional<Boolean> isDefaultInterest = SmackPacketUtil.getBooleanAttribute(parser, OpenlinkXmppNamespace.TAG_DEFAULT, "get-interest result", parseErrors);
            isDefaultInterest.ifPresent(interestBuilder::setDefault);
            final Optional<Integer> maxCalls = SmackPacketUtil.getIntegerAttribute(parser, "maxCalls");
            maxCalls.ifPresent(interestBuilder::setMaxCalls);
            parser.nextTag();
            SmackPacketUtil.getCallStatus(parser, "get-interest result", parseErrors).ifPresent(interestBuilder::setCallStatus);
            builder.setInterest(interestBuilder.build(parseErrors));
        }

        return builder.build(parseErrors);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("status", "completed").attribute("node", OpenlinkXmppNamespace.OPENLINK_GET_INTEREST.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri()).attribute("type", "output")
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_OUT).rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_INTERESTS)
                .attribute("xmlns", "http://xmpp.org/protocol/openlink:01:00:00/interests").rightAngleBracket();
        if (interest != null) {
            xml.halfOpenElement(OpenlinkXmppNamespace.TAG_INTEREST);
            interest.getId().ifPresent(interestId -> xml.attribute("id", interestId.value()));
            interest.getType().ifPresent(interestType -> xml.attribute("type", interestType.value()));
            interest.getLabel().ifPresent(label -> xml.attribute(OpenlinkXmppNamespace.TAG_LABEL, label));
            interest.isDefaultInterest().ifPresent(isDefault -> xml.attribute("default", String.valueOf(isDefault)));
            interest.getMaxCalls().ifPresent(maxCalls -> xml.attribute("maxCalls", String.valueOf(maxCalls)));
            xml.rightAngleBracket();
            interest.getCallStatus().ifPresent(callStatus -> SmackPacketUtil.addCallStatus(xml, callStatus));
            xml.closeElement(OpenlinkXmppNamespace.TAG_INTEREST);
        }
        xml.closeElement(OpenlinkXmppNamespace.TAG_INTERESTS);
        xml.closeElement(OpenlinkXmppNamespace.TAG_OUT);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    public static final class Builder extends GetInterestResultBuilder<Builder, Jid, IQ.Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public static Builder start(@Nonnull final GetInterestRequest request) {
            return start().setId(request.getStanzaId())
                    .setFrom(request.getTo())
                    .setTo(request.getFrom());
        }

        protected Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetInterestResult build() {
            validate();
            return new GetInterestResult(this, null);
        }

        @Nonnull
        private GetInterestResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetInterestResult(this, parseErrors);
        }
    }

}
