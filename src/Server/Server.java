package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server implements Runnable {
    private ServerSocket serverSocket = new ServerSocket();
    private Path path;

    public Server() throws IOException {
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 5000));
        path = Paths.get("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files");
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerThreadForUser(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
