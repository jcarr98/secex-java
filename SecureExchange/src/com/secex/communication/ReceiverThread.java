package com.secex.communication;

import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;

public class ReceiverThread extends Thread {
    private final Socket sock;
    private final String pName;
    public Thread t;
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public ReceiverThread(Socket sock, String peerName) {
        this.sock = sock;
        pName = peerName;
    }

    public void start() {
        if(t == null) {
            t = new Thread(this, "receiver");
            t.start();
        }
    }

    public void run() {
        Scanner input;
        try {
            input = new Scanner(sock.getInputStream());
        }
        catch(IOException e) {
            System.out.println("Error creating sender thread input");
            return;
        }

        System.out.println("Connected to " + pName);

        while(input.hasNextLine()) {
            String line = input.nextLine();
            System.out.println(ANSI_GREEN + pName + ": " + line + ANSI_GREEN + ANSI_RESET);
        }

        input.close();
    }
}
