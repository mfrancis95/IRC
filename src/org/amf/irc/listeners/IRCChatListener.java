package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCChatListener extends IRCListener {
    
    void onMessage(IRCClient client, String from, String to, String message);
    
}