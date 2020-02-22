package Server;

import Server.gui.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerRun extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //new Thread(new Server()).start();
            ServerController serverController = new ServerController();
            serverController.showStage();
            new Thread(new Server(serverController)).start();
            new Thread(() -> {
                try {
                    while (true) {
                        serverController.refresh();
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
