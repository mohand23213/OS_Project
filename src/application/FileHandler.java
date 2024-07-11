package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileHandler {

    // Method to read a files
    public ArrayList<ProcessControlBlock> loadFile() {
        ArrayList<ProcessControlBlock> pcbCollection = new ArrayList<>();
        FileChooser chooser = new FileChooser();
        
        // Set  filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            try {
                List<String> fileLines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
                ///divide the string into lines of string
                for (String line : fileLines) {
                    pcbCollection.add(new ProcessControlBlock(line));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return pcbCollection;
    }
}
