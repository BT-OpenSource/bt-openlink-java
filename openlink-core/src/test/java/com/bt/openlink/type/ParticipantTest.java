package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class ParticipantTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAParticipation() throws Exception {

        final Participant participant = Participant.Builder.start()
                .setJID("test-user")
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(Fixtures.START_TIME)
                .setDuration(Fixtures.DURATION)
                .build();

        assertThat(participant.getJID().get(), is("test-user"));
        assertThat(participant.getType().get(), is(ParticipantType.ACTIVE));
        assertThat(participant.getDirection().get(), is(CallDirection.INCOMING));
        assertThat(participant.getStartTime().get(), is(Fixtures.START_TIME));
        assertThat(participant.getDuration().get(), is(Fixtures.DURATION));
    }

    @Test
    public void cannotBuildAParticipationWithoutAJID() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation jid has not been set");

        Participant.Builder.start()
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(Fixtures.START_TIME)
                .setDuration(Fixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutAType() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation type has not been set");

        Participant.Builder.start()
                .setJID("test-user")
                .setDirection(CallDirection.INCOMING)
                .setStartTime(Fixtures.START_TIME)
                .setDuration(Fixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutADirection() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation direction has not been set");

        Participant.Builder.start()
                .setJID("test-user")
                .setType(ParticipantType.ACTIVE)
                .setStartTime(Fixtures.START_TIME)
                .setDuration(Fixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutAStartTime() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation start time has not been set");

        Participant.Builder.start()
                .setJID("test-user")
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setDuration(Fixtures.DURATION)
                .build();
    }

    @Test
    public void cannotBuildAParticipationWithoutADuration() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The participation duration has not been set");

        Participant.Builder.start()
                .setJID("test-user")
                .setType(ParticipantType.ACTIVE)
                .setDirection(CallDirection.INCOMING)
                .setStartTime(Fixtures.START_TIME)
                .build();
    }

    @Test
    public void willBuildAParticipantWithoutMandatoryValues() throws Exception {

        final List<String> errors = new ArrayList<>();

        final Participant participant = Participant.Builder.start()
                .build(errors);

        assertThat(participant.getJID(), is(Optional.empty()));
        assertThat(participant.getType(), is(Optional.empty()));
        assertThat(participant.getDirection(), is(Optional.empty()));
        assertThat(participant.getStartTime(), is(Optional.empty()));
        assertThat(participant.getDuration(), is(Optional.empty()));
        assertThat(errors, containsInAnyOrder(
                "Invalid participant; missing participation jid is mandatory",
                "Invalid participant; missing participation type is mandatory",
                "Invalid participant; missing participation direction is mandatory",
                "Invalid participant; missing participation start time is mandatory",
                "Invalid participant; missing participation duration is mandatory"
        ));
    }


}