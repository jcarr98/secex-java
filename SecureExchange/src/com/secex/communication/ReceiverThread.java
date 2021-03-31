package com.secex.communication;
import com.secex.main.User;

import java.util.Scanner;

public class ReceiverThread extends Thread {
    private final User user;
    private final String pName;
    public Thread t;
    public final String ANSI_GREEN = "\u001B[32m";
    public final String ANSI_RESET = "\u001B[0m";
    String os;

    public ReceiverThread(User user, String peerName) {
        this.user = user;
        pName = peerName;
        os = System.getProperty("os.name").split(" ")[0];
    }

    public void start() {
        if(t == null) {
            t = new Thread(this, "receiver");
            t.start();
        }
    }

    public void run() {
        System.out.println("Connected to " + pName);

        while(user.stillReceiving()) {
            String line = user.receiveEncryptedString();
            if(os.equals("Mac")) {
                System.out.println(ANSI_GREEN + pName + ": " + line + ANSI_GREEN + ANSI_RESET);
            }
            else {
                System.out.println(pName + ": " + line);
            }
        }

        user.end();
    }
}
