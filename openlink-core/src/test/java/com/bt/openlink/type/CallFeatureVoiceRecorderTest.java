package com.bt.openlink.type;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class CallFeatureVoiceRecorderTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildACallFeatureVoiceRecorder() {
        final VoiceRecorderInfo voiceRecorderInfo = VoiceRecorderInfo.Builder.start()
                .setRecorderNumber(RecorderNumber.from("001").get())
                .setRecorderChannel(RecorderChannel.from("2").get())
                .setRecorderPort(RecorderPort.from("8080").get())
                .setRecorderType(RecorderType.from("V").get())
                .build();

        final CallFeatureVoiceRecorder callFeatureVoiceRecorder = CallFeatureVoiceRecorder.Builder.start()
                .setId(FeatureId.from("voicerecorder_1").get())
                .setType(FeatureType.VOICE_RECORDER)
                .setVoiceRecorderInfo(voiceRecorderInfo)
                .build();

        assertThat(callFeatureVoiceRecorder.getVoiceRecorderInfo(), is(Optional.of(voiceRecorderInfo)));
    }

    @Test
    public void willNotBuildAndInvalidVoiceRecorderInfo() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The feature id has not been set");

        CallFeatureVoiceRecorder.Builder.start()
                .build();
    }

    @Test
    public void willReturnParseErrors() {
        final List<String> parseErrors = new ArrayList<>();

        final CallFeatureVoiceRecorder callFeatureVoiceRecorder = CallFeatureVoiceRecorder.Builder.start()
                .build(parseErrors);

        assertThat(parseErrors, containsInAnyOrder(
                "Invalid feature; missing feature id is mandatory",
                "Invalid feature; the VoiceRecorder information must be set"
        ));

        assertThat(callFeatureVoiceRecorder.getVoiceRecorderInfo(), is(java.util.Optional.empty()));
    }
}