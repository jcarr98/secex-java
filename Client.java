import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String ip = "127.0.0.1";
    private final int port = 8008;
    private final String name;
    
    public Client(String display_name, int connection_id) {
        name = display_name;
    }

    public void connect(int id) {
        Socket sock;
        try {
            sock = new Socket(this.ip, this.port);
        }
        catch(IOException e) {
            System.out.println("Error connecting to server");
            return;
        }

        // Create two new threads - one for sending and one for receiving
        SenderThread S1 = new SenderThread(sock);
        ReceiverThread R1 = new ReceiverThread(sock);

        S1.start();
        R1.start();

        try{
            S1.join();
            R1.join();
            System.out.println("Threads closed");
        }
        catch(InterruptedException e) {
            System.out.println("Error joining threads");
        }

        return;
    }
}
