package lk.playtech.chatApp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * author  Yasith C Bandara
 * created 8/16/2022 - 10:45 AM
 * project Playtech
 */

public class Server {
    private static final ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5001);
        System.out.println("----------Waiting for Client----------");
        Socket socket;
        while (true) {
            socket = serverSocket.accept();
            System.out.println("A new client connected");
            ClientHandler clientThread = new ClientHandler(socket, clients);
            clients.add(clientThread);
            clientThread.start();
        }
    }
}
