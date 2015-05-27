package org.amf.irc;

public interface IRCPingListener extends IRCListener {
    
    default void onPing(IRCClient client, String server) {
        client.pong(server);
    }
    
}