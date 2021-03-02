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

    public boolean stillReceiving() {
        return in.hasNextLine();
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
        String ciphertext;
        // Encrypt message
        byte[][] encryptedPackage = aes.encrypt(message);

        // Encode iv
        ciphertext = Base64.getEncoder().encodeToString(encryptedPackage[0]);

        // Add delimiter
        ciphertext = ciphertext + ",";

        // Add encrypted text
        ciphertext = ciphertext + Base64.getEncoder().encodeToString(encryptedPackage[1]);

        out.println(ciphertext);
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

    public String receiveEncryptedString() {
        // Get encrypted string
        String encryptedString = in.nextLine();

        // Get IV and encrypted text
        String[] parsedEncryptedString = encryptedString.split(",", 2);

        byte[] iv = Base64.getDecoder().decode(parsedEncryptedString[0]);
        byte[] ciphertext = Base64.getDecoder().decode(parsedEncryptedString[1]);

        // Decrypt message
        String plaintext = aes.decrypt(iv, ciphertext);

        return plaintext;
    }

    public void setKey(byte[] key) {
        aes.setKey(key);
    }

    public void end() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
