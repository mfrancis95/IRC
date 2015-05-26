package org.amf.irc;

public interface IRCChatListener extends IRCListener {
    
    void onMessage(IRCBot bot, String from, String to, String message);
    
}