package sample;

import javafx.scene.control.ListView;

import java.util.List;

public class UserInfo {
    String username;
    String path;
    List<String> file_paths;
    List<String> file_names;
    ListView user_files_listview;

    public UserInfo(String username, String path, List<String> file_paths, List<String> file_names, ListView user_files_listview) {
        this.username = username;
        this.path = path;
        this.file_paths = file_paths;
        this.file_names = file_names;
        this.user_files_listview = user_files_listview;
    }

    public void fillListView() {
        this.user_files_listview.getItems().addAll(file_names);
    }
}
