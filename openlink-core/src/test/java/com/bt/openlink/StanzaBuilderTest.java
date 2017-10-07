package com.bt.openlink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("ConstantConditions")
public class StanzaBuilderTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private StanzaBuilder<StanzaBuilder, String> stanzaBuilder;

    @Before
    public void setUp() throws Exception {

        stanzaBuilder = new StanzaBuilder<StanzaBuilder, String>() {
        };
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        stanzaBuilder.setTo("to");
        stanzaBuilder.setFrom("from");
        stanzaBuilder.setStanzaId("id");

        stanzaBuilder.validate();
        assertThat(stanzaBuilder.getTo().get(), is("to"));
        assertThat(stanzaBuilder.getFrom().get(), is("from"));
        assertThat(stanzaBuilder.getStanzaId().get(), is("id"));
    }

    @Test
    public void willValidateThatToIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");

        stanzaBuilder.validate();
    }

    @Test
    public void willCheckThatToIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        stanzaBuilder.validate(errors);
        assertThat(errors, contains("Invalid stanza; missing 'to' attribute is mandatory"));
        assertThat(stanzaBuilder.getTo(), is(Optional.empty()));
        assertThat(stanzaBuilder.getFrom(), is(Optional.empty()));
        assertThat(stanzaBuilder.getStanzaId(), is(Optional.empty()));
    }
}