import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gerli on 25/09/2017.
 */
public class Client {
    private static String IP;
    private static final int PORT = 4545;
    private static String username;
    private static Socket link = null;


    public Client(String username, Socket link) {
        this.username = username;
        this.link = link;
    }

    public String getUsername() {
        return username;
    }


    public static void main(String[] args) {
        IP = "127.0.0.1";
        accessor();
    }

    private static void accessor() {
        PrintWriter output;
        Scanner input;
        String response = "";
        try {
            while (!response.contains("J_OK")) {
                link = new Socket(IP, PORT);
                System.out.print("Enter username: ");
                Scanner scanner = new Scanner(System.in);
                username = scanner.nextLine();
                output = new PrintWriter(link.getOutputStream(), true);
                output.println("JOIN" + " " + username + ", " + IP + ": " + PORT);
                input = new Scanner(link.getInputStream());
                response = input.nextLine();
                System.out.println(response);
                if (response.contains("J_ER")) {
                    link.close();
                }
            }
            //String list = input.nextLine();
            //System.out.println(list);
            ClientThread t = new ClientThread(link, username);
            ClientThreadRec trec = new ClientThreadRec(link, username);
            t.start();
            trec.start();
            imavMsg();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static void imavMsg() {
        try {
            final Timer timer = new Timer();
            PrintWriter alive = new PrintWriter(link.getOutputStream(), true);
            if (!link.isClosed()) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        alive.println("IMAV " + username);
                    }
                }, 0, 10000);
            } else {
                timer.cancel();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}









