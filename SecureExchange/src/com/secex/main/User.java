package com.secex.main;
import com.secex.encryption.AES;
import com.secex.encryption.RSA;

import com.secex.communication.*;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class User {
    private String name;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private AES aes;

    public User(String ip, int port, String name) {
        this.name = name;

        // Create new socket and reader/writer
        try {
            socket = new Socket(ip, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create aes
        aes = new AES();
    }

    public String getName() {
        return name;
    }

    public void send(String message) {
        // Send message
        out.println(message);
    }

    public void send(byte[] message) {
        // Encode bytes into string
        String strMessage = Base64.getEncoder().encodeToString(message);

        // Send message
        out.println(strMessage);
    }

    /**
     * Send a message using AES encryption
     * @param message The message to encrypt
     */
    public void sendEncrypted(String message) {

    }

    /**
     * Send a file using AES encryption
     * @param file The bytes of the file to encrypt
     */
    public void sendEncrypted(byte[] file) {

    }

    public String receiveString() {
        return in.nextLine();
    }

    public byte[] receiveBytes() {
        String reply = in.nextLine();

        byte[] bytes = Base64.getDecoder().decode(reply);

        return bytes;
    }

    public void setKey(byte[] key) {
        aes.setKey(key);
    }

    public void comms(String peerName) {
        // Create two new threads - one for sending and one for receiving
        SenderThread S1 = new SenderThread(socket);
        ReceiverThread R1 = new ReceiverThread(socket, peerName);

        S1.start();
        R1.start();

        try{
            S1.t.join();
            R1.t.join();
            System.out.println("Threads closed");
        }
        catch(InterruptedException e) {
            System.out.println("Error joining threads");
        }
    }
}
