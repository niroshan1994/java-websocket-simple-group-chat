package websocket;

import org.glassfish.tyrus.server.Server;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8025, "/websockets", new HashMap<>(), HelloWebsocketServer.class);

        try {
            server.start();
            System.out.println("Press any key to stop the server...");
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
