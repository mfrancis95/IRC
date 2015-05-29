package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public abstract class IRCAdapter<C extends IRCClient> implements IRCChatListener<C>, 
        IRCJoinListener<C>, IRCLeaveListener<C>, IRCLineListener<C>, IRCPingListener<C> {
    
    public void onJoin(C client, String channel, String user) {}
    
    public void onLeave(C client, String channel, String user) {}

    public void onMessage(C client, String from, String to, String message) {}

}