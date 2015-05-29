package org.amf.irc.listeners;

import org.amf.irc.IRCClient;

public interface IRCLineListener<C extends IRCClient> extends IRCListener<C> {
    
    default void onLine(C client, String line) {
        System.out.println(line);
    }
    
}