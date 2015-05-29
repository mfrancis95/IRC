package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCLeaveListener<C extends IRCClient> extends IRCListener<C> {
    
    void onLeave(C client, String channel, String user);
    
}