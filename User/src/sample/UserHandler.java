package sample;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserHandler {
    public User user;
    public UserGuiController userGuiController;
    private boolean appStopped = false;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private List<String> server_filePaths, server_fileNames;

    public UserHandler(User user, UserGuiController userGuiController) {
        this.user = user;
        this.userGuiController = userGuiController;
        this.reloadFiles();
        this.loadFilesFromServerDirectory();
    }

    private void reloadFiles() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (!appStopped) {
                    try {
                        System.out.println("Files loaded");
                        //user.loadFilesFromUserDirectory();
                        userGuiController.updateFilesListView();
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadFilesFromServerDirectory() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (!appStopped) {
                    try {
                        String serverPath = "C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files\\";
                        Stream<Path> walk = Files.walk(Paths.get(serverPath + user.getUsername()));

                        List<String> result = walk.filter(Files::isRegularFile)
                                .map(x -> x.toString()).collect(Collectors.toList());

                        server_filePaths = new ArrayList<>();
                        server_fileNames = new ArrayList<>();
                        String csvFile = user.getUsername() + ".csv";
                        for (String s : result) {
                            if (!Paths.get(s).getFileName().toString().equals(csvFile)) {
                                server_filePaths.add(s);
                                server_fileNames.add(Paths.get(s).getFileName().toString());
                            }
                        }
                        if (user.getFile_names() != server_fileNames) {
                            user.setFile_paths(server_filePaths);
                            user.setFile_names(server_fileNames);
                        }
                        System.out.println("Files loaded from server_directory");
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
