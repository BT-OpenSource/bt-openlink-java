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

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class IQBuilderTest {

    private static class Builder extends IQBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }

        @Nonnull
        @Override
        public String getExpectedIQType() {
            return CoreFixtures.typeEnum.set.name();
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() {
        builder = new Builder();
    }

    @Test
    public void willValidateAPopulatedBuilder() {

        final List<String> errors = new ArrayList<>();
        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
        builder.setIQType(CoreFixtures.typeEnum.set);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getTo().get(), is("to"));
        assertThat(builder.getFrom().get(), is("from"));
        assertThat(builder.getId().get(), is("id"));
        assertThat(builder.getIqType().get(), is(CoreFixtures.typeEnum.set));
    }

    @Test
    public void willValidateThatToIsSet() {
        builder.setFrom("from");
        builder.setId("id");
        builder.setIQType(CoreFixtures.typeEnum.set);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");

        builder.validate();
    }

    @Test
    public void willValidateThatFromIsSet() {
        builder.setTo("to");
        builder.setId("id");
        builder.setIQType(CoreFixtures.typeEnum.set);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'from' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatIdAndFromAndTypeAreSet() {

        final List<String> errors = new ArrayList<>();
        builder.setIQType(CoreFixtures.typeEnum.MY_UNEXPECTED_TYPE);

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; incorrect 'type' attribute: MY_UNEXPECTED_TYPE"));
        assertThat(builder.getTo(), is(Optional.empty()));
        assertThat(builder.getFrom(), is(Optional.empty()));
        assertThat(builder.getId(), is(Optional.empty()));
        assertThat(builder.getIqType().get(), is(CoreFixtures.typeEnum.MY_UNEXPECTED_TYPE));
    }

}