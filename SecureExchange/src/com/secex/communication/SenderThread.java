package com.secex.communication;
import com.secex.main.User;

import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

public class SenderThread extends Thread {
    private final User user;
    public Thread t;
    
    public SenderThread(User user) {
        this.user = user;
    }

    public void start() {
        if(t == null) {
            t = new Thread(this, "sender");
            t.start();
        }
    }

    public void run() {
        Scanner fromUser = new Scanner(System.in);

        while(fromUser.hasNextLine()) {
            String line = fromUser.nextLine();

            if(line.equals("/quit")) {
                user.send(line);
                break;
            }

            user.sendEncrypted(line);
        }

        // If user quits, close the socket
        user.end();
        fromUser.close();
    }
}
