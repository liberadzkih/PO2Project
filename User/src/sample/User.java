package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {
    private String username;
    private String directoryPath;
    private List<String> file_paths;
    private List<String> file_names;
    private String language; // pl or gr or en

    public User(String username, String directoryPath) {
        this.username = username;
        this.directoryPath = directoryPath;
        this.language = "pl";
        this.file_names = new ArrayList<>();
        this.file_paths = new ArrayList<>();
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

    public void loadFilesFromUserDirectory() throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(directoryPath));

        List<String> result = walk.filter(Files::isRegularFile)
                .map(x -> x.toString()).collect(Collectors.toList());

        List<String> file_paths = new ArrayList<>();
        List<String> file_names = new ArrayList<>();

        for (String s : result) {
            file_paths.add(s);
            file_names.add(Paths.get(s).getFileName().toString());
        }
        setFile_names(file_names);
        setFile_paths(file_paths);
    }

    public void deleteFile(String filename) throws NullPointerException, IOException {
        String pathToDelete = directoryPath + "\\" + filename;
        Files.deleteIfExists(Paths.get(pathToDelete));
    }
}
