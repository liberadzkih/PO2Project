package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserGuiController {
    private final Stage thisStage;

    @FXML
    private ComboBox<String> users_combobox;
    String[] users = {"user1", "user2", "user3", "user4", "user5"};

    @FXML
    private Button share_button, en_button, pl_button, gr_button, addNewFileButton, deleteSelectedFileButton, refreshButton;

    @FXML
    private Label user_name, user_path, txt1, txt2, txt3;

    @FXML
    private ListView files_list;
    private List<String> file_paths;
    private List<String> file_names;

    @FXML
    private Label current_status;

    private String current_language;
    private final String[] languages = {"en", "pl", "gr"};

    public UserGuiController(String user_name, String user_path) {
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_gui.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 600, 400));
            thisStage.setTitle(user_name);
            this.user_name.setText(user_name);
            this.user_path.setText(user_path);
            this.users_combobox.getItems().addAll(users);
            this.users_combobox.getSelectionModel().selectFirst();
            loadFiles(user_path);
            this.files_list.getItems().addAll(file_names);
            setLanguagePolish();
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

        } catch (NoSuchFileException e) {
            errorAlert("Błąd!", "Podana ścieżka jest niepoprawna!");
            System.exit(0);
        } catch (IOException e) {
            errorAlert("Błąd!", "Coś poszło nie tak!");
            System.exit(0);
        }
    }

    /**
     * Just error alert window
     *
     * @param title  - title of window
     * @param header - text displayed in popup window
     */
    public void errorAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    /**
     * Updates GUI strings depending on current_language value
     */
    private void updateGuiToCurrentLanguage() {
        try {
            File file = new File("src/sample/language_files/" + this.current_language + "_gui.txt");
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            System.out.println("Language set to: " + this.current_language);
            this.txt1.setText(lines.get(0));
            this.txt2.setText(lines.get(1));
            this.txt3.setText(lines.get(2));
            this.share_button.setText(lines.get(3));
            this.current_status.setText(lines.get(4));
            this.addNewFileButton.setText(lines.get(7));
            this.deleteSelectedFileButton.setText(lines.get(8));
            this.refreshButton.setText(lines.get(9));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setLanguageEnglish() {
        this.current_language = this.languages[0];
        updateGuiToCurrentLanguage();
    }

    @FXML
    public void setLanguageGerman() {
        this.current_language = this.languages[2];
        updateGuiToCurrentLanguage();
    }

    @FXML
    public void setLanguagePolish() {
        this.current_language = this.languages[1];
        updateGuiToCurrentLanguage();
    }

    @FXML
    public void refreshFiles() {
        loadFiles(user_path.getText());
        this.files_list.getItems().clear();
        this.files_list.getItems().addAll(file_names);
    }

    @FXML
    public void deleteSelectedFile() {
        try {
            String fileToDelete = this.files_list.getSelectionModel().getSelectedItem().toString();
            String pathToDelete = this.user_path.getText() + "\\" + fileToDelete;
            Files.deleteIfExists(Paths.get(pathToDelete));
            refreshFiles();
        } catch (NullPointerException e) {
            errorAlert("Błąd", "Nie wybrano pliku");
        } catch (IOException e) {
            errorAlert("Błąd", "Nie można usunąć tego pliku");
        }
    }

}
