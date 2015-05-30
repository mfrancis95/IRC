package org.amf.irc.twitch;

import java.io.IOException;
import org.amf.irc.IRCClient;

public class TwitchIRCClient extends IRCClient {
    
    public TwitchIRCClient() throws IOException {
        super("irc.twitch.tv");
    }
    
    public void ban(String channel, String user) {
        sendMessage(channel, ".ban " + user);
    }
    
    public void clear(String channel) {
        sendMessage(channel, ".clear");
    }
    
    public void color(String channel, String color) {
        sendMessage(channel, ".color " + color);
    }
    
    public void me(String channel, String message) {
        sendMessage(channel, ".me " + message);
    }
    
    public void slow(String channel, int seconds) {
        sendMessage(channel, ".slow " + seconds);
    }
    
    public void timeout(String channel, String user, int seconds) {
        sendMessage(channel, ".timeout " + user + " " + seconds);
    }
    
    public void unban(String channel, String user) {
        sendMessage(channel, ".unban " + user);
    }
    
}