package Server;

import Server.gui.ServerController;
import User.OperationData;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Klasa zawierająca Wątek dla Usera
 */
public class ServerThreadForUser implements Runnable {
    private Socket socket;
    private OperationData operationData;
    private ServerController serverController;
    Path serverPath = Paths.get("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files\\");
    Lock lock = new ReentrantLock();

    public ServerThreadForUser(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
    }

    /**
     * Metoda run() przyjmująca request użytkownika
     */
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

    /**
     * Wysyłanie plików do użytkownika(z wyjątkiem pliku user.txt)
     *
     * @param username
     * @throws IOException
     */
    private void sendFilesToUser(String username) throws IOException {
        String directory = serverPath.resolve(username.trim()).toString();
        File[] files = new File(directory).listFiles();
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(files.length - 1);

        for (File file : files) {
            if (!file.getName().equals(username.trim() + ".txt")) {
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
                userTxtFileInsertFileName(username, file);
            }
        }
        dos.close();
    }

    /**
     * Funkcja usuwająca plik z katalogu serwerowego
     *
     * @param username
     * @param filename
     * @return
     */
    private boolean deleteFile(String username, String filename) {
        String filePath = serverPath.resolve(username.trim()).resolve(filename.trim()).toString();
        File file = new File(filePath);
        userTxtFileRemoveFileName(username, file);
        return file.delete();
    }

    /**
     * Funkcja przyjmująca pliki od użytkownika
     *
     * @param username
     * @throws IOException
     * @throws InterruptedException
     */
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

    /**
     * Funkcja umieszczająca nazwy plikow z katalogu w pliku user.txt
     *
     * @param username
     * @param file
     * @throws IOException
     */
    private void userTxtFileInsertFileName(String username, File file) throws IOException {
        lock.lock();
        try {
            String userTxtFilePath = serverPath.resolve(username.trim()).resolve(username.trim() + ".txt").toString();
            BufferedReader in = new BufferedReader(new FileReader(userTxtFilePath));
            String str;
            List<String> list = new ArrayList<>();
            while ((str = in.readLine()) != null)
                list.add(str);
            for (String line : list)
                if (line.trim().equals(file.getName().trim()))
                    return;
            Writer output = new BufferedWriter(new FileWriter(userTxtFilePath, true));
            output.append(file.getName() + "\n");
            output.close();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Funkcja usuwająca nazwy plików z pliku user.txt
     *
     * @param username
     * @param file
     */
    private void userTxtFileRemoveFileName(String username, File file) {
        lock.lock();
        try {
            String userTxtFilePath = serverPath.resolve(username.trim()).resolve(username.trim() + ".txt").toString();
            Scanner sc = new Scanner(new File(userTxtFilePath));
            StringBuffer buffer = new StringBuffer();
            while (sc.hasNextLine()) {
                buffer.append(sc.nextLine() + System.lineSeparator());
            }
            String fileContents = buffer.toString();
            sc.close();
            fileContents = fileContents.replaceAll(file.getName(), "");
            FileWriter writer = new FileWriter(userTxtFilePath);
            writer.append(fileContents);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
