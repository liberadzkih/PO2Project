package Server;

import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OperationData implements Comparable<OperationData> {
    @Override
    public int compareTo(OperationData o) {
        return this.filesCount - o.getFilesCount();
    }

    enum Operation {DELETE, INSERT, SAVE}

    private List<String> files;
    private String user;
    private String targetUser;
    private Path serverDirectoryPath;
    private Operation operation;
    private int filesCount;
    private Socket socket;
    public boolean isOperationDone;

    public OperationData(String user, String targetUser, Socket socket) {
        this.user = user;
        serverDirectoryPath = Paths.get("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files");
        this.targetUser = targetUser;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Path getServerDirectoryPath() {
        return serverDirectoryPath;
    }

    public void setServerDirectoryPath(Path serverDirectoryPath) {
        this.serverDirectoryPath = serverDirectoryPath;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }
}
