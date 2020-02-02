package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ServerGuiController serverGuiController = new ServerGuiController();
        serverGuiController.showStage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
