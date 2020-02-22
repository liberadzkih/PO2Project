package Server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

public class Queuer {
    PriorityBlockingQueue<OperationData> queue;
    private Map<String, Integer> userPriority;
    private Path serverPath;

    public Queuer() {
        userPriority = new HashMap<>();
        serverPath = Paths.get("C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files");
        queue = new PriorityBlockingQueue<>();
    }
    
}
