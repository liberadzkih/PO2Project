package User;

import User.gui.UserController;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Requester implements Runnable {
    private UserController userController;
    private Socket socket;
    private OperationData operationData;
    private User user;

    public Requester(User user, UserController userController, OperationData operationData) {
        this.user = user;
        this.userController = userController;
        this.socket = new Socket();
        this.operationData = operationData;
    }

    @Override
    public void run() {
        try {
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 5000);
        socket.connect(inetSocketAddress, 1000);

        OutputStream output = socket.getOutputStream();
        InputStream input = null;

        switch (operationData.getOperation()) {
            case GET_FILES: {
                input = socket.getInputStream();
                sendRequestForFiles(output);
                getFilesFromServer();
                break;
            }
            case INSERT_FILES: {
                System.out.println("insert file");
                input = socket.getInputStream();
                sendRequestForSave(output);
                sendFilesToServer();
                userController.changeInfoLog("Wysyłanie pliku/plików na serwer");
                break;
            }
            case SHARE_FILE: {
                System.out.println("share file");
                input = socket.getInputStream();
                sendRequestForSave(output);
                sendOneFileToServer();
                userController.changeInfoLog("Wysyłanie pliku do użytkownika");
                break;
            }
            case DELETE_FILE: {
                System.out.println("delete file");
                input = socket.getInputStream();
                sendRequestForDeleteFile(output);
                isFileDeleted(input);
                userController.changeInfoLog("Usuwanie pliku");
                break;
            }
        }
        output.close();
        input.close();
        socket.close();
    }

    private void sendRequestForFiles(OutputStream output) throws IOException {
        byte[] request = ("getfiles" + "&" + operationData.getUserName() + "\n").getBytes();
        System.out.println("wysłano request o treści: " + new String(request));
        output.write(request, 0, request.length);
    }

    private void sendRequestForSave(OutputStream output) throws IOException {
        byte[] request = ("save" + "&" + operationData.getTargetUser() + "\n").getBytes();
        System.out.println("wysłano request o treści: " + new String(request));
        output.write(request, 0, request.length);
    }

    private void getFilesFromServer() throws IOException {
        List<String> fileList = new ArrayList<>();
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);
        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        for (int i = 0; i < filesCount; i++) {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            files[i] = new File(user.getUserPath().resolve(fileName).toString());
            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            for (int j = 0; j < fileLength; j++)
                bos.write(bis.read());
            bos.close();
            fileList.add(user.getUserPath().resolve(fileName).toString());
        }
        dis.close();
        user.setUserFilesFromServer(fileList);
    }

    public void sendRequestForDeleteFile(OutputStream output) throws IOException {
        String fileName = operationData.getFilePath().getFileName().toString();
        byte[] request = ("delete" + "&" + operationData.getUserName() + "&" + fileName + "\n").getBytes();
        System.out.println("wysłano request o treści: " + new String(request));
        output.write(request, 0, request.length);
    }

    private void isFileDeleted(InputStream input) {
        Scanner scanner = new Scanner(input);
        List<String> list = Arrays.stream(scanner.nextLine().split("&")).collect(Collectors.toList());
        if (list.get(0).trim().equals("delete")) {
            if (list.get(2).trim().equals("yes") && user.deleteFileFromDirectory(list.get(1).trim())) {
                userController.changeInfoLog("LBL_DELETEDFILE" + list.get(1).trim());
                user.loadUserFilesFromDirectory();
            } else {
                //cannot delete file alert
            }
        }
    }

    private void sendOneFileToServer() throws IOException {
        String path = operationData.getFilePath().toString();
        File file = new File(path);
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(1);

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
        dos.close();
    }

    private void sendFilesToServer() throws IOException {
        List<String> filesPaths = operationData.filesList;
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(filesPaths.size());

        for (String s : filesPaths) {
            File file = new File(s);
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
}
