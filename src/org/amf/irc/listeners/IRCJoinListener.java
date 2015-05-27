package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCJoinListener extends IRCListener {
    
    void onJoin(IRCClient client, String channel, String user);
    
}