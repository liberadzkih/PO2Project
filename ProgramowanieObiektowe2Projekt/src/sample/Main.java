package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private String username, userpath;

    @Override
    public void start(Stage primaryStage) {
        setUserParameters(getParameters());
        UserGuiController userGuiController = new UserGuiController(username, userpath);
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