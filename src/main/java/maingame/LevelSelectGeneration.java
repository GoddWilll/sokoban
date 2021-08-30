package maingame;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;

import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javafx.util.Duration;
import javafx.util.Pair;
import utils.*;
import windows.*;


public class LevelSelectGeneration {
    private static Pane root = new Pane();
    private static VBox menuvBox1;
    private static VBox menuvBox2;
    private static VBox menuvBox3;
    private static VBox boxResfresh;

    public static double WIDTH = MainFX.WIDTH;
    public static double HEIGHT = MainFX.HEIGHT;
    private static Line line;

    /**
     * Permet de verifier si le niveau a deja ete sauvegarde
     * @param files un File[] representant un dossier contenant les niveaux
     * @param level un String representant le niveau
     * @return un booleen
     */
    private static boolean isSaved(File[] files, String level){

        for(File file : files){
            if((level.replaceFirst("[.][^.]+$", "")+"-save.xsb").equals(file.getName())){ // si une version sauvegarde du fichier existe, retourne true
                return true;
            }
        }
        return false;
    }

    // ######## ON VA ENTRER LES NIVEAUX DU DOSSIER NIVEAUX EN TANT QUE MENU 

    /**
     * Permet de créer la liste de niveaux à partir du fichier 'levels'
     * @return Une Liste pour les options.
     */
    private static List<Pair<String, Runnable>> levelDisplay(){
        String path = System.getProperty("user.dir") + "/src/main/resources/customlevels/";
        File folder = new File(path);
        File[] files = folder.listFiles();
        ArrayList<Pair<String, Runnable>> nivo = new ArrayList<Pair<String, Runnable>>() ;
        for ( File level : files){
            if (isSaved(files, level.getName())){ // on parcoury le dossier contenant les niveaux. Si une sauvegarde existe, on n'ajoute pas le niveau à la liste
                continue;
            } else {
                nivo.add(new Pair<String, Runnable>(level.getName().replaceFirst("[.][^.]+$", ""), ()->launchGame(level.getName())));
            }
              
        }  
        
        return nivo;
    }

    private static List<Pair<String, Runnable>> menuData = levelDisplay(); // on cree la liste des niveaux

    private static List<Pair<String, Runnable>> menuData2 = Arrays.asList(  // on cree la liste des boutons
        new Pair<String, Runnable>("Refresh", () -> refresh() ),
        new Pair<String, Runnable>("Retour", () -> MainFX.window.setScene(MainFX.mainMenu))
    );

    /**
     * Permet de rafraichir la page affichant la liste des niveaux
     */
    public static void refresh(){
        MainFX.levelGSelector = new Scene(sceneCreate()); // recree la scene
        MainFX.window.setScene(MainFX.levelGSelector); // reassigne la scene au Stage principal
    }

    /**
     * Permet de lancer le jeu de base
     * @param level un String representant le niveau
     */
    public static void launchGame(String level) {
        
        Scene jeu = new Scene(GameBase.sceneCreate(level, "/customlevels/", "custom"));
        MainFX.window.setScene(jeu); 
        
    }

    /**
     * Permet de creer un Pane qui servira de scene pour la selection du jeu
     * @return un Pane
     */
    public static Pane sceneCreate(){

        root = new Pane();
        menuvBox1 = new VBox(10);
        menuvBox2 = new VBox(10);
        menuvBox3 = new VBox(10);
        boxResfresh = new VBox();
        menuData = levelDisplay();
        
        addBackground(MainFX.wallName);
        addTitle();

        addMenu(menuvBox1, menuvBox2, menuvBox3);
        addMenu2(boxResfresh);
        startAnimation(menuvBox1);
        startAnimation(menuvBox2);
        startAnimation(menuvBox3);
        startAnimation(boxResfresh);

        return root;
    }

    /**
     * Permet de lancer l'animation ainsi que de changer les composants du menu en Nodes
     * @param menuBox une VBox contenant les elements du menu
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
     * Permet d'ajouter un fond d'écran
     * @param name un String representant le fond d'ecran
     */
    public static void addBackground(String name){

        String path = "file:///" + System.getProperty("user.dir") + "/src/main/resources/wallpapers/"; 
        ImageView imageView = new ImageView(new Image(path + name));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);

    }

    /**
     * Permet d'ajouter un titre a la scene
     */
    public static void addTitle(){

        SokoTitle title = new SokoTitle("SELECTION DU NIVEAU");
        title.setTranslateX(WIDTH/2-title.getTitleWidth() /2);
        title.setTranslateY(HEIGHT- (HEIGHT-60));
        root.getChildren().add(title);

    }

    /**
     * Permet de creer le menu contenant les elements de la Box passee en parametre
     * @param menuBox une VBox contenant les elements a afficher
     */
    private static void addMenu2(VBox menuBox){

        menuBox.setTranslateX(900); // on se place à la x et y ième position
        menuBox.setTranslateY(600);
        menuData2.forEach(data -> { // permet de récuperer les elements de la Liste de Pair et de faire l'action qui suit:
            SokoMenuItem item = new SokoMenuItem(data.getKey(), 13); // on créé un nouvel item du menu et récupérant le String (nom de l'option)
            item.setOnAction(data.getValue()); // on applique l'action Runnable de la liste à l'item
            item.setTranslateX(-300); // on fait une translation 

            Rectangle clip = new Rectangle(300, 30); // on créé le rectangle autour du titre de l'option
            clip.translateXProperty().bind(item.translateXProperty().negate());
            item.setClip(clip); // assigne le rectangle au titre (a l'item)

            menuBox.getChildren().addAll(item);

        });

        root.getChildren().add(menuBox);
    }


    /**
     * Permet de creer le menu contenant les elements des Box passees en parametre
     * @param menuBox1 une VBox
     * @param menuBox2 une VBox
     * @param menuBox3 une VBox
     */
    public static void addMenu(VBox menuBox1, VBox menuBox2, VBox menuBox3){
        // on définit les coordonnées initiales des VBox
        menuBox1.setTranslateX(300);
        menuBox1.setTranslateY(100);
        menuBox2.setTranslateX(600);
        menuBox2.setTranslateY(100);
        menuBox3.setTranslateX(900);
        menuBox3.setTranslateY(100);


        int taille = menuData.size();
        int borne = 0; 
        int i = 0; 
        int dizaine = taille/10;
        int dizaineSave = taille/10;
        int reste = taille%10;

        // CETTE PARTIE DU CODE PERMET DE SEPARER LA LISTE DES NIVEAUX EN 3, ET DE LES REPARTIR DANS LES 3 BOX DIFFERENTES.
        // CELA PERMET D'AJOUTER JUSQU'A 30 NIVEAUX PERSONNALISES
        // ON DIVISE LE NOMBRE TOTAL DE NIVEAUX POUR RECUPERER LE NOMBRE DE DIZAINES, PUIS LE RESTE POUR LES REPARTIR


        if (dizaine!=0){
            while(dizaine!=0){
                borne += 10;
                if (dizaine == 3){
                    for(int b =i; b<borne;b++){
                        i++; 
                        SokoMenuItem item = new SokoMenuItem(menuData.get(b).getKey(), 13);
                        item.setOnAction(menuData.get(b).getValue());
                        item.setTranslateX(-300);
    
                        Rectangle clip = new Rectangle(300, 30);
                        clip.translateXProperty().bind(item.translateXProperty().negate());
    
                        item.setClip(clip);
    
                        menuBox3.getChildren().addAll(item);
                    }
                    dizaine -= 1;
                                                   
                } else if(dizaine == 2){
                    for(int b =i; b<borne;b++){
                        i++;
                        SokoMenuItem item = new SokoMenuItem(menuData.get(b).getKey(), 13);
                        item.setOnAction(menuData.get(b).getValue());
                        item.setTranslateX(-300);
    
                        Rectangle clip = new Rectangle(300, 30);
                        clip.translateXProperty().bind(item.translateXProperty().negate());
    
                        item.setClip(clip);
    
                        menuBox2.getChildren().addAll(item);
                    }

                    dizaine -= 1;

                } else if (dizaine == 1){
                    for(int b =i; b<borne;b++){
                        i++; 
 
                        SokoMenuItem item = new SokoMenuItem(menuData.get(b).getKey(), 13);
                        item.setOnAction(menuData.get(b).getValue());
                        item.setTranslateX(-300);
    
                        Rectangle clip = new Rectangle(300, 30);
                        clip.translateXProperty().bind(item.translateXProperty().negate());
    
                        item.setClip(clip);
    
                        menuBox1.getChildren().addAll(item);
                    }
                    dizaine -= 1;

                }
            }
        }

        if (reste != 0){
            if (dizaineSave == 2){
                for(int b =i; b<borne+reste;b++){
                    SokoMenuItem item = new SokoMenuItem(menuData.get(b).getKey(), 13);
                    item.setOnAction(menuData.get(b).getValue());
                    item.setTranslateX(-300);

                    Rectangle clip = new Rectangle(300, 30);
                    clip.translateXProperty().bind(item.translateXProperty().negate());

                    item.setClip(clip);

                    menuBox3.getChildren().addAll(item);
                }
            } else if(dizaineSave == 1 ){
                for(int b =i; b<borne+reste;b++){

                    SokoMenuItem item = new SokoMenuItem(menuData.get(b).getKey(), 13);
                    item.setOnAction(menuData.get(b).getValue());
                    item.setTranslateX(-300);

                    Rectangle clip = new Rectangle(300, 30);
                    clip.translateXProperty().bind(item.translateXProperty().negate());

                    item.setClip(clip);

                    menuBox2.getChildren().addAll(item);
                }
            } else if(dizaineSave == 0){
                for(int b =i; b<reste;b++){
                    
                    SokoMenuItem item = new SokoMenuItem(menuData.get(b).getKey(), 13);
                    item.setOnAction(menuData.get(b).getValue());
                    item.setTranslateX(-300);

                    Rectangle clip = new Rectangle(300, 30);
                    clip.translateXProperty().bind(item.translateXProperty().negate());

                    item.setClip(clip);

                    menuBox1.getChildren().addAll(item);
                }
            }
        }

        root.getChildren().addAll(menuBox1, menuBox2, menuBox3);
    }
    
    
}
