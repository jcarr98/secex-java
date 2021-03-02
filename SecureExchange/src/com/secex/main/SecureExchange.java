package com.secex.main;

import com.secex.communication.ReceiverThread;
import com.secex.communication.SenderThread;
import com.secex.communication.ServerConnector;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SecureExchange {
	public static boolean validateUsername(String name) {
		if(name.replaceAll("\\s", "").length() == 0 || name.length() > 20) {
			return false;
		}
		else {
			return true;
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		System.out.println("Running the Secure Client...");
	
	    if(args.length > 0 && args[0].equals("help")) {
	        System.out.println("To run the program, type 'java SecureClient {connection id}.");
	        System.out.println("A connection Id can be any number. It should match the connection ID of the peer you are trying to connect to.");
	        System.out.println("Your connection ID will expire after 60 seconds. If no connection is made within that time the program will shut down.");
	    }
	
	    Scanner input = new Scanner(System.in);

	    // Get connection id
	    int connectionId;
	    String dName;
	    while(true) {
	    	System.out.print("Please enter a display name: ");
	    	dName = input.nextLine();
	    	if(!validateUsername(dName)) {
	    		System.out.println("Please enter a valid display name. Display names must be between 1-20 characters");
	    		continue;
			}

	        System.out.print("Please enter a connection id: ");
	        try {
	            connectionId = Integer.parseInt(input.nextLine());
	        }
	        catch(NumberFormatException e) {
	            System.out.println("Connection ID must be an integer.");
	            continue;
	        }
	
	        break;
	    }

	    // Create a new user
		User user = new User("127.0.0.1", 8008, dName);
	
	    // Create a new Server Connector
		ServerConnector connector = new ServerConnector(user);;
	
	    // Connect to the server
	    System.out.println("Connecting to server...");
	    String connectedPeer = connector.connect(connectionId);

	    if(connectedPeer == null) {
			System.out.println("Error connecting to server. Please try again");
			System.exit(1);
			return;
		}

		// Create two new threads - one for sending and one for receiving
		SenderThread S1 = new SenderThread(user);
		ReceiverThread R1 = new ReceiverThread(user, connectedPeer);

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
