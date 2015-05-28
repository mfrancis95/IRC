package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public abstract class IRCAdapter implements IRCChatListener, IRCJoinListener, IRCLeaveListener, IRCLineListener, IRCPingListener {
    
    public void onJoin(IRCClient client, String channel, String user) {}
    
    public void onLeave(IRCClient client, String channel, String user) {}

    public void onMessage(IRCClient client, String from, String to, String message) {}

}