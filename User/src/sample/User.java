package sample;

import java.util.List;

public class User {
    private String username;
    private String directoryPath;
    private List<String> file_paths;
    private List<String> file_names;
    private String language; // pl or gr or en

    public User(String username, String directoryPath) {
        this.username = username;
        this.directoryPath = directoryPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<String> getFile_paths() {
        return file_paths;
    }

    public void setFile_paths(List<String> file_paths) {
        this.file_paths = file_paths;
    }

    public List<String> getFile_names() {
        return file_names;
    }

    public void setFile_names(List<String> file_names) {
        this.file_names = file_names;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
