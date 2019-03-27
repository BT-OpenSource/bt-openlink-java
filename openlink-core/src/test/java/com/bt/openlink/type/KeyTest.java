package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class KeyTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final KeyId KEY_ID = KeyId.from("KeyId1").get();
    private static final KeyLabel KEY_LABEL = KeyLabel.from("KeyLabel 1").get();
    private static final KeyFunction KEY_FUNCTION = KeyFunction.from("14").get();
    private static final KeyColor KEY_COLOR = KeyColor.from("3").get();
    private static final KeyQualifier KEY_QUALIFIER = KeyQualifier.from("443").get();
    private static final KeyModifier KEY_MODIFIER = KeyModifier.from("0").get();
    private static final KeyInterest KEY_INTEREST = KeyInterest.from("L443").get();

    @Test
    public void shouldBuildKeyInstance() {
        final Key key = Key.Builder.start()
                .setId(KEY_ID)
                .setLabel(KEY_LABEL)
                .setFunction(KEY_FUNCTION)
                .setColor(KEY_COLOR)
                .setQualifier(KEY_QUALIFIER)
                .setModifier(KEY_MODIFIER)
                .setInterest(KEY_INTEREST)
                .build();
        assertThat(KEY_ID, is(key.getId().get()));
        assertThat(KEY_LABEL, is(key.getLabel().get()));
        assertThat(KEY_FUNCTION, is(key.getFunction().get()));
        assertThat(KEY_COLOR, is(key.getColor().get()));
        assertThat(KEY_QUALIFIER, is(key.getQualifier().get()));
        assertThat(KEY_MODIFIER, is(key.getModifier().get()));
        assertThat(KEY_INTEREST, is(key.getInterest().get()));
    }

    @Test
    public void shouldFailWithoutMandatoryValues() {
        final List<String> errors = new ArrayList<>();
        final Key key = Key.Builder.start().build(errors);

        assertThat(errors, contains(
                "Invalid key : missing key id is mandatory.",
                "Invalid key : missing key label is mandatory.",
                "Invalid key : missing key function is mandatory.",
                "Invalid key : missing key qualifier is mandatory.",
                "Invalid key : missing key modifier is mandatory.",
                "Invalid key : missing key color is mandatory.",
                "Invalid key : missing key interest is mandatory."));
        assertThat(key.getId(), is(Optional.empty()));
        assertThat(key.getLabel(), is(Optional.empty()));
        assertThat(key.getFunction(), is(Optional.empty()));
        assertThat(key.getColor(), is(Optional.empty()));
        assertThat(key.getQualifier(), is(Optional.empty()));
        assertThat(key.getModifier(), is(Optional.empty()));
        assertThat(key.getInterest(), is(Optional.empty()));
    }

    @Test
    public void shouldReturnErrorWithoutKeyId() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key id has not been set.");
        Key.Builder.start().build();
    }

    @Test
    public void shouldReturnErrorWithoutKeyLabel() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key label has not been set.");
        Key.Builder.start().setId(KEY_ID).build();
    }

    @Test
    public void shouldReturnErrorWithoutKeyFunction() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key function has not been set.");
        Key.Builder.start().setId(KEY_ID).setLabel(KEY_LABEL).build();
    }

    @Test
    public void shouldReturnErrorWithoutKeyQualifier() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key qualifier has not been set.");
        Key.Builder.start()
                .setId(KEY_ID)
                .setLabel(KEY_LABEL)
                .setFunction(KEY_FUNCTION)
                .build();
    }

    @Test
    public void shouldReturnErrorWithoutKeyModifier() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key modifier has not been set.");
        Key.Builder.start()
                .setId(KEY_ID)
                .setLabel(KEY_LABEL)
                .setFunction(KEY_FUNCTION)
                .setQualifier(KEY_QUALIFIER)
                .build();
    }

    @Test
    public void shouldReturnErrorWithoutKeyColor() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key color has not been set.");
        Key.Builder.start()
                .setId(KEY_ID)
                .setLabel(KEY_LABEL)
                .setFunction(KEY_FUNCTION)
                .setQualifier(KEY_QUALIFIER)
                .setModifier(KEY_MODIFIER)
                .build();
    }

    @Test
    public void shouldReturnErrorWithoutKeyInterest() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The key interest has not been set.");
        Key.Builder.start()
                .setId(KEY_ID)
                .setLabel(KEY_LABEL)
                .setFunction(KEY_FUNCTION)
                .setQualifier(KEY_QUALIFIER)
                .setModifier(KEY_MODIFIER)
                .setColor(KEY_COLOR)
                .build();
    }
}