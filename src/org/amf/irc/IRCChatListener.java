package org.amf.irc;

public interface IRCChatListener extends IRCListener {
    
    void onMessage(IRCClient client, String from, String to, String message);
    
}