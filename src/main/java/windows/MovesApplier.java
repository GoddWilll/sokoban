package windows;


import java.io.File;

import java.util.List;


import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.util.Arrays;


import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

import javafx.scene.Node;

import javafx.scene.Parent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


import javafx.scene.media.MediaPlayer;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javafx.util.Duration;
import javafx.util.Pair;
import maingame.*;
import utils.*;
public class MovesApplier {
    
    private static Pane root = new Pane();
    
    public static double WIDTH = MainFX.WIDTH;
    public static double HEIGHT = MainFX.HEIGHT;
    private static Line line;
    static MediaPlayer mediaPlayer;
    private static VBox opthbox = new VBox(15);
    static Stage stage1 = new Stage();
    static Stage stage2 = new Stage();
    static Stage stage3 = new Stage();
    static File input;
    static File moves;



    private static List<Pair<String, Runnable>> menuData = Arrays.asList(
        new Pair<String, Runnable>("Niveau en entree :", ()-> start(stage1)),
        new Pair<String, Runnable>("Fichier .mov :", ()->start(stage2)),

        new Pair<String, Runnable>("Appliquer", ()-> appliquer(input, moves)),
        new Pair<String, Runnable>("Retour", () -> MainFX.window.setScene(MainFX.mainMenu)) // permet de quitter le jeu
        );


    /**
     * Permet de creer la scene de l'applicateur de .mov
     * @return un Parent
     */
    public static Parent sceneCreate(){
        root = new Pane();
        opthbox = new VBox(15);
        addBackground(MainFX.wallName);
        addTitle();
        addMenu(MainFX.WIDTH/2-100, 200, opthbox, menuData);  
        startAnimation(opthbox);
        return root;
    }

    /**
     * Permet d'ajouter un fond d'ecran
     * @param name un String du nom du fond d'ecran
     */
    public static void addBackground(String name){

        ImageView imageView = new ImageView(new Image("/wallpapers/" + name));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);
    }


    /**
     * Permet d'ajouter un titre
     */
    public static void addTitle(){
        SokoTitle title = new SokoTitle("APPLIQUER UN .MOV");
        title.setTranslateX(WIDTH/2-title.getTitleWidth() /2);
        title.setTranslateY(HEIGHT- (HEIGHT-60));
        root.getChildren().add(title);
    }


      /**
     * Demarre l'animation de Transition du demarrage du jeu.
     */
    private static void startAnimation(VBox menuBox){
        ScaleTransition transition = new ScaleTransition(Duration.seconds(1), line); // transition d'une seconde
        transition.setToY(1);
        transition.setOnFinished(e -> { // action à effectuer à la fin de l'animation
            for (int i =0; i< menuBox.getChildren().size(); i++){
                Node n = menuBox.getChildren().get(i); // l'item à déplacer

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i*0.1), n); // transition linéaire. Le 1 + i*0.1 permet d'amener les éléments les uns après les autres.
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();  
            }
        });
        transition.play(); // on lance
    }



    /**
     * Permet de créer le menu de base du jeu.
     * @param x un double representant la position horizontale initiale du menu
     * @param y un double representant la position verticale initiale du menu
     * @param menuBox un VBox qui contiendra les elements du menu
     * @menuData une List<Pair<String, Runnable>> qui contient les elements du menu
     */
    private static void addMenu(double x, double y, VBox menuBox, List<Pair<String, Runnable>> menuData){
        menuBox.setTranslateX(x); // on se place à la x et y ième position
        menuBox.setTranslateY(y);
        menuData.forEach(data -> { // permet de récuperer les elements de la Liste de Pair et de faire l'action qui suit:
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
     * Permet de creer les fenetres permettant de recuperer les fichiers voulus
     * @param stage un Stage pour la genetre
     */
    public static void start(final Stage stage) {
        
        final FileChooser fileChooser = new FileChooser(); // on créé un nouveau fileChooser
        configureFileChooser(fileChooser); 
        List<File> list = fileChooser.showOpenMultipleDialog(stage); // permet de choisir les fichiers
        if (list != null) { // si la liste de fichiers choisi n'est pas vide
            for (File file : list) {
                if (stage == stage1){
                    input = file;
                } else if (stage == stage2){
                    moves = file;
                }
            }
        }
        
    }

    /**
     * Permet de configurer le selecteur de fichier.
     * @param fileChooser le selecteur de fichier (FileChooser)
     */
    private static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle("Selection :"); // titre de la fenetre
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // l'endroit ou l'explorateur de fichier s'ouvre
    }

    /**
     * Permet d'appliquer le .mov au fichier .xsb
     * @param input un File representant le niveau en entree
     * @param moves un File representant le fichier .mov
     *
     */
    private static void appliquer(File input, File moves){

        String pathoutput = System.getProperty("user.dir") + "/src/main/resources/outputlevels/"; // le dossier de destination
        Map.inputMov(input.getPath(), moves.getPath(), pathoutput+input.getName());
    }

  

    
}


