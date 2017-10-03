package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class CallStateTest {

    @Test
    public void willParseCallTypes() throws Exception {

        assertThat(CallState.from("CallBusy").get(), is(CallState.CALL_BUSY));
        assertThat(CallState.from(null), is(Optional.empty()));
    }


    @Test
    public void aUserIsParticipatingInAnOutboundCalLDelivered() throws Exception {
        assertThat(CallState.CALL_DELIVERED.isParticipating(CallDirection.OUTGOING), is(true));
    }

    @Test
    public void aUserIsNotParticipatingInAnInboundCalLDelivered() throws Exception {
        assertThat(CallState.CALL_DELIVERED.isParticipating(CallDirection.INCOMING), is(false));
    }
}