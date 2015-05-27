package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCPingListener extends IRCListener {
    
    default void onPing(IRCClient client, String server) {
        client.pong(server);
    }
    
}