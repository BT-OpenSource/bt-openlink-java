package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetInterestsResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String GET_INTERESTS_RESULT = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-interests\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <interests xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/interests\">\n" +
            "          <interest id=\"" + Fixtures.INTEREST_ID + "\" type=\"DirectoryNumber\" label=\"6001/1\" default=\"true\"/>\n" +
            "          <interest id=\"sip:6001@uta.bt.com-DirectDial-2trader1@btsm11\" type=\"DirectoryNumber\" label=\"6001/2\" default=\"true\"/>\n" +
            "          <interest id=\"sip:6002@uta.bt.com-DirectDial-1trader1@btsm11\" type=\"DirectoryNumber\" label=\"6002/1\" default=\"false\"/>\n" +
            "        </interests>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_INTERESTS_RESULT_WITH_BAD_VALUES = "<iq type=\"set\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" action=\"execute\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-interests\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final GetInterestsResult result = GetInterestsResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetInterestsResult.Builder.start()
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final Interest interest1 = Interest.Builder.start()
                .setInterestId(Fixtures.INTEREST_ID)
                .setInterestType(InterestType.from("DirectoryNumber").get())
                .setLabel("6001/1")
                .setDefault(true)
                .build();
        final Interest interest2 = Interest.Builder.start()
                .setInterestId(InterestId.from("sip:6001@uta.bt.com-DirectDial-2trader1@btsm11").get())
                .setInterestType(InterestType.from("DirectoryNumber").get())
                .setLabel("6001/2")
                .setDefault(true)
                .build();
        final Interest interest3 = Interest.Builder.start()
                .setInterestId(InterestId.from("sip:6002@uta.bt.com-DirectDial-1trader1@btsm11").get())
                .setInterestType(InterestType.from("DirectoryNumber").get())
                .setLabel("6002/1")
                .setDefault(false)
                .build();
        final GetInterestsResult result = GetInterestsResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addInterest(interest1)
                .addInterest(interest2)
                .addInterest(interest3)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GET_INTERESTS_RESULT).ignoreWhitespace());
    }

    @Test
    public void willNotBuildAPacketWithDuplicateFeatureIds() throws Exception {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The interest id must be unique");
        final Interest interest = Interest.Builder.start()
                .setInterestId(Fixtures.INTEREST_ID)
                .build();
        GetInterestsResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addInterest(interest)
                .addInterest(interest)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetInterestsResult result = (GetInterestsResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_INTERESTS_RESULT));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        final List<Interest> interests = result.getInterests();

        int i = 0;
        Interest interest = interests.get(i++);
        assertThat(interest.getId().get(), is(Fixtures.INTEREST_ID));
        assertThat(interest.getType(), is(InterestType.from("DirectoryNumber")));
        assertThat(interest.getLabel().get(), is("6001/1"));
        assertThat(interest.isDefault().get(), is(true));

        interest = interests.get(i++);
        assertThat(interest.getId(), is(InterestId.from("sip:6001@uta.bt.com-DirectDial-2trader1@btsm11")));
        assertThat(interest.getType(), is(InterestType.from("DirectoryNumber")));
        assertThat(interest.getLabel().get(), is("6001/2"));
        assertThat(interest.isDefault().get(), is(true));

        interest = interests.get(i++);
        assertThat(interest.getId(), is(InterestId.from("sip:6002@uta.bt.com-DirectDial-1trader1@btsm11")));
        assertThat(interest.getType(), is(InterestType.from("DirectoryNumber")));
        assertThat(interest.getLabel().get(), is("6002/1"));
        assertThat(interest.isDefault().get(), is(false));

        assertThat(interests.size(), is(i));

        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetInterestsResult result = GetInterestsResult.from(Fixtures.iqFrom(GET_INTERESTS_RESULT_WITH_BAD_VALUES));

        final List<String> parseErrors = result.getParseErrors();
        int errorCount = 0;
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing or incorrect 'type' attribute"));
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing 'to' attribute is mandatory"));
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing 'from' attribute is mandatory"));
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing 'id' attribute is mandatory"));
        assertThat(parseErrors.get(errorCount++), is("Invalid get-interests result; missing 'interests' element is mandatory"));
        assertThat(parseErrors.size(), is(errorCount));
    }

}
