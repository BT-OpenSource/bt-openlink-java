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
public class GetInterestResultTest {

    private static final Interest INTEREST = Interest.Builder.start()
            .setId(Fixtures.INTEREST_ID)
            .setType(InterestType.from("DirectoryNumber").get())
            .setLabel("6001/1")
            .setDefault(true)
            .build();


    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String GET_INTEREST_RESULT = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-interest\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <interests xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/interests\">\n" +
            "          <interest id=\"" + Fixtures.INTEREST_ID + "\" type=\"DirectoryNumber\" label=\"6001/1\" default=\"true\"/>\n" +
            "        </interests>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_INTEREST_RESULT_WITH_BAD_VALUES = "<iq type=\"set\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" action=\"execute\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-interest\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final GetInterestResult result = GetInterestResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterest(INTEREST)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getInterest().get(), is(INTEREST));
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

        final GetInterestResult result = GetInterestResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterest(INTEREST)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GET_INTEREST_RESULT).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetInterestResult result = (GetInterestResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_INTEREST_RESULT));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getType(), is(IQ.Type.result));
        final Interest interest = result.getInterest().get();
        assertThat(interest.getId(), is(InterestId.from("test-interest-id")));
        assertThat(interest.getType(), is(InterestType.from("DirectoryNumber")));
        assertThat(interest.getLabel().get(), is("6001/1"));
        assertThat(interest.isDefaultInterest().get(), is(true));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetInterestResult result = GetInterestResult.from(Fixtures.iqFrom(GET_INTEREST_RESULT_WITH_BAD_VALUES));

        final List<String> parseErrors = result.getParseErrors();
        int errorCount = 0;
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing or incorrect 'type' attribute"));
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing 'to' attribute is mandatory"));
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing 'from' attribute is mandatory"));
        assertThat(parseErrors.get(errorCount++), is("Invalid stanza; missing 'id' attribute is mandatory"));
        assertThat(parseErrors.get(errorCount++), is("Invalid get-interest result; missing 'interest' element is mandatory"));
        assertThat(parseErrors.size(), is(errorCount));
    }

    @Test
    public void willBuildAResultFromARequest() throws Exception {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setID(Fixtures.STANZA_ID)
                .setInterestId(Fixtures.INTEREST_ID)
                .build();

        final GetInterestResult result = GetInterestResult.Builder.start(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }

}