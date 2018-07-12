package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
@RunWith(MockitoJUnitRunner.class)
public class CallStatusTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willCreateACallStatusType() {

        final CallStatus callStatus = CallStatus.Builder.start()
                .setCallStatusBusy(true)
                .addCall(CoreFixtures.CALL_OUTGOING_CONFERENCED)
                .build();

        assertThat(callStatus.isCallStatusBusy().get(), is(true));
        assertThat(callStatus.getCalls(), contains(CoreFixtures.CALL_OUTGOING_CONFERENCED));
    }

    @Test
    public void willCheckThatAtLeastOneCallIsPresent() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The callstatus has no calls");

        CallStatus.Builder.start()
                .build();
    }

    @Test
    public void willCheckThatDuplicateCallsAreNotPresent() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each call id must be unique - test-call-id appears more than once");

        CallStatus.Builder.start()
                .addCalls(Arrays.asList(CoreFixtures.CALL_OUTGOING_CONFERENCED, CoreFixtures.CALL_OUTGOING_CONFERENCED))
                .build();
    }

    @Test
    public void willValidateAtLeastOneCallIsPresent() {
        final List<String> errors = new ArrayList<>();

        final CallStatus callStatus = CallStatus.Builder.start()
                .build(errors);

        assertThat(callStatus.isCallStatusBusy(),is(Optional.empty()));
        assertThat(callStatus.getCalls().size(), is(0));
        assertThat(errors, contains("Invalid callstatus; missing or invalid calls"));
    }

    @Test
    public void willValidateDuplicateCallsAreNotPresent() {
        final List<String> errors = new ArrayList<>();

        final CallStatus callStatus = CallStatus.Builder.start()
                .setCallStatusBusy(false)
                .addCalls(Arrays.asList(CoreFixtures.CALL_OUTGOING_CONFERENCED, CoreFixtures.CALL_OUTGOING_CONFERENCED))
                .build(errors);

        assertThat(callStatus.isCallStatusBusy().get(),is(false));
        assertThat(callStatus.getCalls(), contains(CoreFixtures.CALL_OUTGOING_CONFERENCED, CoreFixtures.CALL_OUTGOING_CONFERENCED));
        assertThat(errors, contains("Invalid callstatus; each call id must be unique - test-call-id appears more than once"));
    }
}
