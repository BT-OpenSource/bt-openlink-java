package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ItemIdTest {

    @Test
    public void willGenerateRandomItemIds() {

        assertThat(ItemId.random().value(), is(not(ItemId.random().value())));

    }
}