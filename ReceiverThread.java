import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

public class ReceiverThread extends Thread {
    private Socket sock;
    private Thread t;

    public ReceiverThread(Socket sock) {
        this.sock = sock;
    }

    public void start() {
        System.out.println("Starting receiver thread");

        if(t == null) {
            t = new Thread(this, "receiver");
            t.start();
        }
    }

    public void run() {
        System.out.println("Running receiver thread");

        Scanner input;
        try {
            input = new Scanner(sock.getInputStream());
        }
        catch(IOException e) {
            System.out.println("Error creating sender thread input");
            return;
        }

        while(input.hasNextLine()) {
            String line = input.nextLine();
            System.out.println(line);
        }

        input.close();

        return;
    }
}
