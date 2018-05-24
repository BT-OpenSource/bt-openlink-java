package com.bt.openlink.type;

import com.bt.openlink.ManageVoiceMessageFixtures;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class VoiceMessageFeatureTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willBuildAFeatureWithAnId() throws Exception {

        final VoiceMessageFeature feature = VoiceMessageFeature.Builder.start()
                .setId(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE)
                .build();

        assertThat(feature.getId().get(), is(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE));
    }

    @Test
    public void willNotBuildAFeatureWithoutAnId() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The feature id has not been set");

        VoiceMessageFeature.Builder.start()
                .build();
    }


}