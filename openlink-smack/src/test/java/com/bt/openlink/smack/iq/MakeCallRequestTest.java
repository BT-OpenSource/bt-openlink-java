package com.bt.openlink.smack.iq;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.Optional;
import org.jivesoftware.smack.packet.IQ;

import com.bt.openlink.smack.Fixtures;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.MakeCallFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;


@SuppressWarnings({ "ConstantConditions", "RedundantThrows" })
public class MakeCallRequestTest {
	 @Rule public final ExpectedException expectedException = ExpectedException.none();

	 @BeforeClass
	    public static void setUpClass() throws Exception {
	        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
	    }

	    @AfterClass
	    public static void tearDownClass() throws Exception {
	        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
	    }
	    
	    @Test
	    public void canCreateAStanza() throws Exception {

	        final MakeCallRequest request = MakeCallRequest.Builder.start()
	                .setId(CoreFixtures.STANZA_ID)
	                .setTo(Fixtures.TO_JID)
	                .setFrom(Fixtures.FROM_JID)
	                .setJID(Fixtures.USER_FULL_JID)
	                .setInterestId(CoreFixtures.INTEREST_ID)
	                .setDestination(CoreFixtures.CALLED_DESTINATION)
	                .addFeatureId(CoreFixtures.FEATURE_ID)
	                .build();

	        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
	        assertThat(request.getTo(), is(Fixtures.TO_JID));
	        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
	        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
	        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
	        assertThat(request.getDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
	        assertThat(request.getFeatureIds(), contains(CoreFixtures.FEATURE_ID));
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
	                .addFeatureId(CoreFixtures.FEATURE_ID)
	                .build();

	        assertThat(request.toXML().toString(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_REQUEST).ignoreWhitespace());
	    }

	    @Test
	    public void willParseAnXmppStanza() throws Exception {

	        final MakeCallRequest request = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_REQUEST);
	        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
	        assertThat(request.getTo(), is(Fixtures.TO_JID));
	        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
	        assertThat(request.getType(), is(IQ.Type.set));
	        assertThat(request.getJID().get(), is(Fixtures.USER_FULL_JID));
	        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
	        assertThat(request.getDestination().get(), is(CoreFixtures.CALLED_DESTINATION));
	        assertThat(request.getFeatureIds(), contains(CoreFixtures.FEATURE_ID));
	        assertThat(request.getParseErrors(), is(empty()));
	    }

	    @Test
	    public void willReturnParsingErrors() throws Exception {

	        final MakeCallRequest request = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_REQUEST_WITH_BAD_VALUES);

	        assertThat(request.getStanzaId(), is(nullValue()));
	        assertThat(request.getTo(), is(nullValue()));
	        assertThat(request.getFrom(), is(nullValue()));
	        assertThat(request.getType(), is(IQ.Type.get));
	        assertThat(request.getJID(), is(Optional.empty()));
	        assertThat(request.getInterestId(), is(Optional.empty()));
	        assertThat(request.getDestination(), is(Optional.empty()));
	        assertThat(request.getParseErrors(), contains(
	              //  "Invalid stanza; missing 'to' attribute is mandatory",
	              //  "Invalid stanza; missing 'from' attribute is mandatory",
	              //  "Invalid stanza; missing 'id' attribute is mandatory",
	               // "Invalid stanza; missing or incorrect 'type' attribute",
	                "Invalid make-call request stanza; missing or invalid 'jid'"));
	    }

	    @Test
	    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

	        final MakeCallRequest request = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_REQUEST_WITH_BAD_VALUES);

	        assertThat(request.toXML().toString(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());
	    }
}
