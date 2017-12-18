package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.RequestActionFixtures;
import com.bt.openlink.type.RequestAction;

@SuppressWarnings("ConstantConditions")
public class RequestActionRequestBuilderTest {
    private static class Builder extends RequestActionRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAZeroValueBuilder() {

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .validate();

        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(builder.getAction().get(), is(RequestAction.START_VOICE_DROP));
        assertThat(builder.getCallId().get(), is(CoreFixtures.CALL_ID));
        assertThat(builder.getValue1().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_1));
        assertThat(builder.getValue2().get(), is(RequestActionFixtures.REQUEST_ACTION_VALUE_2));
    }

    @Test
    public void requiresAnInterestId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action 'interestId' has not been set");

        builder.setAction(RequestAction.CLEAR_CALL)
                .setCallId(CoreFixtures.CALL_ID)
                .validate();
    }

    @Test
    public void requiresAnAction() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action 'action' has not been set");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setCallId(CoreFixtures.CALL_ID)
                .validate();
    }

    @Test
    public void requiresACallId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action 'callId' has not been set");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.CLEAR_CALL)
                .validate();
    }

    @Test
    public void cannotSetValue2WithoutValue1() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The request-action 'value2' has been set without setting 'value1'");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.CLEAR_CALL)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .validate();
    }

    @Test
    public void mustSetValue1() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The requestAction 'StartVoiceDrop' requires 'value1' to be set");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .validate();
    }

    @Test
    public void mustSetValue2() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The requestAction 'StartVoiceDrop' requires 'value2' to be set");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .validate();
    }

    @Test
    public void mustNotSetValue1() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The requestAction 'ClearConference' does not require 'value1' to be se");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.CLEAR_CONFERENCE)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setCallId(CoreFixtures.CALL_ID)
                .validate();
    }

    @Test
    public void mustNotSetValue2() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The requestAction 'AnswerCall' does not require 'value2' to be set");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.ANSWER_CALL)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .validate();
    }

    @Test
    public void willValidateMandatoryFieldsArePresent() {

        final ArrayList<String> errors = new ArrayList<>();

        builder.setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .validate(errors);

        assertThat(errors, containsInAnyOrder(
                "Invalid request-action stanza; missing 'interestId'",
                "Invalid request-action stanza; missing or invalid 'requestAction'",
                "Invalid request-action stanza; missing 'callId'",
                "Invalid request-action stanza; value2 cannot be set without value1"
                ));

    }

    @Test
    public void willValidateValue1() {

        final ArrayList<String> errors = new ArrayList<>();

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.INTERCOM_TRANSFER)
                .setCallId(CoreFixtures.CALL_ID)
                .validate(errors);

        assertThat(errors, containsInAnyOrder("Invalid request-action stanza; the action 'IntercomTransfer' requires value1 to be set"));
    }

    @Test
    public void willValidateValue2() {

        final ArrayList<String> errors = new ArrayList<>();

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.START_VOICE_DROP)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .validate(errors);

        assertThat(errors, containsInAnyOrder("Invalid request-action stanza; the action 'StartVoiceDrop' requires value2 to be set"));
    }

    @Test
    public void willValidateValue1IsNotSet() {

        final ArrayList<String> errors = new ArrayList<>();

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.CLEAR_CONFERENCE)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setCallId(CoreFixtures.CALL_ID)
                .validate(errors);

        assertThat(errors, containsInAnyOrder("Invalid request-action stanza; the action 'ClearConference' does not require value1 to be set"));
    }

    @Test
    public void willValidateValue2IsNotSet() {

        final ArrayList<String> errors = new ArrayList<>();

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setAction(RequestAction.ANSWER_CALL)
                .setCallId(CoreFixtures.CALL_ID)
                .setValue1(RequestActionFixtures.REQUEST_ACTION_VALUE_1)
                .setValue2(RequestActionFixtures.REQUEST_ACTION_VALUE_2)
                .validate(errors);

        assertThat(errors, containsInAnyOrder("Invalid request-action stanza; the action 'AnswerCall' does not require value2 to be set"));
    }

}