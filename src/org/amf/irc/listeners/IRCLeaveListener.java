package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCLeaveListener extends IRCListener {
    
    void onLeave(IRCClient client, String channel, String user);
    
}