package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.MakeCallResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Call;

public class MakeCallResult extends OpenlinkIQ {
    @Nullable private final Boolean callStatusBusy;
    @Nonnull private final List<Call> calls;

    private MakeCallResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.calls = Collections.unmodifiableList(builder.getCalls());
        this.callStatusBusy = builder.isCallStatusBusy().orElse(null);
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_MAKE_CALL);
        TinderPacketUtil.addItemCallStatusCalls(outElement, callStatusBusy, calls);
    }

    @Nonnull
    public List<Call> getCalls() {
        return calls;
    }

    @Nonnull
    public Optional<Boolean> isCallStatusBusy() {
        return Optional.ofNullable(callStatusBusy);
    }

    @Nonnull
    public static MakeCallResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Builder builder = Builder.start(iq);
        if (outElement != null) {
            final List<Call> calls = TinderPacketUtil.getCalls(outElement, "make call result", parseErrors);
            builder.addCalls(calls);
        }
        final MakeCallResult result = builder.build(parseErrors);
        result.setID(iq.getID());
        return result;
    }

    public static final class Builder extends MakeCallResultBuilder<Builder, JID, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            final Builder builder = start();
            TinderIQBuilder.setIQBuilder(builder, iq);
            return builder;
        }

        private Builder() {
            super(Type.class);
        }

        @Nonnull
        public MakeCallResult build() {
            super.validate();
            return new MakeCallResult(this, null);
        }

        @Nonnull
        public MakeCallResult build(@Nonnull final List<String> errors) {
            super.validate(errors, true);
            return new MakeCallResult(this, errors);
        }

    }

}
