package com.secex.communication;

import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

public class SenderThread extends Thread {
    private final Socket sock;
    public Thread t;
    
    public SenderThread(Socket sock) {
        this.sock = sock;
    }

    public void start() {
        if(t == null) {
            t = new Thread(this, "sender");
            t.start();
        }
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        PrintWriter out;
        try {
            out = new PrintWriter(sock.getOutputStream(), true);
        }
        catch(IOException e) {
            System.out.println("Error creating sender thread output");
            input.close();
            return;
        }

        while(input.hasNextLine()) {
            String line = input.nextLine();

            if(line.equals("/quit")) {
                out.println(line);
                break;
            }

            out.println(line);
        }

        // If user quits, close the socket
        try {
            sock.close();
        }
        catch(IOException e) {
            System.out.println("Error closing socket");
        }

        input.close();
    }
}
