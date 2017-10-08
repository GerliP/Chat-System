import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by gerli on 26/09/2017.
 */
public class ServerThread extends Thread {

    private Socket socket;
    private String username;
    private Scanner scanner;
    private PrintWriter output;

    public Socket getSocket() {
        return this.socket;
    }

    public String getUsername() {
        return this.username;
    }

    ServerThread(Socket socket, String username) {

        this.socket = socket;
        this.username = username;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {

        }
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ServerThread) {
            String username = ((ServerThread) o).getUsername();
            if (username != null && username.equals(this.getUsername())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public void run() {
        String message;
        Set<ServerThread> threadList = Server.getThreadList();
        System.out.println(threadList.size() + threadList.toString());
        do {
            int length = 4 + username.length();
            message = scanner.nextLine();
            if (message.contains("IMAV")) {
                System.out.println(message);
            } else {
                String count = message.substring(length);
                if (!(count.length() > 250)) {
                    //for(int i =0; i<threadList.size(); i++){
                    //System.out.println(threadList.get(i));
                    System.out.println(message);
                    for (ServerThread t : threadList) {
                        try {
                            // System.out.println(t);
                            output = new PrintWriter(t.getSocket().getOutputStream(), true);
                            output.println(message);
                        } catch (IOException ioEx) {
                        }


                    }
                } else {
                    output.println("Your message is longer than 250 characters");
                }
            }
        } while (!message.contains("QUIT"));
        ServerThread thread = new ServerThread(socket, username);
        threadList.remove(thread);
        System.out.println(threadList.toString());
        try {
            System.out.println(username + " has left the chat");
            output.println("QUIT " + username + " has left the chat");

            System.out.println("Active list: " + threadList.toString().substring(1, (threadList.toString()).length() - 1));
            for (ServerThread t : threadList) {

                PrintWriter msg = new PrintWriter(t.getSocket().getOutputStream(), true);
                msg.println("LIST " + threadList.toString().substring(1, (threadList.toString()).length() - 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return username;
    }
}