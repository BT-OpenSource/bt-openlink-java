package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ChangedTest {

    @Test
    public void newStateIsPreferredOverNull() throws Exception {
        assertThat(Changed.STATE.or(null), is(Changed.STATE));
    }

    @Test
    public void newStateIsPreferredOverLowPriorityState() throws Exception {
        assertThat(Changed.STATE.or(Changed.ACTIONS), is(Changed.STATE));
    }

    @Test
    public void oldStateIsPreferredOverHighPriorityState() throws Exception {
        assertThat(Changed.PARTICIPANT.or(Changed.STATE), is(Changed.STATE));
    }

}