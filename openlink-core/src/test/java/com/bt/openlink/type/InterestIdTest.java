package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class InterestIdTest {

    @Test
    public void willConvertToAPubSubNodeId() throws Exception {

        final String id = "test-node-id";
        final InterestId interestId = InterestId.from(id).get();

        final PubSubNodeId pubSubNodeId = interestId.toPubSubNodeId();

        assertThat(pubSubNodeId.value(), is(interestId.value()));

    }

}
