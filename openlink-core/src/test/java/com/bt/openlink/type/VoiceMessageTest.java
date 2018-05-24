package com.bt.openlink.type;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class VoiceMessageTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willNotBuildAVoiceMessageWithoutALabel() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The VoiceMessage label has not been set");

        VoiceMessage.Builder.start()
                .build();
    }

    @Test
    public void willBuildAVoiceMessageWithoutMandatoryValues() throws Exception {

        final List<String> errors = new ArrayList<>();

        final VoiceMessage voiceMessage = VoiceMessage.Builder.start()
                .build(errors);

        assertThat(voiceMessage.getLabel(), is(Optional.empty()));
        assertThat(errors, containsInAnyOrder(
                "Invalid VoiceMessage; missing label is mandatory",
                "Invalid VoiceMessage; missing status is mandatory",
                "Invalid VoiceMessage; missing action is mandatory",
                "Invalid VoiceMessage; missing msgLength is mandatory",
                "Invalid VoiceMessage; missing creationDate is mandatory"
                ));
    }

}