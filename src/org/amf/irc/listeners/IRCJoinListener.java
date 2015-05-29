package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCJoinListener<C extends IRCClient> extends IRCListener<C> {
    
    void onJoin(C client, String channel, String user);
    
}