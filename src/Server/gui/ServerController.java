package Server.gui;

import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JavaFX controller dla GUI serwera
 */
public class ServerController {
    private final Stage thisStage;
    private final String path = "C:\\Users\\Hlibe\\OneDrive\\Pulpit\\po2_project_files\\server_files";
    private final String[] user_names = {"user1", "user2", "user3", "user4", "user5"};
    private List<String> file_paths;
    private List<String> file_names;
    private ArrayList<String> fileList1 = new ArrayList<>();
    private ArrayList<String> fileList2 = new ArrayList<>();
    private ArrayList<String> fileList3 = new ArrayList<>();
    private ArrayList<String> fileList4 = new ArrayList<>();
    private ArrayList<String> fileList5 = new ArrayList<>();
    private Path serverPath;

    @FXML
    private ListView list1, list2, list3, list4, list5, logs_list;

    @FXML
    private HBox horizontalBox;

    public ServerController() {
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server_gui.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 950, 400));
            thisStage.setTitle("Server");
            thisStage.setOnCloseRequest(event -> System.exit(0));

            serverPath = Paths.get(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda wyświetlająca gui
     */
    public void showStage() {
        thisStage.show();
    }

    /**
     * Odświeżanie zawartości katalogów serwerowych
     *
     * @throws IOException
     */
    public void refresh() throws IOException {
        listFiles(serverPath, 1, fileList1);
        listFiles(serverPath, 2, fileList2);
        listFiles(serverPath, 3, fileList3);
        listFiles(serverPath, 4, fileList4);
        listFiles(serverPath, 5, fileList5);
        Platform.runLater(() -> fillGuiLists());
    }

    /**
     * Listuje pliki dla podanego użytkownika
     *
     * @param serverPath - scieżka do katalogu serwerowego
     * @param number     - numer użytkownika/numer listy(list view)
     * @param list       - lista przechowywująca nazwy plików dla danego usera
     */
    private void listFiles(Path serverPath, int number, ArrayList<String> list) {
        list.clear();
        try (Stream<Path> walk = Files.walk(serverPath.resolve("user" + number))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            for (String s : result) {
                list.add(Paths.get(s).getFileName().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Czyszczenie poprzedniej zawartości i uzupełnianie listy plików w serwerowym katalogu użytkownika
     */
    public void fillGuiLists() {
        list1.getItems().clear();
        list1.getItems().addAll(fileList1);
        list2.getItems().clear();
        list2.getItems().addAll(fileList2);
        list3.getItems().clear();
        list3.getItems().addAll(fileList3);
        list4.getItems().clear();
        list4.getItems().addAll(fileList4);
        list5.getItems().clear();
        list5.getItems().addAll(fileList5);
    }

    /**
     * Funkcja dodająca wiadomość do logów serwera
     *
     * @param text
     */
    public void addToLogs(String text) {
        Platform.runLater(() -> {
            int logsSize = logs_list.getItems().size();
            if (logsSize > 9)
                logs_list.getItems().remove(1);
            logs_list.getItems().add(text);
        });
    }
}
