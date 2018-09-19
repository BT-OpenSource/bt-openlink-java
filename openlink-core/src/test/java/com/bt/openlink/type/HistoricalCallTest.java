package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class HistoricalCallTest {

    private final Instant startTime = Instant.now();
    private final Duration duration = Duration.ofMinutes(1);

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willCreateACall() {

        final HistoricalCall call = HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();

        assertThat(call.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(call.getUserId().get(), is(CoreFixtures.USER_ID));
        assertThat(call.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(call.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(call.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(call.getCallerNumber().get(), is(CoreFixtures.CALLER_NUMBER));
        assertThat(call.getCallerName().get(), is(CoreFixtures.CALLER_NAME));
        assertThat(call.getCalledNumber().get(), is(CoreFixtures.CALLED_NUMBER));
        assertThat(call.getCalledName().get(), is(CoreFixtures.CALLED_NAME));
        assertThat(call.getStartTime().get(), is(startTime));
        assertThat(call.getDuration().get(), is(duration));
        assertThat(call.getTsc().get(), is(CoreFixtures.TSC));
    }

    @Test
    public void willNotCreateACallWithoutAnId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call id has not been set");

        HistoricalCall.Builder.start()
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAUser() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The user id has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAnInterest() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call interest has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAState() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call state has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutADirection() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call direction has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutACallerNumber() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The caller number has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutACallerName() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The caller name has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutACalledNumber() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The called number has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutACalledName() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The called name has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setStartTime(startTime)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAStartTime() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call start time has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setDuration(duration)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutADuration() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call duration has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setTsc(CoreFixtures.TSC)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutATSC() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call tsc has not been set");

        HistoricalCall.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setStartTime(startTime)
                .setDuration(duration)
                .build();
    }

    @Test
    public void willCreateACallWithoutMandatoryFields() {
        final List<String> errors = new ArrayList<>();

        final HistoricalCall call = HistoricalCall.Builder.start()
                .build(errors);

        assertThat(call.getId(), is(Optional.empty()));
        assertThat(call.getUserId(), is(Optional.empty()));
        assertThat(call.getInterestId(), is(Optional.empty()));
        assertThat(call.getState(), is(Optional.empty()));
        assertThat(call.getDirection(), is(Optional.empty()));
        assertThat(call.getCallerNumber(), is(Optional.empty()));
        assertThat(call.getCallerName(), is(Optional.empty()));
        assertThat(call.getCalledNumber(), is(Optional.empty()));
        assertThat(call.getCalledName(), is(Optional.empty()));
        assertThat(call.getStartTime(), is(Optional.empty()));
        assertThat(call.getDuration(), is(Optional.empty()));
        assertThat(call.getTsc(), is(Optional.empty()));
        assertThat(errors, contains(
                "Invalid historical call; missing call id is mandatory",
                "Invalid historical call; missing user id is mandatory",
                "Invalid historical call; missing interest id is mandatory",
                "Invalid historical call; missing state is mandatory",
                "Invalid historical call; missing direction is mandatory",
                "Invalid historical call; missing caller number is mandatory",
                "Invalid historical call; missing caller name is mandatory",
                "Invalid historical call; missing called number is mandatory",
                "Invalid historical call; missing called name is mandatory",
                "Invalid historical call; missing start time is mandatory",
                "Invalid historical call; missing duration is mandatory",
                "Invalid historical call; missing TSC is mandatory"));
    }

}