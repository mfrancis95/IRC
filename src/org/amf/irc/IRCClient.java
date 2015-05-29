package org.amf.irc;

import org.amf.irc.listeners.IRCPingListener;
import org.amf.irc.listeners.IRCJoinListener;
import org.amf.irc.listeners.IRCChatListener;
import org.amf.irc.listeners.IRCListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.amf.irc.listeners.IRCLeaveListener;
import org.amf.irc.listeners.IRCLineListener;

public class IRCClient {
    
    private Socket socket;
    
    private BufferedReader in;
    
    private BufferedWriter out;
    
    private List<IRCListener> listeners;
    
    private BlockingQueue<String> outputQueue;
    
    private long sendDelay = (long) (30.0 / 20.0 * 1000.0);
    
    public IRCClient(String server) throws IOException {
        this(server, 6667);
    }
    
    public IRCClient(String server, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(server, port), 2000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        listeners = new LinkedList<>();
        outputQueue = new LinkedBlockingQueue<>();
        new Thread(new Input()).start();
        new Thread(new Output()).start();
    }
    
    public void addListener(IRCListener listener) {
        listeners.add(listener);
    }
    
    public long getSendDelay() {
        return sendDelay;
    }
    
    public void join(String channel) {
        sendLine("JOIN " + channel);
    }
    
    public void leave(String channel) {
        sendLine("PART " + channel);
    }
    
    public void logIn(String nickname, String password) {
        sendLine("PASS " + password);
        sendLine("NICK " + nickname);
    }
    
    public void pong(String server) {
        sendLine("PONG :" + server);
    }
    
    public void removeListener(IRCListener listener) {
        listeners.remove(listener);
    }
    
    public void sendLine(String line) {
        outputQueue.offer(line);
    }
    
    public void sendMessage(String channel, String message) {
        sendLine("PRIVMSG " + channel + " :" + message);
    }
    
    public void setSendDelay(long milliseconds) {
        sendDelay = milliseconds;
    }
    
    private class Input implements Runnable {

        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    for (IRCListener listener : listeners) {
                        if (listener instanceof IRCLineListener) {
                            ((IRCLineListener) listener).onLine(IRCClient.this, line);
                        }
                    }
                    if (line.contains("JOIN")) {
                        String channel = line.substring(line.indexOf("#"));
                        String user = line.substring(1, line.indexOf("!"));
                        for (IRCListener listener : listeners) {
                            if (listener instanceof IRCJoinListener) {
                                ((IRCJoinListener) listener).onJoin(IRCClient.this, channel, user);
                            }
                        }
                    }
                    else if (line.contains("PART")) {
                        String channel = line.substring(line.indexOf("#"));
                        String user = line.substring(1, line.indexOf("!"));
                        for (IRCListener listener : listeners) {
                            if (listener instanceof IRCLeaveListener) {
                                ((IRCLeaveListener) listener).onLeave(IRCClient.this, channel, user);
                            }
                        }
                    }
                    else if (line.contains("PING")) {
                        String server = line.substring(6);
                        for (IRCListener listener : listeners) {
                            if (listener instanceof IRCPingListener) {
                                ((IRCPingListener) listener).onPing(IRCClient.this, server);
                            }
                        }
                    }
                    else if (line.contains("PRIVMSG")) {
                        String from = line.substring(1, line.indexOf("!"));
                        String to = line.substring(line.indexOf("PRIVMSG") + 8);
                        String message = to;
                        to = to.substring(0, to.indexOf(" "));
                        message = message.substring(message.indexOf(":"));
                        for (IRCListener listener : listeners) {
                            if (listener instanceof IRCChatListener) {
                                ((IRCChatListener) listener).onMessage(IRCClient.this, from, to, message);
                            }
                        }
                    }
                }
            } 
            catch (IOException ex) {
                System.out.println(ex);
            }
        }
        
    }
    
    private class Output implements Runnable {

        public void run() {
            while (socket.isConnected()) {
                try {
                    String message = outputQueue.take() + "\r\n";
                    out.write(message);
                    out.flush();
                    System.out.print("Message sent: " + message);
                    Thread.sleep(sendDelay);
                } 
                catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        
    }
    
}