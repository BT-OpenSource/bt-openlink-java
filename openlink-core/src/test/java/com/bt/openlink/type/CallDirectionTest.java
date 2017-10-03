package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class CallDirectionTest {

    @Test
    public void willParseCallDirections() throws Exception {

        assertThat(CallDirection.from("Incoming").get(), is(CallDirection.INCOMING));
        assertThat(CallDirection.from(null), is(Optional.empty()));
    }

}