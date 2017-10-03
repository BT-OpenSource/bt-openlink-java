package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class RequestActionTest {

    @Test
    public void willParseRequestActions() throws Exception {

        assertThat(RequestAction.from("AnswerCall").get(), is(RequestAction.ANSWER_CALL));
        assertThat(RequestAction.from(null), is(Optional.empty()));
    }

}