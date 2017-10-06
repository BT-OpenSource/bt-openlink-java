package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class ParticipantTypeTest {

    @Test
    public void willParseParticipationTypes() throws Exception {

        assertThat(ParticipantType.from("Active").get(), is(ParticipantType.ACTIVE));
        assertThat(ParticipantType.from(null), is(Optional.empty()));
    }
}