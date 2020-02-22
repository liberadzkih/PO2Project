package User;

import User.gui.UserController;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {
    private Path userPath;
    private String userName;
    private UserController userController;
    private Socket socket;
    private List<String> userFilesFromLocalDirectory;
    private List<String> userFilesFromServer;
    private Thread thread;
    public boolean isReadyForNextOperations = true;
    public boolean isFirstRun = true;

    public User(Path userPath, String userName, UserController userController) throws InterruptedException {
        this.userController = userController;
        this.userPath = userPath;
        this.userName = userName;
        this.socket = new Socket();
        userFilesFromLocalDirectory = new ArrayList<>();
        userFilesFromServer = new ArrayList<>();

        //getFilesFromServer();
        //loadUserFilesFromDirectory();
    }

    public void loadUserFilesFromDirectory() {
        try {
            Stream<Path> walk = Files.walk(userPath);
            List<String> currentFileList = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            userFilesFromLocalDirectory = currentFileList;
            userController.initUserDirectory(userFilesFromLocalDirectory);

            if (userFilesFromLocalDirectory.size() > userFilesFromServer.size()) {
                List<String> filesToInsert = new ArrayList<>();
                filesToInsert.addAll(userFilesFromLocalDirectory);
                Collections.sort(filesToInsert);
                Collections.sort(userFilesFromServer);
                filesToInsert.removeAll(userFilesFromServer);
                filesToInsert.forEach(e -> System.out.println("Should send file: " + e));
                filesToInsert.forEach(e -> {
                    try {
                        sendFileToServer(Paths.get(e).getFileName().toString(), getUserName());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFilesFromServer() throws InterruptedException {
        OperationData operationData = new OperationData(userName, userPath, userName);
        operationData.setOperation(OperationData.Operation.GET_FILES);
        Requester requester = new Requester(this, userController, operationData);
        thread = new Thread(requester);
        thread.start();
        thread.join();
    }

    public void deleteFileFromServer(String fileName) throws InterruptedException {
        OperationData operationData = new OperationData(userName, userPath.resolve(fileName), userName);
        operationData.setOperation(OperationData.Operation.DELETE_FILE);
        Requester requester = new Requester(this, userController, operationData);
        thread = new Thread(requester);
        thread.start();
        thread.join();
    }

    public void sendFilesToServer(List<String> list) throws InterruptedException {
        OperationData operationData = new OperationData(userName, userPath, userName);
        operationData.setOperation(OperationData.Operation.DELETE_FILE);
        Requester requester = new Requester(this, userController, operationData);
        thread = new Thread(requester);
        thread.start();
        thread.join();
    }

    public void sendFileToServer(String fileName, String targetUser) throws InterruptedException {
        OperationData operationData = new OperationData(userName, userPath.resolve(fileName), targetUser);
        operationData.setOperation(OperationData.Operation.INSERT_FILE);
        Requester requester = new Requester(this, userController, operationData);
        thread = new Thread(requester);
        thread.start();
        thread.join();
    }

    public boolean deleteFileFromDirectory(String fileName) {
        File file = new File(userPath.resolve(fileName).toString());
        return file.delete();
    }

    public Path getUserPath() {
        return userPath;
    }

    public void setUserPath(Path userPath) {
        this.userPath = userPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public List<String> getUserFilesFromLocalDirectory() {
        return userFilesFromLocalDirectory;
    }

    public void setUserFilesFromLocalDirectory(List<String> userFiles) {
        this.userFilesFromLocalDirectory = userFiles;
    }

    public List<String> getUserFilesFromServer() {
        return userFilesFromServer;
    }

    public void setUserFilesFromServer(List<String> userFilesFromServer) {
        this.userFilesFromServer = userFilesFromServer;
    }
}
