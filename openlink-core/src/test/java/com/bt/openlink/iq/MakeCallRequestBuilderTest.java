package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.type.OriginatorReference;

@SuppressWarnings({"ConstantConditions", "RedundantThrows"})
public class MakeCallRequestBuilderTest {
    private static class Builder extends MakeCallRequestBuilder<MakeCallRequestBuilder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() throws Exception {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setJID("jid")
                .setProfileId(CoreFixtures.PROFILE_ID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .addFeature(MakeCallFixtures.MAKE_CALL_FEATURE)
                .setDestination(CoreFixtures.CALLED_DESTINATION)
                .addOriginatorReference(CoreFixtures.ORIGINATOR_REFERENCE)
                .addOriginatorReference("key2", "value2");

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getJID().get(), is("jid"));
        assertThat(builder.getProfileId().get(), is(CoreFixtures.PROFILE_ID));
        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(builder.getFeatures(), contains(MakeCallFixtures.MAKE_CALL_FEATURE));
        assertThat(builder.getDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
        assertThat(builder.getOriginatorReferences(),contains(CoreFixtures.ORIGINATOR_REFERENCE, new OriginatorReference("key2", "value2")));
    }

    @Test
    public void willValidateTheJidIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call request 'jid' has not been set");

        builder.validate();
    }

    @Test
    public void willCheckThatJidIsSet() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid make-call request stanza; missing or invalid 'jid'"));
        assertThat(builder.getJID(), is(Optional.empty()));
        assertThat(builder.getProfileId(), is(Optional.empty()));
        assertThat(builder.getInterestId(), is(Optional.empty()));
        assertThat(builder.getFeatures(), is(empty()));
        assertThat(builder.getDestination(), is(Optional.empty()));
    }

}