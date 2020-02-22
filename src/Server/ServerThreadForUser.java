package Server;

import Server.gui.ServerController;
import User.OperationData;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerThreadForUser implements Runnable {
    private Socket socket;
    private OperationData operationData;
    private ServerController serverController;
    Path serverPath = Paths.get("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files\\");

    public ServerThreadForUser(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
    }

    @Override
    public void run() {
        String info = getInfoFromUserRequest();
        String[] infoString = info.split("&");
        if (infoString[0].trim().equals("getfiles")) {
            try {
                System.out.println("send files to user: " + infoString[1]);
                sendFilesToUser(infoString[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (infoString[0].trim().equals("delete")) {
            try {
                if (deleteFile(infoString[1], infoString[2])) {
                    System.out.println("deleting file in directory " + infoString[1]);
                    byte[] delete = ("delete&" + infoString[2] + "&" + "yes" + "\n").getBytes();
                    socket.getOutputStream().write(delete, 0, delete.length);
                    serverController.addToLogs("Użytkownik " + infoString[1] + " usunął plik " + infoString[2]);
                } else {
                    byte[] delete = ("delete&" + infoString[2] + "&" + "no" + "\n").getBytes();
                    socket.getOutputStream().write(delete, 0, delete.length);
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (infoString[0].trim().equals("save")) {
            try {
                getFilesFromUser(infoString[1]);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private String getInfoFromUserRequest() {
        byte[] charFromInfo = new byte[1];
        byte[] info = new byte[1024];
        int i = 0;
        try {
            while (socket.getInputStream().read(charFromInfo) > 0 & charFromInfo[0] != '\n') {
                info[i] = charFromInfo[0];
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(info);
    }

    private void sendFilesToUser(String username) throws IOException {
        String directory = serverPath.resolve(username.trim()).toString();
        File[] files = new File(directory).listFiles();
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(files.length);

        for (File file : files) {
            long length = file.length();
            dos.writeLong(length);
            String name = file.getName();
            dos.writeUTF(name);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            int theByte = 0;
            while ((theByte = bis.read()) != -1)
                bos.write(theByte);
            bis.close();
        }
        dos.close();
    }

    private boolean deleteFile(String username, String filename) {
        String filePath = serverPath.resolve(username.trim()).resolve(filename.trim()).toString();
        File file = new File(filePath);
        return file.delete();
    }

    private void getFilesFromUser(String username) throws IOException, InterruptedException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);
        int filesCount = dis.readInt();
        File[] files = new File[filesCount];
        if (filesCount == 1) {
            for (int i = 0; i < filesCount; i++) {
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();
                files[i] = new File(serverPath.resolve(username.trim()).resolve(fileName).toString());
                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                for (int j = 0; j < fileLength; j++)
                    bos.write(bis.read());
                bos.close();
                serverController.addToLogs("Użytkownik " + username + " dodał plik " + fileName);
            }
            dis.close();
        } else {
            for (int i = 0; i < filesCount; i++) {
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();
                files[i] = new File(serverPath.resolve(username.trim()).resolve(fileName).toString());
                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                for (int j = 0; j < fileLength; j++)
                    bos.write(bis.read());
                bos.close();
                serverController.addToLogs("Użytkownik " + username + " dodał plik " + fileName);
                Thread.sleep(2000);
            }
        }
    }
}
