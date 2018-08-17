# bt-openlink-java ![Build status](https://travis-ci.org/BT-OpenSource/bt-openlink-java.svg?branch=master) [ ![Download](https://api.bintray.com/packages/gregdthomas/bt-openlink/bt-openlink-java/images/download.svg) ](https://bintray.com/gregdthomas/bt-openlink/bt-openlink-java/_latestVersion)

A Java implementation of the BT Openlink API that supports the [Smack](https://github.com/igniterealtime/Smack) and [Tinder](https://github.com/igniterealtime/tinder) libraries (the Smack library is typically used for writing applications that are XMPP clients, whilst the Tinder library is typically used for applications that run on the Openfire server itself).

Usage of these libraries is very similar, with the only differences caused by the different underlying libraries.

## Using the Smack library

### Add the dependencies to your project

<table>
  <thead>
    <tr>
      <th>Gradle</th>
      <th>Maven</th>
      </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <pre><code>
repositories {
  ...
  jcenter()
}
...
dependencies {
  ...
  compile 'com.bt.openlink:openlink-smack:0.0.9'
}
        </code></pre>
      </td>
      <td>
        <pre><code>
...
  &lt;repositories&gt;
    &lt;repository&gt;
      &lt;id&gt;jcenter&lt;/id&gt;
      &lt;url&gt;https://jcenter.bintray.com/&lt;/url&gt;
    &lt;/repository&gt;
  &lt;/repositories&gt;
...
  &lt;dependencies&gt;
    ...
    &lt;dependency&gt;
      &lt;groupId&gt;com.bt.openlink&lt;/groupId&gt;
      &lt;artifactId&gt;openlink-smack&lt;/artifactId&gt;
      &lt;version&gt;0.0.9&lt;/version&gt;
    &lt;/dependency&gt;
  &lt;/dependencies&gt;
        </code></pre>
      </td>
    </tr>
  </tbody>
</table>

### Building a packet in code

The library uses a fluent API throughout, so all the Openlink stanzas are built in a very similar fashion. For 
example, to build an Openlink `get-profiles` request stanza:
```
import com.bt.openlink.smack.iq.GetProfilesRequest;
   ...
   final GetProfilesRequest getProfilesRequest = GetProfilesRequest.Builder.start()
            .setTo(openlinkJID)
            .setFrom(userJID)
            .setJID(userJID)
            .build();
   ...
   xmppConnection.sendStanza(getProfilesRequest);
```

Refer to the Javadoc for full details of all the stanzas that can be built and the attributes that can be set for
them. Note that the library will prevent an invalid stanza from being built - so a `get-profiles` request cannot be 
built unless the JID is set for example.

### Parsing a packet

The library provides a Smack IQ provider, so it's easy for packets to be parsed. Adding the provider will also ensure
that Openlink packets received by the Smack library will be parsed as they are received from the server before they
are passed back to client code. 
```
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.iq.OpenlinkIQProvider;
import com.bt.openlink.smack.iq.GetProfilesResult;
  ...
  ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
  final GetProfilesResult getProfilesResult = PacketParserUtils.parseStanza("<iq type='result' ...</iq>");
``` 
Note that if the stanza is not an Openlink `get-profiles` result packet, an exception will be thrown. To avoid this,
it may be necessary to check the type of the result first. For example:
```
  final Stanza stanza = PacketParserUtils.parseStanza("<iq type='result' ...</iq>");
  if (stanza instanceof GetProfilesResult) {
    final GetProfilesResult getProfilesResult = (GetProfilesResult)stanza;
    ... 
  }
```

## Using the Tinder library

### Add the dependencies to your project

<table>
  <thead>
    <tr>
      <th>Gradle</th>
      <th>Maven</th>
      </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <pre><code>
repositories {
  ...
  jcenter()
}
...
dependencies {
  ...
  compile 'com.bt.openlink:openlink-tinder:0.0.9'
}
        </code></pre>
      </td>
      <td>
        <pre><code>
...
  &lt;repositories&gt;
    &lt;repository&gt;
      &lt;id&gt;jcenter&lt;/id&gt;
      &lt;url&gt;https://jcenter.bintray.com/&lt;/url&gt;
    &lt;/repository&gt;
  &lt;/repositories&gt;
...
  &lt;dependencies&gt;
    ...
    &lt;dependency&gt;
      &lt;groupId&gt;com.bt.openlink&lt;/groupId&gt;
      &lt;artifactId&gt;openlink-tinder&lt;/artifactId&gt;
      &lt;version&gt;0.0.9&lt;/version&gt;
    &lt;/dependency&gt;
  &lt;/dependencies&gt;
        </code></pre>
      </td>
    </tr>
  </tbody>
</table>

### Building a packet in code

This is essentially identical to the Smack, so to build an Openlink `get-profiles` request stanza:
```
import com.bt.openlink.tinder.iq.GetProfilesRequest;
   ...
   final GetProfilesRequest getProfilesRequest = GetProfilesRequest.Builder.start()
            .setTo(openlinkJID)
            .setFrom(userJID)
            .setJID(userJID)
            .build();
   ...
   componentManager.sendPacket(component, getProfilesRequest);
```

Refer to the Javadoc for full details of all the stanzas that can be built and the attributes that can be set for
them. As with the Smack library it's not possible to build an invalid stanza, e.g. a `get-profiles` request without
a JID.

### Parsing a packet

The library provides a parse of IQ packets.
```
import org.xmpp.packet.IQ;

import com.bt.openlink.tinder.iq.OpenlinkIQParser;
import com.bt.openlink.tinder.iq.GetProfilesResult;
  ...
  
  final IQ iq = ...
  final GetProfilesResult getProfilesResult = OpenlinkIQParser.parse(iq);  
``` 
though if the code is not already sure of the type of packet being parsed it may be necessary to check the type of 
the value returned:

```
  final IQ iq = ...
  final IQ parsedIQ = OpenlinkIQParser.parse(iq);
  if (parsedIQ instanceof GetProfilesResult) {
    final GetProfilesResult getProfilesResult = (GetProfilesResult)parsedIQ;
    ... 
  }
```