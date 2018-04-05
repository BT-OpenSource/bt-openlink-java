package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public class CallTest {

    private final Instant startTime = Instant.now();
    private final Duration duration = Duration.ofMinutes(1);

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willCreateACall() {

        final Call call = Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setTelephonyCallId(CoreFixtures.TELEPHONY_CALL_ID)
                .setConferenceId(CoreFixtures.CONFERENCE_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setUserId(CoreFixtures.USER_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setChanged(Changed.STATE)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setCallerNumber(CoreFixtures.CALLER_NUMBER)
                .setCallerName(CoreFixtures.CALLER_NAME)
                .addCallerE164Number(CoreFixtures.CALLER_E164_NUMBER)
                .setCalledNumber(CoreFixtures.CALLED_NUMBER)
                .setCalledName(CoreFixtures.CALLED_NAME)
                .setCalledDestination(CoreFixtures.CALLED_DESTINATION)
                .addCalledE164Number(CoreFixtures.CALLED_E164_NUMBER)
                .addOriginatorReference(CoreFixtures.ORIGINATOR_REFERENCE)
                .addOriginatorReference("key2", "value2")
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .addFeature(CoreFixtures.CALL_FEATURE)
                .addParticipant(CoreFixtures.PARTICIPANT)
                .build();

        assertThat(call.getId().get(), is(CoreFixtures.CALL_ID));
        assertThat(call.getTelephonyCallId().get(), is(CoreFixtures.TELEPHONY_CALL_ID));
        assertThat(call.getConferenceId().get(), is(CoreFixtures.CONFERENCE_ID));
        assertThat(call.getSite().get(), is(CoreFixtures.SITE));
        assertThat(call.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(call.getUserId().get(), is(CoreFixtures.USER_ID));
        assertThat(call.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(call.getChanged().get(), is(Changed.STATE));
        assertThat(call.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(call.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(call.getCallerNumber().get(), is(CoreFixtures.CALLER_NUMBER));
        assertThat(call.getCallerName().get(), is(CoreFixtures.CALLER_NAME));
        assertThat(call.getCallerE164Numbers(), is(Collections.singletonList(CoreFixtures.CALLER_E164_NUMBER)));
        assertThat(call.getCalledNumber().get(), is(CoreFixtures.CALLED_NUMBER));
        assertThat(call.getCalledName().get(), is(CoreFixtures.CALLED_NAME));
        assertThat(call.getCalledDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
        assertThat(call.getCalledE164Numbers(), is(Collections.singletonList(CoreFixtures.CALLED_E164_NUMBER)));
        assertThat(call.getOriginatorReferences(), contains(CoreFixtures.ORIGINATOR_REFERENCE, new OriginatorReference("key2", "value2")));
        assertThat(call.getStartTime().get(), is(startTime));
        assertThat(call.getDuration().get(), is(duration));
        assertThat(call.isParticipating().get(), is(false));
        assertThat(call.getActions(), contains(RequestAction.ANSWER_CALL));
        assertThat(call.getFeatures(), contains(CoreFixtures.CALL_FEATURE));
        assertThat(call.getParticipants(), contains(CoreFixtures.PARTICIPANT));
        assertThat(call.getActiveHandset(), is(Optional.empty()));
        assertThat(call.getActiveSpeakerChannel(), is(Optional.empty()));
        assertThat(call.isPrivate(), is(Optional.empty()));
        assertThat(call.isPublic(), is(Optional.empty()));
    }

    @Test
    public void willNotCreateACallWithoutAnId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call id has not been set");

        Call.Builder.start()
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutASite() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call site has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAProfileId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The profile id has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAnInterestId() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The interest id has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();

    }

    @Test
    public void willNotCreateACallWithoutAState() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call state has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutADirection() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call direction has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setStartTime(startTime)
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutAStartTime() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call start time has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .addCalledE164Numbers(Collections.singletonList(CoreFixtures.CALLED_E164_NUMBER))
                .addCallerE164Numbers(Collections.singletonList(CoreFixtures.CALLER_E164_NUMBER))
                .setDuration(duration)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willNotCreateACallWithoutADuration() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call duration has not been set");

        Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .addAction(RequestAction.ANSWER_CALL)
                .build();
    }

    @Test
    public void willCreateACallWithoutMandatoryFields() {
        final List<String> errors = new ArrayList<>();

        final Call call = Call.Builder.start()
                .build(errors);

        assertThat(call.getId(), is(Optional.empty()));
        assertThat(call.getTelephonyCallId(), is(Optional.empty()));
        assertThat(call.getConferenceId(), is(Optional.empty()));
        assertThat(call.getSite(), is(Optional.empty()));
        assertThat(call.getProfileId(), is(Optional.empty()));
        assertThat(call.getUserId(), is(Optional.empty()));
        assertThat(call.getInterestId(), is(Optional.empty()));
        assertThat(call.getChanged(), is(Optional.empty()));
        assertThat(call.getState(), is(Optional.empty()));
        assertThat(call.getDirection(), is(Optional.empty()));
        assertThat(call.getCallerNumber(), is(Optional.empty()));
        assertThat(call.getCallerName(), is(Optional.empty()));
        assertThat(call.getCallerE164Numbers(), is(empty()));
        assertThat(call.getCalledNumber(), is(Optional.empty()));
        assertThat(call.getCalledName(), is(Optional.empty()));
        assertThat(call.getCalledDestination(), is(Optional.empty()));
        assertThat(call.getCalledE164Numbers(), is(empty()));
        assertThat(call.getOriginatorReferences(), is(empty()));
        assertThat(call.getStartTime(), is(Optional.empty()));
        assertThat(call.getDuration(), is(Optional.empty()));
        assertThat(call.isParticipating(), is(Optional.empty()));
        assertThat(call.getActions(), is(empty()));
        assertThat(call.getFeatures(), is(empty()));
        assertThat(call.getParticipants(), is(empty()));
        assertThat(errors, contains(
                "Invalid call status; missing call id is mandatory",
                "Invalid call status; missing call site is mandatory",
                "Invalid call status; missing profile id is mandatory",
                "Invalid call status; missing interest id is mandatory",
                "Invalid call status; missing call state is mandatory",
                "Invalid call status; missing call direction is mandatory",
                "Invalid call status; missing call start time is mandatory",
                "Invalid call status; missing call duration is mandatory"
                ));
    }

    @Test
    public void aCallHasAnActiveHandset() {
        final Optional<FeatureId> activeHS = FeatureId.from("HS1");
        final Call call = Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setChanged(Changed.STATE)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(Duration.ZERO)
                .addFeature(CallFeatureBoolean.Builder.start()
                        .setType(FeatureType.HANDSET)
                        .setEnabled(true)
                        .setLabel("Handset 1")
                        .setId(activeHS.get())
                        .build())
                .build();

        assertThat(call.getActiveHandset(), is(activeHS));

    }

    @Test
    public void aCallHasAnActiveHeadset() {
        final Optional<FeatureId> activeHD = FeatureId.from("HD1");
        final Call call = Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setChanged(Changed.STATE)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(Duration.ZERO)
                .addFeature(CallFeatureBoolean.Builder.start()
                        .setType(FeatureType.HEADSET)
                        .setEnabled(true)
                        .setLabel("Headset 1")
                        .setId(activeHD.get())
                        .build())
                .build();

        assertThat(call.getActiveHeadset(), is(activeHD));
    }

    @Test
    public void aCallHasAnActiveSpeakerChannel() {
        final Call call = Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setChanged(Changed.STATE)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(Duration.ZERO)
                .addFeature(CoreFixtures.SPEAKER_FEATURE)
                .build();

        assertThat(call.getActiveSpeakerChannel().get(), is(CoreFixtures.SPEAKER_CHANNEL_NUMBER));
    }

    @Test
    public void aCallIsPrivate() {
        final Call call = Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setChanged(Changed.STATE)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(Duration.ZERO)
                .addFeature(CallFeatureBoolean.Builder.start()
                        .setType(FeatureType.PRIVACY)
                        .setEnabled(true)
                        .setId(FeatureId.from("Privacy").get())
                        .setLabel("Privacy")
                        .build())
                .build();

        assertThat(call.isPrivate().get(), is(true));
        assertThat(call.isPublic().get(), is(false));
    }

    @Test
    public void aCallIsPublic() {
        final Call call = Call.Builder.start()
                .setId(CoreFixtures.CALL_ID)
                .setSite(CoreFixtures.SITE)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setChanged(Changed.STATE)
                .setState(CallState.CALL_ORIGINATED)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(startTime)
                .setDuration(Duration.ZERO)
                .addFeature(CallFeatureBoolean.Builder.start()
                        .setType(FeatureType.PRIVACY)
                        .setEnabled(false)
                        .setId(FeatureId.from("Privacy").get())
                        .setLabel("Privacy")
                        .build())
                .build();

        assertThat(call.isPrivate().get(), is(false));
        assertThat(call.isPublic().get(), is(true));
    }
}