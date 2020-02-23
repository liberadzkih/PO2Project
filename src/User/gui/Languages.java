package User.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa przechowująca zawartość tekstową dla danego języka
 */
public class Languages {
    enum Language {PL, GR, EN}

    Map<String, String> labelsPL;
    Map<String, String> labelsGR;
    Map<String, String> labelsEN;
    private Language language;

    public Languages(Language language) {
        initLabelsEN();
        initLabelsGR();
        initLabelsPL();
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getLabel(String label) {
        if (language == Language.EN)
            return labelsEN.get(label);
        else if (language == Language.GR)
            return labelsGR.get(label);
        else
            return labelsPL.get(label);
    }

    private void initLabelsPL() {
        labelsPL = new HashMap<>();
        labelsPL.put("LBL_USER", "Użytkownik ");
        labelsPL.put("LBL_PATH", "Ścieżka ");
        labelsPL.put("LBL_SHARESELECTEDFILE", "Udostępnij zaznaczony plik ");
        labelsPL.put("LBL_ADDFILE", "Dodaj nowy plik ");
        labelsPL.put("LBL_SHARE", "Udostępnij");
        labelsPL.put("LBL_DELETEFILE", "Usuń plik");
        labelsPL.put("LBL_DOWNLOADFROMSERVER", "Pobieranie zawartości dysku z serwera...");
        labelsPL.put("LBL_LOADUSERDIRECTORY", "Ładowanie plików z katalogu użytkownika...");
        labelsPL.put("LBL_DELETEDFILE", "Usunięto plik ");
    }

    private void initLabelsEN() {
        labelsEN = new HashMap<>();
        labelsEN.put("LBL_USER", "User ");
        labelsEN.put("LBL_PATH", "Path ");
        labelsEN.put("LBL_SHARESELECTEDFILE", "Share selected file to ");
        labelsEN.put("LBL_ADDFILE", "Add new file");
        labelsEN.put("LBL_SHARE", "Share");
        labelsEN.put("LBL_DELETEFILE", "Delete file");
        labelsEN.put("LBL_DOWNLOADFROMSERVER", "Downloading user files from server...");
        labelsEN.put("LBL_LOADUSERDIRECTORY", "Loading files from users directory...");
        labelsEN.put("LBL_DELETEDFILE", "Deleted file: ");
    }

    private void initLabelsGR() {
        labelsGR = new HashMap<>();
        labelsGR.put("LBL_USER", "Benutzer ");
        labelsGR.put("LBL_PATH", "Pfad ");
        labelsGR.put("LBL_SHARESELECTEDFILE", "Freigegebene ausgewählte Datei ");
        labelsGR.put("LBL_ADDFILE", "Datei hinzufügen ");
        labelsGR.put("LBL_SHARE", "Teilen");
        labelsGR.put("LBL_DELETEFILE", "Datei löschen");
        labelsGR.put("LBL_DOWNLOADFROMSERVER", "Herunterladen von Benutzerdateien vom Server...");
        labelsGR.put("LBL_LOADUSERDIRECTORY", "Laden Sie Dateien aus dem Benutzerverzeichnis...");
        labelsGR.put("LBL_DELETEDFILE", "Deleted file ");
    }
}
