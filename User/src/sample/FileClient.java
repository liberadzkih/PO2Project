package sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        byte[] bytes = new byte[20000];
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\users_files\\user1\\stars.png");
        inputStream.read(bytes, 0, bytes.length);
        fileOutputStream.write(bytes, 0, bytes.length);
    }
}
