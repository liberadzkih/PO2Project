package sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserHandler {
    public User user;
    public UserGuiController userGuiController;
    private boolean appStopped = false;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public UserHandler(User user, UserGuiController userGuiController) {
        this.user = user;
        this.userGuiController = userGuiController;
        this.reloadFiles();
    }

    private void reloadFiles(){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (!appStopped) {
                    try {
                        System.out.println("Files loaded");
                        user.loadFilesFromUserDirectory();
                        userGuiController.updateFilesListView();
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
