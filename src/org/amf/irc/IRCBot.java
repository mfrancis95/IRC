package org.amf.irc;

public class IRCBot implements IRC {
    
    private IRCConnection connection;
    
    public IRCConnection getIRCConnection() {
        return connection;
    }
    
    public void onMessage(String from, String to, String message) {}
    
    public void sendMessage(String channel, String message) {
        connection.sendMessage(channel, message);
    }
    
    public void setIRCConnection(IRCConnection connection) {
        connection.bots.add(this);
        this.connection = connection;
    }
    
}