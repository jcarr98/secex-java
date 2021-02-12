import java.util.Scanner;

public class SecureClient {
    public static void main(String[] args) {
        System.out.println("Running the Secure Client...");

        if(args.length > 0 && args[0].equals("help")) {
            System.out.println("To run the program, type 'java SecureClient {connection id}.");
            System.out.println("A connection Id can be any number. It should match the connection ID of the peer you are trying to connect to.");
            System.out.println("Your connection ID will expire after 60 seconds. If no connection is made within that time the program will shut down.");
        }

        // Get display name
        String name;
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("Please enter a display name");
            name = input.nextLine();

            if(name.replaceAll("\\s", "").length() == 0) {
                System.out.println("Please enter a valid display name. Must be at least 1 character");
                continue;
            } else {
                break;
            }
        }

        // Get connection id
        int connectionId;
        while(true) {
            System.out.println("Please enter a connection id");
            try {
                connectionId = Integer.parseInt(input.nextLine());
            }
            catch(NumberFormatException e) {
                System.out.println("Connection ID must be an integer.");
                continue;
            }

            break;
        }

        // Create a client
        Client client;
        try{
            client = new Client(name, connectionId);
        }
        catch(NumberFormatException e) {
            System.out.println("The only acceptable arguments are a connection ID or 'help'");
            return;
        }

        // Connect to the server
        System.out.println("Connecting to server...");
        client.connect(connectionId);
    }
}
