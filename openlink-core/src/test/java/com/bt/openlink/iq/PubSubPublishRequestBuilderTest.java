package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.type.InterestId;

@SuppressWarnings({"ConstantConditions", "RedundantThrows"})
public class PubSubPublishRequestBuilderTest {

    private static class Builder extends PubSubPublishRequestBuilder<Builder, String, CoreFixtures.typeEnum> {
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
        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setCallStatus(CoreFixtures.CALL_STATUS);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(builder.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willValidateTheNodeIdIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId'/'interestId' has not been set");

        builder.validate();
    }

    @Test
    public void willValidateCallsAreOnTheRightInterest() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id test-call-id is on interest test-interest-id which differs from the pub-sub node id test-interest-id-2");

        builder.setInterestId(InterestId.from("test-interest-id-2").get())
                .setCallStatus(CoreFixtures.CALL_STATUS);

        builder.validate();
    }

    @Test
    public void willCheckThePubSubNodeId() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.setCallStatus(CoreFixtures.CALL_STATUS);

        builder.validate(errors);

        assertThat(errors, contains("Invalid pub-sub publish request stanza; missing node id/interest id"));
    }

    @Test
    public void atLeastOneCallMustBePublished() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Either a callstatus or a devicestatus event must be published");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .validate();

    }

    @Test
    public void willBuildADeviceStatusPublishRequest() {

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .validate();

        assertThat(builder.getDeviceStatus().get(),is(CoreFixtures.DEVICE_STATUS_LOGON));
    }

    @Test
    public void willNotBuildARequestWithBothCallsAndDeviceStatus() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A callstatus and a devicestatus event cannot be published in the same message");

        builder.setInterestId(CoreFixtures.INTEREST_ID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .setDeviceStatus(CoreFixtures.DEVICE_STATUS_LOGON)
                .validate();

    }
}