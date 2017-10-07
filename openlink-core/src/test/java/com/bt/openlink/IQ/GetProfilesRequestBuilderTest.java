package com.bt.openlink.IQ;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("ConstantConditions")
public class GetProfilesRequestBuilderTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private GetProfilesRequestBuilder<GetProfilesRequestBuilder, String, String> builder;

    @Before
    public void setUp() throws Exception {

        builder = new GetProfilesRequestBuilder<GetProfilesRequestBuilder, String, String>() {
            @Nonnull
            @Override
            public String getExpectedIQType() {
                return "set";
            }
        };

        builder.setTo("to");
        builder.setFrom("from");
        builder.setStanzaId("id");
        builder.setIQType("set");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        builder.setJid("jid");

        builder.validate();

        assertThat(builder.getJid().get(), is("jid"));
    }

    @Test
    public void willValidateTheJidIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'jid' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatJidIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid get-profiles request stanza; missing or invalid 'jid'"));
        assertThat(builder.getJid(), is(Optional.empty()));
    }

    @Test
    public void willNotCheckThatIdIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setStanzaId(null);
        builder.setJid("jid");

        builder.validate(errors, false);

        assertThat(errors, is(empty()));
    }
}