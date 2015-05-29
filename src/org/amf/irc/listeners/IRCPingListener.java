package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCPingListener<C extends IRCClient> extends IRCListener<C> {
    
    default void onPing(C client, String server) {
        client.pong(server);
    }
    
}