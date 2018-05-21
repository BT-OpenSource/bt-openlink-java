package com.bt.openlink.smack.iq;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.RequestActionRequestBuilder;
import com.bt.openlink.smack.internal.SmackPacketUtil;
import com.bt.openlink.type.CallId;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.RequestActionValue;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ParserUtils;
import org.jxmpp.jid.Jid;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RequestActionRequest extends OpenlinkIQ {

    @Nullable private final InterestId interestId;
    @Nullable private final RequestAction action;
    @Nullable private final RequestActionValue value1;
    @Nullable private final RequestActionValue value2;
    @Nullable private final CallId callId;

    private RequestActionRequest(@Nonnull RequestActionRequest.Builder builder,
            @Nullable List<String> parseErrors) {
        super("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), builder, parseErrors);
        this.interestId = builder.getInterestId().orElse(null);
        this.callId = builder.getCallId().orElse(null);
        this.action = builder.getAction().orElse(null);
        this.value1 = builder.getValue1().orElse(null);
        this.value2 = builder.getValue2().orElse(null);
    }

    @Nonnull
    static IQ from(XmlPullParser parser) throws IOException, XmlPullParserException {
        final RequestActionRequest.Builder builder = RequestActionRequest.Builder.start();
        final ArrayList<String> parseErrors = new ArrayList<>();

        moveToStartOfTag(parser, OpenlinkXmppNamespace.TAG_IODATA, OpenlinkXmppNamespace.TAG_IN);

        final int inDepth = parser.getDepth();
        parser.nextTag();
        while (parser.getDepth() > inDepth) {
            switch (parser.getName()) {
                case OpenlinkXmppNamespace.TAG_INTEREST:
                    InterestId.from(parser.nextText()).ifPresent(builder::setInterestId);
                    break;
                case "action":
                    SmackPacketUtil.getElementTextString(parser).ifPresent(action -> builder.setAction(RequestAction.from(action).orElse(null)));
                    break;
                case "call":
                    SmackPacketUtil.getElementTextString(parser).ifPresent(callId -> builder.setCallId(CallId.from(callId).orElse(null)));
                    break;
                case "value1":
                    SmackPacketUtil.getElementTextString(parser).ifPresent(requestActionValue -> builder.setValue1(RequestActionValue.from(requestActionValue).orElse(null)));
                    break;
                case "value2":
                    SmackPacketUtil.getElementTextString(parser).ifPresent(requestActionValue -> builder.setValue2(RequestActionValue.from(requestActionValue).orElse(null)));
                    break;
            }

            ParserUtils.forwardToEndTagOfDepth(parser, inDepth + 1);
            parser.nextTag();
        }
        return builder.build(parseErrors);
    }

        @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(final IQChildElementXmlStringBuilder xml) {
        xml.attribute("action", "execute")
                .attribute("node", OpenlinkXmppNamespace.OPENLINK_REQUEST_ACTION.uri())
                .rightAngleBracket();
        xml.halfOpenElement(OpenlinkXmppNamespace.TAG_IODATA)
                .attribute("xmlns", OpenlinkXmppNamespace.XMPP_IO_DATA.uri())
                .attribute("type", "input")
                .rightAngleBracket();
        xml.openElement(OpenlinkXmppNamespace.TAG_IN);

            getInterestId().ifPresent(interestId -> xml.element("interest", interestId.value()));
            getAction().ifPresent(action -> xml.element("action", action.getId()));
        getCallId().ifPresent(callId -> xml.element("call", callId.value()));
        getValue1().ifPresent(requestActionValue -> xml.optElement("value1", requestActionValue.value()));
        getValue2().ifPresent(requestActionValue -> xml.optElement("value2", requestActionValue.value()));

        xml.closeElement(OpenlinkXmppNamespace.TAG_IN);
        xml.closeElement(OpenlinkXmppNamespace.TAG_IODATA);
        return xml;
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<RequestAction> getAction() {
        return Optional.ofNullable(action);
    }

    @Nonnull
    public Optional<RequestActionValue> getValue1() {
        return Optional.ofNullable(value1);
    }

    @Nonnull
    public Optional<RequestActionValue> getValue2() {
        return Optional.ofNullable(value2);
    }

    @Nonnull
    public Optional<CallId> getCallId() {
        return Optional.ofNullable(callId);
    }

    public static final class Builder extends RequestActionRequestBuilder<RequestActionRequest.Builder, Jid, Type> {

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public static RequestActionRequest.Builder start() {
            return new RequestActionRequest.Builder();
        }

        @Nonnull
        public RequestActionRequest build() {
            super.validate();
            return new RequestActionRequest(this, null);
        }

        @Nonnull
        private RequestActionRequest build(@Nonnull final List<String> errors) {
            super.validate(errors, false);
            return new RequestActionRequest(this, errors);
        }

    }
}
