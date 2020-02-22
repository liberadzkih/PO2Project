package User;

import java.nio.file.Path;
import java.util.List;

public class OperationData {
    enum Operation {DELETE_FILE, INSERT_FILES, GET_FILES, SHARE_FILE}

    private String userName;
    private Path filePath;
    private String targetUser;
    private Operation operation;
    public List<String> filesList;

    public OperationData(String userName, Path filePath, String targetUser) {
        this.userName = userName;
        this.filePath = filePath;
        this.targetUser = targetUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
