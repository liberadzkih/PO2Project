package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ServerGuiController {
    private final Stage thisStage;
    private final String path = "C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files\\";
    private final String[] user_names = {"user1", "user2", "user3", "user4", "user5"};
    private List<UserInfo> usersInfo = new ArrayList<>();
    private List<String> file_paths;
    private List<String> file_names;

    @FXML
    private ListView list1, list2, list3, list4, list5, logs_list;

    @FXML
    private HBox horizontalBox;

    public ServerGuiController() {
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server_gui.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 600, 400));
            thisStage.setTitle("Server");
            initUsers();
            initRandomLogs();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the stage that was loaded in the constructor
     */
    public void showStage() {
        thisStage.showAndWait();
    }

    /**
     * Loading files from user directory path
     * If path is wrong displays Error Alert window
     *
     * @param _path - user directory path
     */
    public void loadFiles(String _path) {
        try (Stream<Path> walk = Files.walk(Paths.get(_path))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            file_paths = new ArrayList<>();
            file_names = new ArrayList<>();
            for (String s : result) {
                file_paths.add(s);
                file_names.add(Paths.get(s).getFileName().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUsers() {
        for (int i = 0; i < user_names.length; i++) {
            String username = user_names[i];
            loadFiles(this.path + username);
            ListView listView = (ListView) horizontalBox.getChildren().get(i);
            UserInfo uf = new UserInfo(username, this.path + username, file_paths, file_names, listView);
            usersInfo.add(uf);
        }
        usersInfo.forEach(UserInfo::fillListView);
    }

    private void initRandomLogs() {
        String[] logs = {"Przesyłanie pliku od user1 do user4...",
                "Pobieranie pliku od użytkownika user3...",
                "Aktualizowanie zawartosci folderów...",
                "Przesyłanie pliku od user5 do user2...",
                "Przesyłanie pliku od user1 do user4...",
                "Pobieranie pliku od użytkownika user3...",
                "Aktualizowanie zawartosci folderów...",
                "Przesyłanie pliku od user5 do user2..."};
        for (String s : logs) {
            String time = "(" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ")";
            logs_list.getItems().add(time + s);
        }
    }

}
