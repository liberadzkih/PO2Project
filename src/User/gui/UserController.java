package User.gui;

import User.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Klasa odpowiedzialna za GUI użytkownika
 */

public class UserController {
    private final Stage thisStage;
    private User user;
    private Languages languages;
    @FXML
    private ComboBox<String> users_combobox;
    @FXML
    private Button share_button, en_button, pl_button, gr_button, addNewFile_button, deleteSelectedFile_button, refresh_button;
    @FXML
    private Label user_name_label, user_path_label, txt1_label, txt2_label, txt3_label;
    @FXML
    private ListView files_list_listView;
    @FXML
    public Label current_status_label;

    public UserController() {
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_gui.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 500, 300));
            thisStage.setOnCloseRequest(event -> System.exit(0));
            initUserNames();
            languages = new Languages(Languages.Language.PL);
            setLanguagePL();
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
     * Przypisanie Klasy User do kontrolera
     *
     * @param user - obiekt klasy User
     */
    public void setUser(User user) {
        this.user = user;
        thisStage.setTitle(user.getUserName());
        user_name_label.setText(user.getUserName());
        user_path_label.setText(user.getUserPath().toString());
    }

    /**
     * Przypisanie listy plikow do listview w GUI użytkownika
     *
     * @param list - Lista scieżek do plików użytkownika
     */
    public void initUserDirectory(List<String> list) {
        Platform.runLater(() -> {
            files_list_listView.getItems().clear();
            list.forEach(e -> files_list_listView.getItems().add(Paths.get(e).getFileName()));
        });
    }

    /**
     * Metoda inicjująca username'y
     */
    private void initUserNames() {
        users_combobox.getItems().clear();
        users_combobox.getItems().add("user1");
        users_combobox.getItems().add("user2");
        users_combobox.getItems().add("user3");
        users_combobox.getItems().add("user4");
        users_combobox.getItems().add("user5");
    }

    /**
     * Metoda pobierająca odpowiedni label z klasy Languages i przypisująca zawartość do labela opisującego wykonywaną akcję
     *
     * @param label - np "LBL_SHARE"
     */
    public void changeInfoLog(String label) {
        Platform.runLater(() -> {
            current_status_label.setText(languages.getLabel(label));
        });
    }

    /**
     * to samo co wyżej tylko z dodatkowym tekstem
     *
     * @param label
     * @param text
     */
    public void changeInfoLog(String label, String text) {
        Platform.runLater(() -> {
            current_status_label.setText(languages.getLabel(label) + text);
        });
    }

    /**
     * Metoda usuwająca plik z listy użytkownika
     *
     * @throws InterruptedException
     */
    @FXML
    public void deleteFileAction() throws InterruptedException {
        user.deleteFileFromServer(files_list_listView.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Metoda uruchamiana pod przyciskiem "Dodaj nowy plik"
     *
     * @throws IOException
     */
    @FXML
    public void chooseFile() throws IOException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            File file = fileChooser.showOpenDialog(thisStage);
            Path sourceFile = file.toPath();
            Path destFile = Paths.get(user.getUserPath().resolve(file.getName()).toString());
            Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (NullPointerException e) {
            //error alert nie wybrano pliku
        }
    }

    /**
     * Metoda uruchamiana pod przyciskiem "Udostępnij"
     *
     * @throws InterruptedException
     */
    @FXML
    public void shareFile() throws InterruptedException {
        user.sendFileToAnotherUser(
                files_list_listView.getSelectionModel().getSelectedItem().toString(),
                users_combobox.getSelectionModel().getSelectedItem()
        );
    }

    /**
     * Zmienia zawartość statycznych napisów gdy użytkownik zmieni język w GUI
     */
    private void updateGuiLabels() {
        txt1_label.setText(languages.getLabel("LBL_USER"));
        txt2_label.setText(languages.getLabel("LBL_PATH"));
        txt3_label.setText(languages.getLabel("LBL_SHARESELECTEDFILE"));
        share_button.setText(languages.getLabel("LBL_SHARE"));
        addNewFile_button.setText(languages.getLabel("LBL_ADDFILE"));
        deleteSelectedFile_button.setText(languages.getLabel("LBL_DELETEFILE"));
    }

    /**
     * Metoda odpowiedzialne za zmiane języka na Polski
     */
    @FXML
    public void setLanguagePL() {
        languages.setLanguage(Languages.Language.PL);
        updateGuiLabels();
    }

    /**
     * Metoda odpowiedzialne za zmiane języka na Niemiecki
     */
    @FXML
    public void setLanguageGR() {
        languages.setLanguage(Languages.Language.GR);
        updateGuiLabels();
    }

    /**
     * Metoda odpowiedzialne za zmiane języka na Angielski
     */
    @FXML
    public void setLanguageEN() {
        languages.setLanguage(Languages.Language.EN);
        updateGuiLabels();
    }
}
