package com.bt.openlink.IQ;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class IQBuilderTest {

    private IQBuilder<IQBuilder, String, String> iqBuilder;

    @Before
    public void setUp() throws Exception {

        iqBuilder = new IQBuilder<IQBuilder, String, String>() {
            @Nonnull
            @Override
            public String getExpectedIQType() {
                return "type";
            }
        };
        iqBuilder.setTo("to");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        iqBuilder.setFrom("from");
        iqBuilder.setStanzaId("id");
        iqBuilder.setIQType("type");

        iqBuilder.validate();

        assertThat(iqBuilder.getFrom().get(), is("from"));
        assertThat(iqBuilder.getStanzaId().get(), is("id"));
        assertThat(iqBuilder.getIqType().get(), is("type"));
    }

    @Test
    public void willCheckThatIdAndFromAndTypeAreSet() throws Exception {

        final List<String> errors = new ArrayList<>();
        iqBuilder.setIQType("not-type");

        iqBuilder.validate(errors);

        assertThat(errors, contains(
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute"));
        assertThat(iqBuilder.getFrom(), is(Optional.empty()));
        assertThat(iqBuilder.getStanzaId(), is(Optional.empty()));
        assertThat(iqBuilder.getIqType().get(), is("not-type"));
    }

}