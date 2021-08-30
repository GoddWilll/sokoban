package maingame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import java.util.Arrays;
import java.util.List;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javafx.util.Duration;
import javafx.util.Pair;
import utils.*;
import windows.*;


public class GameBase {

    private static StackPane root = new StackPane();
  


    public static double WIDTH = MainFX.WIDTH;
    public static double HEIGHT = MainFX.HEIGHT;
    private static Line line;
    private static VBox menuBox;
    public static char[][] essai;
    public static String levelBis; 
    public static String folderBis; 
    public static String typeBis;
    




    /**
     * Permet de faire la sauvegarde de la map.
     * @param test Un tableau de character
     * @param level Un String représentant le niveau 
     * @param folder Un String représentant le dossier du niveau
     * @throws IOException
     */
    public static void saveGame(char[][] test, String level, String folder) throws IOException{
        
        
        String path = System.getProperty("user.dir") + "/src/main/resources/";
        
        MainGame.map.save(level); // on créé une sauvegarde en .mov des mouvements du personnage
        
        File existant = new File(path + folder + level); // on créé un file du niveau

        if(existant.getName().substring(existant.getName().replaceFirst("[.][^.]+$", "").length()-4, existant.getName().replaceFirst("[.][^.]+$", "").length() ).equals("save")){ // permet de vérifier s'il existe déjà une sauvegarde
            existant.delete(); // si oui, on la supprime pour ne pas avoir de doublons.
            try {
                
                
                File save = new File(path + folder + level);
                FileWriter writer = new FileWriter(path + folder + level); // on écrit dans le fichier
              
                for(int i=0; i<test.length;i++){
                    for (int j=0; j<test[i].length; j++){   // on écrit dans le fichier les cases
                        writer.write(test[i][j]);
                    }
                    writer.write("\n");
                }
                writer.write("A");
                
                
                writer.close();
              } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
              }
        } else {
            Path path2 = FileSystems.getDefault().getPath(path + folder + level.replaceFirst("[.][^.]+$", "")+"-save.xsb"); //si pas de doublon on créé le fichier de sauvegarde
        try{
            Files.delete(path2);
        } catch (NoSuchFileException ignored){}


        try {
            // on créé un fichier save
            File save = new File(path + folder + level.replaceFirst("[.][^.]+$", "")+ "-save.xsb");
            
            // on écrit dans le fichier
            FileWriter writer = new FileWriter(path + folder + level.replaceFirst("[.][^.]+$", "")+ "-save.xsb");
            
            for(int i=0; i<test.length;i++){
                for (int j=0; j<test[i].length; j++){
                    writer.write(test[i][j]);             // on remplit  le fichier avec l'état du jeu
                }
                writer.write("\n");
            }
            writer.write("A\n");
            writer.write("");
            
            
            writer.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        }

        
    }
    
    /**
     * Permet de créer la base de l'interface du jeu
     * @param level Un String représentant le niveau
     * @param folder Un String représentant le dossier
     * @param type Un String représentant le type de niveau (personnalisé ou de base)
     * @return un StackPane
     */
    public static Parent sceneCreate(String level, String folder, String type) {
 
        levelBis = level; 
        folderBis = folder;
        typeBis = type;
    
        root = new StackPane();                // on créé un nouveau StackPane
        BorderPane jeu = new BorderPane();
        menuBox = new VBox(1); 
        
        try {
            addBackground(MainGame.path); // on ajoute un fond d'écran au jeu
           
            
            jeu = MainGame.lancement(level, folder); // on lance le jeu 

     
            root.getChildren().add(jeu); // on l'ajoute au StackPane
            
          
            addMenu(1100, 600, type);
            startAnimation(menuBox);
            
            essai = MainGame.departMap;
            return root;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return root;
    }

    /**
     * Permet de recommencer le niveau.
     * @param level un String representant le niveau
     * @param folder un String representant le dossier qui contient le niveau
     * @param type un String representant le type de niveau (Personnalisé ou de base)
     */
    public static void restart(String level, String folder, String type){
        String levelf = level; // on définit le level
        String path = System.getProperty("user.dir") + "/src/main/resources/" + folder;
        File foldeer = new File(path);
        if (level.replaceFirst("[.][^.]+$", "").substring(level.replaceFirst("[.][^.]+$", "").length()-4, level.replaceFirst("[.][^.]+$", "").length()).equals("save")){ // si on joue sur la sauvegarde
            levelf = level.replaceFirst("[.][^.]+$", "").substring(0, level.replaceFirst("[.][^.]+$", "").length()-5) + ".xsb"; // on prend le niveau de base (qui n'est pas la sauvegarde) en retirant le "-save"
        }
         
        sceneCreate(levelf, folder, type); // on recréé la scene
        if (type.equals("custom")){
            LevelSelectGeneration.launchGame(levelf);
        } else{
            LevelSelect.launchGame(levelf, foldeer.listFiles()); // on relance le jeu
        }

    }


    // liste contenant les elements du menu
    private static List<Pair<String, Runnable>> menuData = Arrays.asList(
        new Pair<String, Runnable>("Recommencer", () -> restart(levelBis, folderBis, typeBis)    ),
        new Pair<String, Runnable>("Sauvegarder", ()->{
            try {
               saveGame(essai, levelBis, folderBis);
            } catch (IOException e) {
                System.out.println("Erreur.");
            }
        }),
        new Pair<String, Runnable>("Quitter", () -> MainFX.window.setScene(MainFX.levelSelector))
    );

    // liste contenant les elements du menu
    private static List<Pair<String, Runnable>> menuData2 = Arrays.asList(
        new Pair<String, Runnable>("Recommencer", () -> restart(levelBis, folderBis, typeBis)    ),
        new Pair<String, Runnable>("Sauvegarder", ()->{
            try {
                saveGame(essai, levelBis, folderBis);
            } catch (IOException e) {
                System.out.println("Erreur.");
            }
        }),
        new Pair<String, Runnable>("Quitter", () -> MainFX.window.setScene(MainFX.levelGSelector))
    );


    /**
     * Permet de lancer l'animation et de créer des Nodes pour chaque element de la box
     * @param menuBox une VBox contenant les elements à afficher
     */
    public static void startAnimation(VBox menuBox){
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {
            for (int i =0; i< menuBox.getChildren().size(); i++){
                Node n = menuBox.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i*0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    /**
     * Permet d'ajouter un fond d'écran au niveau
     * @param name un String représenant le niveau à ajouter
     */
    public static void addBackground(String name){
        String path = "file:///" + System.getProperty("user.dir") + "/src/main/resources/"; 
        
        ImageView imageView = new ImageView(new Image(path + name));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);
    }

    /**
     * Permet d'ajouter un titre au niveau
     * @param level un String representant le nom du niveau
     */
    public static void addTitle(String level){
        SokoTitle title = new SokoTitle(level.replaceFirst("[.][^.]+$", "").replace('_', ' '));
        title.setTranslateX(WIDTH/2-title.getTitleWidth() /2);
        title.setTranslateY(HEIGHT- (HEIGHT-60));
        root.getChildren().add(title);
    }

    /**
     * Permet de creer les elements du menu
     * @param x un double representant la position horizontale du menu
     * @param y un double representant la position verticale du menu
     * @param type un String representant le type de niveau (custom ou de base)
     */
    private static void addMenu(double x, double y, String type){
        menuBox.setTranslateX(x); // on se place à la x et y ième position
        menuBox.setTranslateY(y);
        if (type.equals("custom")){
            menuData2.forEach(data -> { // permet de récuperer les elements de la Liste de Pair et de faire l'action qui suit:
            SokoMenuItem item = new SokoMenuItem(data.getKey(), 18); // on créé un nouvel item du menu et récupérant le String (nom de l'option)
            item.setOnAction(data.getValue()); // on applique l'action Runnable de la liste à l'item
            item.setTranslateX(-300); // on fait une translation 

            Rectangle clip = new Rectangle(300, 30); // on créé le rectangle autour du titre de l'option
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip); // assigne le rectangle au titre (a l'item)

            menuBox.getChildren().addAll(item);
        });
        } else {
            menuData.forEach(data -> { // permet de récuperer les elements de la Liste de Pair et de faire l'action qui suit:
            SokoMenuItem item = new SokoMenuItem(data.getKey(), 18); // on créé un nouvel item du menu et récupérant le String (nom de l'option)
            item.setOnAction(data.getValue()); // on applique l'action Runnable de la liste à l'item
            item.setTranslateX(-300); // on fait une translation 

            Rectangle clip = new Rectangle(300, 30); // on créé le rectangle autour du titre de l'option
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip); // assigne le rectangle au titre (a l'item)

            menuBox.getChildren().addAll(item);
        });
        }
        

        root.getChildren().add(menuBox);
    }

  
}
