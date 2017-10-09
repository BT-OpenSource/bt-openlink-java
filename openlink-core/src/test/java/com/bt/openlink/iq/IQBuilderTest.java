package com.bt.openlink.iq;

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

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class IQBuilderTest {

    private static class ConcreteIQBuilder extends IQBuilder<ConcreteIQBuilder, String, Fixtures.typeEnum> {
        protected ConcreteIQBuilder() {
            super(Fixtures.typeEnum.class);
        }

        @Nonnull
        @Override
        public String getExpectedIQType() {
            return Fixtures.typeEnum.set.name();
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private ConcreteIQBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new ConcreteIQBuilder();
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
        builder.setIQType(Fixtures.typeEnum.set);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getTo().get(), is("to"));
        assertThat(builder.getFrom().get(), is("from"));
        assertThat(builder.getId().get(), is("id"));
        assertThat(builder.getIqType().get(), is(Fixtures.typeEnum.set));
    }

    @Test
    public void willValidateThatToIsSet() throws Exception {
        builder.setFrom("from");
        builder.setId("id");
        builder.setIQType(Fixtures.typeEnum.set);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");

        builder.validate();
    }

    @Test
    public void willValidateThatFromIsSet() throws Exception {
        builder.setTo("to");
        builder.setId("id");
        builder.setIQType(Fixtures.typeEnum.set);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'from' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatIdAndFromAndTypeAreSet() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setIQType(Fixtures.typeEnum.MY_UNEXPECTED_TYPE);

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute"));
        assertThat(builder.getTo(), is(Optional.empty()));
        assertThat(builder.getFrom(), is(Optional.empty()));
        assertThat(builder.getId(), is(Optional.empty()));
        assertThat(builder.getIqType().get(), is(Fixtures.typeEnum.MY_UNEXPECTED_TYPE));
    }

}