package com.secex.communication;
import com.secex.encryption.RSA;
import com.secex.main.User;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public class ServerConnector {
    private String peer;
    private RSA rsa;
    private PublicKey serverKey;
    private User user;

    public ServerConnector(User user) {
        // Create new user
        this.user = user;

        // Generate RSA keys
        rsa = new RSA();
    }

    public String connect(int id) {
        boolean result = handshake(id);

        // Check for successful handshake
        if(!result) {
            System.out.println("Bad handshake");
            return null;
        }

        // Wait for CONNECTED message, meaning connection to peer was successful
        byte[] eConnected = user.receiveBytes();
        String connected = rsa.decrypt(eConnected);
        String[] pConnected = connected.split("\\s", 2);

        String pName;
        if(!pConnected[0].equals("CONNECTED")) {
            System.out.println("Error connecting to peer, received " + connected);
            return null;
        }
        else {
            pName = pConnected[1];
        }

        return pName;
    }

    public void start() {
        user.comms(peer);
    }

    private boolean handshake(int id) {
        String hello = "HELLO " + id;
        // Send id
        user.send(hello);

        // Wait for reply
        String reply = user.receiveString();

        // Reply should be OK, KEY indicating public rsa key request from server
        if(!reply.equals("OK KEY")) {
            System.out.println("Bad handshake, key not requested");
            return false;
        }

        // Exchange RSA keys with server
        if(!keyExchange()) {
            System.out.println("Error exchanging keys");
            return false;
        }

        // Get confirmation message from server, should be encrypted
        byte[] eConfirm = user.receiveBytes();
        String confirm = rsa.decrypt(eConfirm);

        // OK CONNECTION means successful connection to peer
        // OK WAITING means successful connection to server, waiting for peer to connect
        if(confirm.equals("OK WAITING")) {
            System.out.println("Waiting for peer...");
        }
        else if(!confirm.equals("OK CONNECTION")) {
            return false;
        }

        // Get AES key from server
        byte[] eKey = user.receiveBytes();
        byte[] key = rsa.bDecrypt(eKey);
        user.setKey(key);

        // Send confirmation and username to server
        byte[] eMessage = rsa.encrypt("KEYRECV " + user.getName(), serverKey);
        user.send(eMessage);

        return true;
    }

    private boolean keyExchange() {
        // Send public RSA key to server
        user.send(rsa.getPublicKey());

        // Wait for OK from server, should be encrypted
        byte[] eKeyReceived = user.receiveBytes();
        String keyReceived = rsa.decrypt(eKeyReceived);

        if(!keyReceived.equals("OK")) {
            System.out.println("No key from server");
            return false;
        }

        // Wait for key from server
        byte[] keyBytes = user.receiveBytes();

        // Create server key
        try {
            serverKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
