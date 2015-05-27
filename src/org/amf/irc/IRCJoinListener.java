package org.amf.irc;

public interface IRCJoinListener extends IRCListener {
    
    void onJoin(IRCClient client, String channel, String user);
    
}