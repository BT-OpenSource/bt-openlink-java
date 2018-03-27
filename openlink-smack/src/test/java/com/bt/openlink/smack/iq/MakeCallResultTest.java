package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.apache.commons.lang3.builder.EqualsBuilder;
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
import com.bt.openlink.smack.Fixtures;

@SuppressWarnings({ "ConstantConditions", "RedundantThrows" })
public class MakeCallResultTest {
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
    public void canBuildAStanza() throws Exception {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final MakeCallResult result = MakeCallResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(result.toXML().toString(), isIdenticalTo(MakeCallFixtures.MAKE_CALL_RESULT).ignoreWhitespace());
    }

    @Test
    public void willEnsureTheStanzaHasACall() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");
        MakeCallResult.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final MakeCallResult result = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_RESULT);

        assertThat(result.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(EqualsBuilder.reflectionEquals(CoreFixtures.CALL_STATUS, result.getCallStatus().get(), false, null, true), is(true));
        assertThat(result.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final MakeCallResult result = PacketParserUtils.parseStanza(MakeCallFixtures.MAKE_CALL_RESULT_WITH_BAD_VALUES);

        assertThat(result.getParseErrors(), contains(
                //  "Invalid stanza; missing 'to' attribute is mandatory",
                //  "Invalid stanza; missing 'from' attribute is mandatory",
                //  "Invalid stanza; missing 'id' attribute is mandatory",
                //  "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid make-call result stanza; missing or invalid callstatus"));
    }

}
