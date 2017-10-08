import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by gerli on 02/10/2017.
 */
public class ClientThreadRec extends Thread {

    Socket socket;
    String username;

    public ClientThreadRec(Socket socket, String username) {

        this.socket = socket;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            Scanner server = new Scanner(socket.getInputStream());
            while (!socket.isClosed()) {
                if (server.hasNextLine()) {
                    String message = server.nextLine();
                    if (message.contains("LIST")) {
                        System.out.println(message);
                    } else {
                        String datamsg = message.substring(5);
                        System.out.println(datamsg);
                    }
                }
            }
            System.out.println("QUIT");
            socket.close();
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



