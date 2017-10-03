package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class PubSubNodeIdTest {

    @Test
    public void willConvertToAnInterestId() throws Exception {

        final String id = "test-node-id";
        final PubSubNodeId pubSubNodeId = PubSubNodeId.from(id).get();

        final InterestId interestId = pubSubNodeId.toInterestId();

        assertThat(interestId.value(), is(pubSubNodeId.value()));

    }
}