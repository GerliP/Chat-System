import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by gerli on 01/10/2017.
 */
public class ClientThread extends Thread {

    private Socket socket;
    private String username;
    private Scanner input;
    private PrintWriter output;

    public ClientThread(Socket socket, String username) {

        this.socket = socket;
        this.username = username;

        try {
            input = new Scanner(System.in);
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ioEx) {

        }
    }

    @Override
    public void run() {
        String message;
        Set<ServerThread> threadList = Server.getThreadList();
        do {
            message = "DATA " + username + ": " + input.nextLine();
            output.println(message);

        } while (!message.contains("QUIT"));

        output.close();
        input.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println("QUIT");
        //  try {
        //     socket.close();
        //  } catch (IOException e) {
        //    e.printStackTrace();
        //}
    }
}
