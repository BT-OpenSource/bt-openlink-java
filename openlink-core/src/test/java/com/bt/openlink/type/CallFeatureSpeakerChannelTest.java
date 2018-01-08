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

@SuppressWarnings("ALL")
public class CallFeatureSpeakerChannelTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildASpeakerChannel() {

        final CallFeatureSpeakerChannel speakerChannel = CallFeatureSpeakerChannel.Builder.start()
                .setId(FeatureId.from("spkr_1").get())
                .setChannel(42)
                .setMicrophoneActive(true)
                .setMuteRequested(true)
                .build();

        assertThat(speakerChannel.getChannel().get(), is(42L));
        assertThat(speakerChannel.isMicrophoneActive().get(), is(Boolean.TRUE));
        assertThat(speakerChannel.isMuteRequested().get(), is(Boolean.TRUE));
    }

    @Test
    public void willNotBuildASpeakerChannelWithAChannel() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The speaker channel number has not been set");

        CallFeatureSpeakerChannel.Builder.start()
                .setId(FeatureId.from("spkr_1").get())
                .setMicrophoneActive(true)
                .setMuteRequested(true)
                .build();

    }

    @Test
    public void willValidateTheSpeakerChannelNumberHasBeenSet() {

        final List<String> errors = new ArrayList<>();

        final CallFeatureSpeakerChannel speakerChannel = CallFeatureSpeakerChannel.Builder.start()
                .setId(FeatureId.from("spkr-1").get())
                .build(errors);

        assertThat(speakerChannel.getId(), is(FeatureId.from("spkr-1")));
        assertThat(speakerChannel.getType().get(), is(FeatureType.SPEAKER_CHANNEL));
        assertThat(speakerChannel.getChannel(), is(Optional.empty()));
        assertThat(speakerChannel.isMicrophoneActive(), is(Optional.empty()));
        assertThat(speakerChannel.isMuteRequested(), is(Optional.empty()));

        assertThat(errors, containsInAnyOrder(
                "Invalid speaker channel feature; the speaker channel number has not been set"
        ));


    }
}