package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;

@SuppressWarnings("ConstantConditions")
public class ParticipantTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAParticipation() {

        final Participant participant = Participant.Builder.start()
                .setJID(CoreFixtures.USER_FULL_JID_STRING)
                .setNumber(CoreFixtures.CALLED_NUMBER)
                .addE164Number(CoreFixtures.CALLED_E164_NUMBER)
                .setDestinationNumber(CoreFixtures.CALLED_DESTINATION)
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .build();

        assertThat(participant.getJID().get(), is(CoreFixtures.USER_FULL_JID_STRING));
        assertThat(participant.getNumber().get(), is(CoreFixtures.CALLED_NUMBER));
        assertThat(participant.getE164Numbers(), contains(CoreFixtures.CALLED_E164_NUMBER));
        assertThat(participant.getDestinationNumber().get(), is(CoreFixtures.CALLED_DESTINATION));
        assertThat(participant.getType().get(), is(ParticipantType.ACTIVE));
        assertThat(participant.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(participant.getStartTime().get(), is(CoreFixtures.START_TIME));
        assertThat(participant.getDuration().get(), is(CoreFixtures.DURATION));
    }

    @Test
    public void willBuildAParticipationWithAJIDButNoNumber() {
        Participant.Builder.start()
                .setType(ParticipantType.ACTIVE)
                .setJID(CoreFixtures.USER_FULL_JID_STRING)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .build();
    }

    @Test
    public void willBuildAParticipationWithANumberButNoJID() {
        Participant.Builder.start()
                .setType(ParticipantType.ACTIVE)
                .setNumber(CoreFixtures.CALLED_NUMBER)
                .addE164Numbers(Collections.singletonList(CoreFixtures.CALLED_E164_NUMBER))
                .setDirection(CallDirection.INCOMING)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutAJIDOrNumber() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Either the participation jid or number must be set");

        Participant.Builder.start()
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutAType() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation type has not been set");

        Participant.Builder.start()
                .setJID(CoreFixtures.USER_FULL_JID_STRING)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutADirection() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation direction has not been set");

        Participant.Builder.start()
                .setJID(CoreFixtures.USER_FULL_JID_STRING)
                .setType(ParticipantType.ACTIVE)
                .setStartTime(CoreFixtures.START_TIME)
                .setDuration(CoreFixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutAStartTime() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation start time has not been set");

        Participant.Builder.start()
                .setJID(CoreFixtures.USER_FULL_JID_STRING)
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setDuration(CoreFixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutADuration() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation duration has not been set");

        Participant.Builder.start()
                .setJID(CoreFixtures.USER_FULL_JID_STRING)
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(CoreFixtures.START_TIME)
                .build();
    }

    @Test
    public void willBuildAParticipantWithoutMandatoryValues() {

        final List<String> errors = new ArrayList<>();

        final Participant participant = Participant.Builder.start()
                .build(errors);

        assertThat(participant.getJID(), is(Optional.empty()));
        assertThat(participant.getType(), is(Optional.empty()));
        assertThat(participant.getDirection(), is(Optional.empty()));
        assertThat(participant.getStartTime(), is(Optional.empty()));
        assertThat(participant.getDuration(), is(Optional.empty()));
        assertThat(errors, containsInAnyOrder(
                "Invalid participant; either the participation jid or number must be set",
                "Invalid participant; missing participation type is mandatory",
                "Invalid participant; missing participation direction is mandatory",
                "Invalid participant; missing participation start time is mandatory",
                "Invalid participant; missing participation duration is mandatory"));
    }

}