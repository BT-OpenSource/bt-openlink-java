package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class CallTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willCreateACall() throws Exception {

        final Call call = Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setSite(Fixtures.SITE)
                .setProfileId(Fixtures.PROFILE_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setDuration(1)
                .build();

        assertThat(call.getId().get(), is(Fixtures.CALL_ID));
        assertThat(call.getSite().get(), is(Fixtures.SITE));
        assertThat(call.getProfileId().get(), is(Fixtures.PROFILE_ID));
        assertThat(call.getInterestId().get(), is(Fixtures.INTEREST_ID));
        assertThat(call.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(call.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(call.isParticipating(), is(false));
    }

    @Test
    public void willNotCreateACallWithoutAnId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call id has not been set");

        Call.Builder.start()
                .setSite(Fixtures.SITE)
                .setProfileId(Fixtures.PROFILE_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setDuration(1)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutASite() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call site has not been set");

        Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setProfileId(Fixtures.PROFILE_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setDuration(1)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAProfileId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The profile id has not been set");

        Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setSite(Fixtures.SITE)
                .setInterestId(Fixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setDuration(1)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAnInterestId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest id has not been set");

        Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setSite(Fixtures.SITE)
                .setProfileId(Fixtures.PROFILE_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setDuration(1)
                .build();

    }

    @Test
    public void willNotCreateACallWithoutAState() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call state has not been set");

        Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setSite(Fixtures.SITE)
                .setProfileId(Fixtures.PROFILE_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .setDirection(CallDirection.INCOMING)
                .setDuration(1)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutADirection() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call direction has not been set");

        Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setSite(Fixtures.SITE)
                .setProfileId(Fixtures.PROFILE_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDuration(1)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutADuration() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call duration has not been set");

        Call.Builder.start()
                .setId(Fixtures.CALL_ID)
                .setSite(Fixtures.SITE)
                .setProfileId(Fixtures.PROFILE_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .build();
    }

    @Test
    public void willCreateACallWithoutMandatoryFields() throws Exception {
        final Call call = Call.Builder.start()
                .build(new ArrayList<>());

        assertThat(call.getId(), is(Optional.empty()));
        assertThat(call.getSite(), is(Optional.empty()));
        assertThat(call.getProfileId(), is(Optional.empty()));
        assertThat(call.getInterestId(), is(Optional.empty()));
        assertThat(call.getState(), is(Optional.empty()));
        assertThat(call.getDirection(), is(Optional.empty()));
        assertThat(call.getDuration(), is(Optional.empty()));
        assertThat(call.isParticipating(), is(false));
    }
}