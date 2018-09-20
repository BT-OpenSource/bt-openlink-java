package com.bt.openlink.iq;

import static com.bt.openlink.ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE;
import static com.bt.openlink.ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.ManageVoiceMessageFixtures;
import com.bt.openlink.type.FeatureId;
import com.bt.openlink.type.ManageVoiceMessageAction;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ManageVoiceMessageRequestBuilderTest {

    private static class Builder extends ManageVoiceMessageRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() {
        this.builder = builderWithBasicIQFieldsSet();
    }

    @Test
    public void willValidateACorrectManageVoiceMessageRequest() {

        builder
                .setAction(ManageVoiceMessageAction.CREATE)
                .setProfile(CoreFixtures.PROFILE_ID)
                .addFeature(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE)
                .setLabel(VOICE_MESSAGE_LABEL)
                .validate();

        assertThat(builder.getAction().get(), is(ManageVoiceMessageAction.CREATE));
        assertThat(builder.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(builder.getLabel().get(), is(VOICE_MESSAGE_LABEL));
        assertThat(builder.getFeatures(), contains(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE));
    }

    @Test
    public void willValidateTheProfileIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The manage-voice-message request 'profileId' has not been set");

        builder.
                setAction(ManageVoiceMessageAction.CREATE)
                .validate();
    }

    @Test
    public void willValidateTheActionIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Invalid manage-voice-message stanza; missing or invalid 'action'");

        builder.setProfile(CoreFixtures.PROFILE_ID)
                .validate();
    }

    @Test
    public void willCheckThatTheProfileIsSet() {

        final List<String> errors = new ArrayList<>();

        builder.setAction(ManageVoiceMessageAction.CREATE)
                .addFeature(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE)
                .setLabel(ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL)
                .validate(errors);

        assertThat(errors, contains("Invalid manage-voice-message request stanza; missing 'profile'"));
        assertThat(builder.getProfileId(), is(Optional.empty()));
    }

    @Test
    public void willCheckThatTheActionIsSet() {

        final List<String> errors = new ArrayList<>();

        builder.setProfile(CoreFixtures.PROFILE_ID);
        builder.addFeature(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE);
        builder.validate(errors);

        assertThat(errors, contains("Invalid manage-voice-message request stanza; missing 'action'"));
        assertThat(builder.getAction(), is(Optional.empty()));
    }

    @Test
    public void withCheckThatAtLeastOneFeatureIsSetForGivenActions() {
        final ManageVoiceMessageAction[] actions = {
                ManageVoiceMessageAction.ARCHIVE,
                ManageVoiceMessageAction.CREATE,
                ManageVoiceMessageAction.DELETE,
                ManageVoiceMessageAction.EDIT,
                ManageVoiceMessageAction.PLAYBACK,
                ManageVoiceMessageAction.QUERY,
        };

        for (ManageVoiceMessageAction action : actions) {
            final List<String> errors = new ArrayList<>();

            final Builder builder = builderWithBasicIQFieldsSet();
            builder.setProfile(CoreFixtures.PROFILE_ID);
            builder.setAction(action);
            builder.setLabel(ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL);
            builder.validate(errors);

            assertThat(errors, contains(String.format("A single feature must be supplied with the '%s' action", action)));
        }
    }

    @Test
    public void withCheckThatALabelSetForGivenActions() {
        final ManageVoiceMessageAction[] actions = {
                ManageVoiceMessageAction.CREATE,
                ManageVoiceMessageAction.RECORD,
                ManageVoiceMessageAction.EDIT,
                ManageVoiceMessageAction.SEARCH
        };

        for (ManageVoiceMessageAction action : actions) {
            final List<String> errors = new ArrayList<>();

            final Builder builder = builderWithBasicIQFieldsSet();
            builder.setProfile(CoreFixtures.PROFILE_ID);
            builder.addFeature(VOICE_MESSAGE_ID_FEATURE);
            builder.setAction(action);
            builder.validate(errors);

            assertThat(errors, contains(String.format("Invalid manage-voice-message stanza; The '%s' action requires a label", action)));
        }
    }

    @Test
    public void withCheckThatNoMoreThanOneFeatureAllowedForGivenActions() {
        final ManageVoiceMessageAction[] actions = {
                ManageVoiceMessageAction.RECORD,
                ManageVoiceMessageAction.EDIT
        };

        for (ManageVoiceMessageAction action : actions) {
            final List<String> errors = new ArrayList<>();

            final Builder builder = builderWithBasicIQFieldsSet();
            builder.setProfile(CoreFixtures.PROFILE_ID);
            builder.setAction(action);

            builder.addFeature(ManageVoiceMessageFixtures.VOICE_MESSAGE_ID_FEATURE);
            builder.addFeature(FeatureId.from("MK1048").get());
            builder.setLabel(ManageVoiceMessageFixtures.VOICE_MESSAGE_LABEL);

            builder.validate(errors);

            assertThat(errors, contains(String.format("Invalid manage-voice-message stanza; Only one feature can be supplied with the '%s' action", action)));
        }
    }


    private Builder builderWithBasicIQFieldsSet() {
        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");

        return builder;
    }

}
