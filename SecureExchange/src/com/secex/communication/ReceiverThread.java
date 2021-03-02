package com.secex.communication;
import com.secex.main.User;

import java.util.Scanner;

public class ReceiverThread extends Thread {
    private final User user;
    private final String pName;
    public Thread t;
    public final String ANSI_GREEN = "\u001B[32m";
    public final String ANSI_RESET = "\u001B[0m";

    public ReceiverThread(User user, String peerName) {
        this.user = user;
        pName = peerName;
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
            System.out.println(ANSI_GREEN + pName + ": " + line + ANSI_GREEN + ANSI_RESET);
        }

        user.end();
    }
}
