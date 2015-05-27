package org.amf.irc;

public interface IRCJoinListener extends IRCListener {
    
    void onJoin(IRCBot bot, String channel, String user);
    
}