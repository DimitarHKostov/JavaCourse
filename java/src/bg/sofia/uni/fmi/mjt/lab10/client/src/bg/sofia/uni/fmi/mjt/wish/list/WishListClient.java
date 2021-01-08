package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WishListClient {
    private static final int SERVER_PORT = 4444;
    private static final String DISCONNECT_COMMAND = "disconnect";

    public void start() {
        try (Socket socket = new Socket("localhost", SERVER_PORT)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String message = scanner.nextLine();

                writer.println(message);

                String reply = reader.readLine();
                System.out.println(reply);

                if (message.equals(DISCONNECT_COMMAND.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WishListClient client = new WishListClient();
        client.start();
    }
}