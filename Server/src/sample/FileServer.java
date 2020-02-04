package sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files\\user1\\stars.png");
        byte[] bytes = new byte[20000];
        fileInputStream.read(bytes, 0, bytes.length);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes, 0, bytes.length);

    }
}
