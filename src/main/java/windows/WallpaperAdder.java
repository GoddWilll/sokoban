package windows;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import maingame.*;
import utils.*;


public class WallpaperAdder {


    

    /**
     * Permet d'importer un niveau personnalisé.
     * @param stage La fenetre de jeu.
     */
    public static void start(final Stage stage) {
        final FileChooser fileChooser = new FileChooser(); // on créé un nouveau fileChooser
        configureFileChooser(fileChooser); 
        List<File> list = fileChooser.showOpenMultipleDialog(stage); // permet de choisir les fichiers
        if (list != null) { // si la liste de fichiers choisi n'est pas vide
            for (File file : list) {
                String path = System.getProperty("user.dir") + "/src/main/resources/wallpapers/ingamewallpapers/";  // on selectionne le fichier de destination
                File folder = new File(path);
                try {
                    FileUtils.copyFileToDirectory(file, folder); // on copie le fichier dans le dossier des niveaux importés 
                } catch (IOException e1) {
                    e1.printStackTrace(); 
                }
            }
        }
        
    }

    /**
     * Permet de configurer le selecteur de fichier.
     * @param fileChooser le selecteur de fichier (FileChooser)
     */
    private static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle("Importez vos niveaux :"); // titre de la fenetre
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // l'endroit ou l'explorateur de fichier s'ouvre
    }

}

