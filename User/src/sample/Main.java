package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private String username, userpath;

    @Override
    public void start(Stage primaryStage) {
        setUserParameters(getParameters());
        User user = new User(username, userpath);
        UserGuiController userGuiController = new UserGuiController(user);
        UserHandler userHandler = new UserHandler(user, userGuiController);
        userGuiController.showStage();
    }

    // java Main.java user1 C:\Users\Hlibe\Desktop\directory
    public static void main(String[] args) {
        launch(args);
    }

    void setUserParameters(Parameters parameters) {
        this.userpath = parameters.getRaw().get(1);
        this.username = parameters.getRaw().get(0);
    }
}