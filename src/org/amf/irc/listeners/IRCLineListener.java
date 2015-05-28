package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCLineListener extends IRCListener {
    
    default void onLine(IRCClient client, String line) {
        System.out.println(line);
    }
    
}