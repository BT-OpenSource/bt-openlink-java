package com.bt.openlink.tinder.iq;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("ConstantConditions")
public class MakeCallRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String MAKE_CALL_REQUEST = "<iq type='set' id='" + CoreFixtures.STANZA_ID + "' to='" + Fixtures.TO_JID + "' from='" + Fixtures.FROM_JID + "'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#make-call'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in>\n" +
            "        <jid>" + Fixtures.USER_FULL_JID + "</jid>\n" +
            "        <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
            "        <destination>" + CoreFixtures.CALLED_DESTINATION + "</destination>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String MAKE_CALL_REQUEST_WITH_BAD_VALUES = "<iq type='get'>\n" +
            "  <command xmlns='http://jabber.org/protocol/commands' action='execute' node='http://xmpp.org/protocol/openlink:01:00:00#make-call'>\n" +
            "    <iodata xmlns='urn:xmpp:tmp:io-data' type='input'>\n" +
            "      <in/>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final MakeCallRequest request = MakeCallRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setDestination(CoreFixtures.CALLED_DESTINATION)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
    }

    @Test
    public void cannotCreateAStanzaWithoutAJID() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call request 'jid' has not been set");
        MakeCallRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final MakeCallRequest request = MakeCallRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setJID(Fixtures.USER_FULL_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .setDestination(CoreFixtures.CALLED_DESTINATION)
                .build();

        assertThat(request.toXML(), isIdenticalTo(MAKE_CALL_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final MakeCallRequest request = (MakeCallRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(MAKE_CALL_REQUEST));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final IQ iq = Fixtures.iqFrom(MAKE_CALL_REQUEST_WITH_BAD_VALUES);

        final MakeCallRequest request = MakeCallRequest.from(iq);

        assertThat(request.getID(), is(nullValue()));
        assertThat(request.getTo(), is(nullValue()));
        assertThat(request.getFrom(), is(nullValue()));
        assertThat(request.getType(), is(IQ.Type.get));
        assertThat(request.getJID(), is(Optional.empty()));
        assertThat(request.getInterestId(), is(Optional.empty()));
        assertThat(request.getDestination(), is(Optional.empty()));
        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid make-call request stanza; missing or invalid 'jid'"
        ));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final IQ iq = Fixtures.iqFrom(MAKE_CALL_REQUEST_WITH_BAD_VALUES);

        final MakeCallRequest request = MakeCallRequest.from(iq);

        assertThat(request.toXML(), isIdenticalTo(iq.toXML()).ignoreWhitespace());
    }
}