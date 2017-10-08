import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by gerli on 25/09/2017.
 */
public class Server {

    private static final int PORT = 4545;
    private static volatile Set<ServerThread> threadList;

    public static Set<ServerThread> getThreadList() {
        return threadList;
    }

    public static void main(String[] args) throws IOException {
        new Server().runServer();
    }

    private void runServer() throws IOException {
        System.out.println("Server is up and running...");
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = null;
        threadList = new HashSet<>();
        while (true) {
            String join = "";
            String joinRequest = "";
            while (!(checkUsername(join)).equals("J_OK")) {
                socket = serverSocket.accept();
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                Scanner input = new Scanner(socket.getInputStream());
                joinRequest = input.nextLine();
                if (!"JOIN".equals(joinRequest.substring(0, 4))) {
                    output.println("J_ER 400: Unknown command!");
                    System.out.println("Invalid command: " + joinRequest);
                }
                int index = joinRequest.indexOf(',');
                join = joinRequest.substring(5, index);
                output.println(checkUsername(join));
                if (checkUsername(join).contains("J_ER")) {
                    socket.close();

                }
            }

            //String accept = "J_OK";
            System.out.println(joinRequest);
            //  for (ServerThread t1 : threadList) {
            // System.out.println(t);
            //       PrintWriter msg = new PrintWriter(t1.getSocket().getOutputStream(), true);
            //     msg.println(join + " has entered the chat");
            // System.out.println(message);

            // output.println(accept);
            ServerThread t1 = new ServerThread(socket, join);
            threadList.add(t1);
            t1.start();
            System.out.println("Active list: " + threadList.toString().substring(1, (threadList.toString()).length() - 1));
            for (ServerThread t : threadList) {
                PrintWriter msg = new PrintWriter(t.getSocket().getOutputStream(), true);
                msg.println("LIST " + threadList.toString().substring(1, (threadList.toString()).length() - 1));
                //output.println("LIST " + threadList.toString().substring(1, (threadList.toString()).length() - 1));
            }
        }
    }

    //while(true);
                    /*Scanner scanner = new Scanner(System.in);
                    String message = scanner.nextLine();
                    System.out.println(message);
                    while(!message.equals("***CLOSE***")){
                       output.println("Server: " + message);
                        message = scanner.nextLine();
                    }
            output.println(message);
            */

    private String checkUsername(String join) {
        String result;
        CheckUsername check = new CheckUsername();

        if (!check.validate(join)) {
            result = "J_ER 406: Invalid username! Start Over!";

            return result;
        } else if (!threadList.isEmpty() && !check.unique(join)) {
            result = "J_ER 406: Duplicate username! Try again!";
            return result;

        } else {
            result = "J_OK";
            return result;
        }
    }
}



