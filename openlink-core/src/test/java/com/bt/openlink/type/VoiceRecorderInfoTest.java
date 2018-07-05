package com.bt.openlink.type;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class VoiceRecorderInfoTest {

    private static final RecorderNumber RECORDER_NUMBER = RecorderNumber.from("001").get();
    private static final RecorderChannel RECORDER_CHANNEL = RecorderChannel.from("2").get();
    private static final RecorderPort RECORDER_PORT = RecorderPort.from("8080").get();
    private static final RecorderType RECORDER_TYPE = RecorderType.from("V").get();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();


    @Test
    public void willBuildAVoiceRecorderInfo() {
        final VoiceRecorderInfo voiceRecorderInfo = VoiceRecorderInfo.Builder.start()
                .setRecorderNumber(RECORDER_NUMBER)
                .setRecorderChannel(RECORDER_CHANNEL)
                .setRecorderPort(RECORDER_PORT)
                .setRecorderType(RECORDER_TYPE)
                .build();

        assertThat(voiceRecorderInfo.getRecorderNumber(), is(java.util.Optional.of(RECORDER_NUMBER)));
        assertThat(voiceRecorderInfo.getRecorderChannel(), is(java.util.Optional.of(RECORDER_CHANNEL)));
        assertThat(voiceRecorderInfo.getRecorderPort(), is(java.util.Optional.of(RECORDER_PORT)));
        assertThat(voiceRecorderInfo.getRecorderType(), is(java.util.Optional.of(RECORDER_TYPE)));
    }

    @Test
    public void willNotBuildAndInvalidVoiceRecorderInfo() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The VoiceRecorder recorderNumber has not been set");

        VoiceRecorderInfo.Builder.start()
                .build();
    }

    @Test
    public void willReturnParseErrors() {
        final List<String> parseErrors = new ArrayList<>();

        final VoiceRecorderInfo voiceRecorderInfo = VoiceRecorderInfo.Builder.start()
                .build(parseErrors);

        assertThat(parseErrors, containsInAnyOrder(
                "Invalid VoiceRecorderInfo; missing recorderNumber is mandatory",
                "Invalid VoiceRecorderInfo; missing recorderPort is mandatory",
                "Invalid VoiceRecorderInfo; missing recorderChannel is mandatory",
                "Invalid VoiceRecorderInfo; missing recorderType is mandatory"
        ));

        assertThat(voiceRecorderInfo.getRecorderNumber(), is(java.util.Optional.empty()));
        assertThat(voiceRecorderInfo.getRecorderChannel(), is(java.util.Optional.empty()));
        assertThat(voiceRecorderInfo.getRecorderPort(), is(java.util.Optional.empty()));
        assertThat(voiceRecorderInfo.getRecorderType(), is(java.util.Optional.empty()));
    }
}