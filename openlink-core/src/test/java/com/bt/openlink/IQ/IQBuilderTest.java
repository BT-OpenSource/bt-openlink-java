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
public class IQBuilderTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

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
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();
        iqBuilder.setTo("to");
        iqBuilder.setFrom("from");
        iqBuilder.setId("id");
        iqBuilder.setIQType("type");

        iqBuilder.validate();
        iqBuilder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(iqBuilder.getTo().get(), is("to"));
        assertThat(iqBuilder.getFrom().get(), is("from"));
        assertThat(iqBuilder.getId().get(), is("id"));
        assertThat(iqBuilder.getIqType().get(), is("type"));
    }

    @Test
    public void willValidateThatToIsSet() throws Exception {
        iqBuilder.setFrom("from");
        iqBuilder.setId("id");
        iqBuilder.setIQType("type");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");

        iqBuilder.validate();
    }

    @Test
    public void willValidateThatFromIsSet() throws Exception {
        iqBuilder.setTo("to");
        iqBuilder.setId("id");
        iqBuilder.setIQType("type");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'from' has not been set");

        iqBuilder.validate();
    }

    @Test
    public void willCheckThatIdAndFromAndTypeAreSet() throws Exception {

        final List<String> errors = new ArrayList<>();
        iqBuilder.setIQType("not-type");

        iqBuilder.validate(errors);

        assertThat(errors, contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute"));
        assertThat(iqBuilder.getTo(), is(Optional.empty()));
        assertThat(iqBuilder.getFrom(), is(Optional.empty()));
        assertThat(iqBuilder.getId(), is(Optional.empty()));
        assertThat(iqBuilder.getIqType().get(), is("not-type"));
    }

}