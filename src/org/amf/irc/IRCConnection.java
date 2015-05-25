package org.amf.irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IRCConnection implements IRC {
    
    private Socket socket;
    
    private BufferedReader in;
    
    private BufferedWriter out;
    
    List<IRCBot> bots;
    
    private BlockingQueue<String> outputQueue;
    
    private int messages;
    
    private Timer timer;
    
    public IRCConnection(String server, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(server, port), 2000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bots = new LinkedList<>();
        outputQueue = new LinkedBlockingQueue<>();
        messages = 20;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                messages = 20;
                System.out.println("Messages remaining before timeout: " + messages);
            }
            
        }, 30000, 30000);
        new Thread(new Input()).start();
        new Thread(new Output()).start();
    }
    
    public void join(String channel) {
        sendLines("JOIN " + channel);
    }
    
    public void leave(String channel) {
        sendLines("PART " + channel);
    }
    
    public void logIn(String nickname, String password) {
        sendLines("PASS " + password, "NICK " + nickname);
    }
    
    public void sendLines(String... lines) {
        for (String line : lines) {
            outputQueue.offer(line + "\r\n");
        }
    }
    
    public void sendMessage(String channel, String message) {
        sendLines("PRIVMSG " + channel + " :" + message);
    }
    
    private class Input implements Runnable {

        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    if (line.contains("PRIVMSG")) {
                        String from = line.substring(1, line.indexOf("!"));
                        String to = line.substring(line.indexOf("#"));
                        String message = to;
                        to = to.substring(0, to.indexOf(" "));
                        message = message.substring(message.indexOf(":"));
                        for (IRCBot bot : bots) {
                            bot.onMessage(from, to, message);
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
                if (messages > 0) {
                    try {
                        out.write(outputQueue.take());
                        out.flush();
                        System.out.println("Messages remaining before timeout: " + (--messages));
                    } 
                    catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        
    }
    
}