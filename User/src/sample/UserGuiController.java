package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class UserGuiController {
    private final Stage thisStage;
    private User user;
    @FXML
    private ComboBox<String> users_combobox;
    @FXML
    private Button share_button, en_button, pl_button, gr_button, addNewFile_button, deleteSelectedFile_button, refresh_button;
    @FXML
    private Label user_name_label, user_path_label, txt1_label, txt2_label, txt3_label;
    @FXML
    private ListView files_list_listView;
    @FXML
    private Label current_status_label;

    public UserGuiController(User user) {
        this.user = user;
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_gui.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 600, 400));
            thisStage.setTitle(this.user.getUsername());
            user_name_label.setText(this.user.getUsername());
            user_path_label.setText(this.user.getDirectoryPath());
            String[] users = {"user1", "user2", "user3", "user4", "user5"};
            users_combobox.getItems().addAll(users);
            users_combobox.getSelectionModel().selectFirst();
            loadFiles();
            files_list_listView.getItems().addAll(this.user.getFile_names());
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
     */
    public void loadFiles() {
        try {
            user.loadFilesFromUserDirectory();
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
            File file = new File("src/sample/language_files/" + this.user.getLanguage() + "_gui.txt");
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            System.out.println("Language set to: " + this.user.getLanguage());
            txt1_label.setText(lines.get(0));
            txt2_label.setText(lines.get(1));
            txt3_label.setText(lines.get(2));
            share_button.setText(lines.get(3));
            current_status_label.setText(lines.get(4));
            addNewFile_button.setText(lines.get(7));
            deleteSelectedFile_button.setText(lines.get(8));
            refresh_button.setText(lines.get(9));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setLanguageEnglish() {
        this.user.setLanguage("en");
        updateGuiToCurrentLanguage();
    }

    @FXML
    public void setLanguageGerman() {
        this.user.setLanguage("gr");
        updateGuiToCurrentLanguage();
    }

    @FXML
    public void setLanguagePolish() {
        this.user.setLanguage("pl");
        updateGuiToCurrentLanguage();
    }

    @FXML
    public void refreshFiles() {
        loadFiles();
        files_list_listView.getItems().clear();
        files_list_listView.getItems().addAll(this.user.getFile_names());
    }

    @FXML
    public void deleteSelectedFile() {
        try {
            user.deleteFile(this.files_list_listView.getSelectionModel().getSelectedItem().toString());
            refreshFiles();
        } catch (NullPointerException e) {
            errorAlert("Błąd", "Nie wybrano pliku");
        } catch (IOException e) {
            errorAlert("Błąd", "Nie można usunąć tego pliku");
        }
    }

    @FXML
    public void chooseFile() throws IOException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik");
            File file = fileChooser.showOpenDialog(thisStage);
            Path sourceFile = file.toPath();
            Path destFile = Paths.get(user.getDirectoryPath() + "\\" + file.getName());
            Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
            refreshFiles();
        } catch (NullPointerException e) {
            errorAlert("Błąd", "Nie wybrano pliku");
        }
    }

}
