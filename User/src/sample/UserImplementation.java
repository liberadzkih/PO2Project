package sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserImplementation {
    public User user;
    public UserGuiController userGuiController;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public UserImplementation(User user, UserGuiController userGuiController) {
        this.user = user;
        this.userGuiController = userGuiController;
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        System.out.println("Files loaded");
                        user.loadFilesFromUserDirectory();
                        userGuiController.updateFilesListView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
