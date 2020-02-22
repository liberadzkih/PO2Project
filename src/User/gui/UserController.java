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
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class UserController {
    private final Stage thisStage;
    private User user;
    public int userFilesCount = 0;
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
            thisStage.setScene(new Scene(loader.load(), 600, 400));
            thisStage.setOnCloseRequest(event -> System.exit(0));
            current_status_label.setText("Jeszcze nie wykonano akcji");
            initUserNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.show();
    }

    public void setUser(User user) {
        this.user = user;
        thisStage.setTitle(user.getUserName());
        user_name_label.setText(user.getUserName());
        user_path_label.setText(user.getUserPath().toString());
    }

    public void initUserDirectory(List<String> list) {
        Platform.runLater(() -> {
            files_list_listView.getItems().clear();
            list.forEach(e -> files_list_listView.getItems().add(Paths.get(e).getFileName()));
        });
    }

    private void initUserNames() {
        users_combobox.getItems().clear();
        users_combobox.getItems().add("user1");
        users_combobox.getItems().add("user2");
        users_combobox.getItems().add("user3");
        users_combobox.getItems().add("user4");
        users_combobox.getItems().add("user5");
    }

    public void changeInfoLog(String text) {
        Platform.runLater(() -> {
            current_status_label.setText(text);
        });
    }

    @FXML
    public void deleteFileAction() throws InterruptedException {
        user.deleteFileFromServer(files_list_listView.getSelectionModel().getSelectedItem().toString());
    }

    @FXML
    public void addFileAction() {

    }

    @FXML
    public void shareFile() throws InterruptedException {
        user.sendFileToAnotherUser(
                files_list_listView.getSelectionModel().getSelectedItem().toString(),
                users_combobox.getSelectionModel().getSelectedItem()
        );
    }
}
