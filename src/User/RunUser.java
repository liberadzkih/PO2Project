package User;

import User.gui.UserController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class RunUser extends Application {

    private String username, userpath;

    public static void main(String[] args) {
        launch(args);
    }

    void setUserParameters(Application.Parameters parameters) {
        this.userpath = parameters.getRaw().get(1);
        this.username = parameters.getRaw().get(0);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        setUserParameters(getParameters());
        User user;
        UserController userController;
        if (username == null && userpath == null) {
            System.exit(0);
        }

        userController = new UserController();
        user = new User(Paths.get(userpath), username, userController);
        userController.setUser(user);
        userController.showStage();
        Thread thread1, thread2;

        //watek odswiezajacy zawartość katalogu serwerowego
        thread2 = new Thread(() -> {
            try {
                while (user.isReadyForNextOperations = true) {
                    user.getFilesFromServer();
                    userController.changeInfoLog("LBL_DOWNLOADFROMSERVER");
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread2.start();

        //watek odswiezajacy zawartosc lokalnego katalogu usera
        thread1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                while (true) {
                    userController.changeInfoLog("LBL_LOADUSERDIRECTORY");
                    user.loadUserFilesFromDirectory();
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread1.start();

    }
}
