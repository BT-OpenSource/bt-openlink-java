package com.bt.openlink.type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class VoiceMessageTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willNotBuildAVoiceMessageWithoutAStatus() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The VoiceMessage status has not been set");

        VoiceMessage.Builder.start()
                .build();
    }

    @Test
    public void willNotBuildVoiceMessageQueryWithoutAMsgLength() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The VoiceMessage msgLength has not been set");

        VoiceMessage.Builder.start()
                .setAction(ManageVoiceMessageAction.QUERY)
                .setLabel("test-label")
                .setStatus(VoiceMessageStatus.OK)
                .build();
    }

    @Test
    public void willNotBuildVoiceMessageQueryWithoutACreationDate() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The VoiceMessage creationDate has not been set");

        VoiceMessage.Builder.start()
                .setAction(ManageVoiceMessageAction.QUERY)
                .setLabel("test-label")
                .setStatus(VoiceMessageStatus.OK)
                .setMsgLength(Duration.ZERO)
                .build();
    }

    @Test
    public void willNotBuildVoiceMessageQueryWithoutAnExtension() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The VoiceMessage extension has not been set");

        VoiceMessage.Builder.start()
                .setAction(ManageVoiceMessageAction.PLAYBACK)
                .setStatus(VoiceMessageStatus.OK)
                .build();
    }

    @Test
    public void willBuildAVoiceMessageWithoutMandatoryValues() {

        final List<String> errors = new ArrayList<>();

        final VoiceMessage voiceMessage = VoiceMessage.Builder.start()
                .build(errors);

        assertThat(voiceMessage.getLabel(), is(Optional.empty()));
        assertThat(errors, containsInAnyOrder(
                "Invalid VoiceMessage; missing status is mandatory",
                "Invalid VoiceMessage; missing action is mandatory"
                ));
    }

}