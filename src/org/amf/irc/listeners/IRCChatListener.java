package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCChatListener<C extends IRCClient> extends IRCListener<C> {
    
    void onMessage(C client, String from, String to, String message);
    
}