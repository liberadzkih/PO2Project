package Server;

import java.nio.file.Path;
import java.util.List;

public class OperationData implements Comparable<Integer> {
    enum Operation {DELETE, INSERT, SAVE}

    private List<String> files;
    private String user;
    private Path serverDirectoryPath;
    private Operation operation;
    private int filesCount;

    @Override
    public int compareTo(Integer o) {
        return 0;
    }
}
