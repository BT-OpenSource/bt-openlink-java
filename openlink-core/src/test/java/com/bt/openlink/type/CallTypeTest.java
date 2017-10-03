package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class CallTypeTest {

    @Test
    public void willParseCallTypes() throws Exception {

        assertThat(CallType.from("in").get(), is(CallType.INBOUND));
        assertThat(CallType.from(null), is(Optional.empty()));
    }

}