package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetInterestFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;
import com.bt.openlink.type.Interest;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetInterestResultTest {
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

	        final GetInterestResult result = GetInterestResult.Builder.start()
	                .setId(CoreFixtures.STANZA_ID)
	                .setTo(Fixtures.TO_JID)
	                .setFrom(Fixtures.FROM_JID)
	                .setInterest(CoreFixtures.INTEREST)
	                .build();

	        assertThat(result.getType(), is(IQ.Type.result));
	        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
	        assertThat(result.getTo(), is(Fixtures.TO_JID));
	        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
	        assertThat(result.getInterest().get(), is(CoreFixtures.INTEREST));
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
	                .setId(CoreFixtures.STANZA_ID)
	                .setTo(Fixtures.TO_JID)
	                .setFrom(Fixtures.FROM_JID)
	                .setInterest(CoreFixtures.INTEREST)
	                .build();

	        assertThat(result.toXML().toString(), isIdenticalTo(GetInterestFixtures.GET_INTEREST_RESULT).ignoreWhitespace());
	    }

	    @Test
	    public void willParseAnXmppStanza() throws Exception {

	        final GetInterestResult result = PacketParserUtils.parseStanza(GetInterestFixtures.GET_INTEREST_RESULT);
	        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
	        assertThat(result.getTo(), is(Fixtures.TO_JID));
	        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
	        assertThat(result.getType(), is(IQ.Type.result));
	        final Interest interest = result.getInterest().get();
	        assertThat(interest.getId().get(), is(CoreFixtures.INTEREST_ID));
	        assertThat(interest.getType().get(), is(CoreFixtures.INTEREST_TYPE));
	        assertThat(interest.getLabel().get(), is("test interest label"));
	        assertThat(interest.isDefaultInterest().get(), is(true));
	    }
	    
	    
	    //Scenario not valid here
/*	    @Test
	    public void willReturnParsingErrors() throws Exception {

//PacketParserUtils.parseStanza(GetInterestsFixtures.GET_INTERESTS_RESULT_WITH_BAD_VALUES);
	        final GetInterestResult result = PacketParserUtils.parseStanza(GetInterestFixtures.GET_INTEREST_RESULT_WITH_BAD_VALUES);

	        assertThat(result.getParseErrors(), contains(
	                "Invalid stanza; missing 'to' attribute is mandatory",
	                "Invalid stanza; missing 'from' attribute is mandatory",
	                "Invalid stanza; missing 'id' attribute is mandatory",
	                "Invalid stanza; missing or incorrect 'type' attribute",
	                "Invalid get-interest result stanza; missing 'interest'"));
	    }*/

	    @Test
	    public void willBuildAResultFromARequest() throws Exception {

	        final GetInterestRequest request = GetInterestRequest.Builder.start()
	                .setTo(Fixtures.TO_JID)
	                .setFrom(Fixtures.FROM_JID)
	                .setId(CoreFixtures.STANZA_ID)
	                .setInterestId(CoreFixtures.INTEREST_ID)
	                .build();

	        final GetInterestResult result = GetInterestResult.Builder.start(request)
	                .setInterest(CoreFixtures.INTEREST)
	                .build();

	        assertThat(result.getStanzaId(), is(request.getStanzaId()));
	        assertThat(result.getTo(), is(request.getFrom()));
	        assertThat(result.getFrom(), is(request.getTo()));
	    }


}
