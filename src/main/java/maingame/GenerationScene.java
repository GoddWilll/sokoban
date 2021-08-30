package maingame;

import java.util.Arrays;
import utils.*;
import windows.*;

import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;


public class GenerationScene {
    private static Pane root = new Pane();
    
    public static final double WIDTH = MainFX.WIDTH;
    public static final double HEIGHT = MainFX.HEIGHT;
    private static Line line;
    
    // ON CREE NOS BOX VIDES

    private static HBox lenBox = new HBox();
    private static HBox heiBox = new HBox();
    private static HBox boxesBox = new HBox();
    private static HBox nameBox = new HBox();
    private static VBox geneBox = new VBox();

    // ON CREE NOS TEXTFIELDS VIDES

    public static TextField lenField = new TextField();
    public static TextField heiField = new TextField();
    public static TextField boxesField = new TextField();
    public static TextField nameField = new TextField();

    

    private static List<Pair<String, Runnable>> menuData = Arrays.asList(
        new Pair<String, Runnable>("G\u00e9n\u00e9rer", () -> MainGeneration.start()), // permet de lancer la generation du niveau
        new Pair<String, Runnable>("Retour", () -> MainFX.window.setScene(MainFX.mainMenu)) // permet de quitter le jeu

        );
    

    /**
     * Permet de creer la Scene de base
     * @return un Parent
     */
    public static Parent sceneCreate(){

        
        // ON CREE LES TEXT QUI INDIQUERONT A QUOI SERVENT LES TEXTFIELDS
        Text length = new Text("Longueur (entre 7 et 20) :");
        length.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 13));
        length.setFill(Color.WHITE);

        Text height = new Text("Hauteur (entre 7 et 20) :");
        height.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 13));
        height.setFill(Color.WHITE);

        Text boxes = new Text("Boites (entre 1 et 15):");
        boxes.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 13));
        boxes.setFill(Color.WHITE);

        Text levelName = new Text("Nom du Niveau :");
        levelName.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 13));
        levelName.setFill(Color.WHITE);

        // ON PLACE LES TEXTFIELDS

        lenField.setTranslateX(98);
        heiField.setTranslateX(109); 
        boxesField.setTranslateX(137); 
        nameField.setTranslateX(162); 
        //ON AJOUTE LES TEXTFIELDS ET TEXTES AUX BOX
        lenBox.getChildren().addAll(length, lenField); lenBox.setTranslateX(500); lenBox.setTranslateY(180); 
        heiBox.getChildren().addAll(height, heiField);  heiBox.setTranslateX(500); heiBox.setTranslateY(240); 
        boxesBox.getChildren().addAll(boxes, boxesField); boxesBox.setTranslateX(500); boxesBox.setTranslateY(300); 
        nameBox.getChildren().addAll(levelName, nameField);  nameBox.setTranslateX(500); nameBox.setTranslateY(360); 
        
        
        addBackground(MainFX.wallName);
        addTitle();
         
        addMenu(500.0, 500.0 , geneBox, menuData); 
        startAnimation(geneBox);
    


        root.getChildren().addAll(lenBox, heiBox, boxesBox, nameBox);
        
        return root;
    }

    /**
     * Permet d'ajouter un fond d'ecran a la scene
     * @param name un String representant le fond d'ecran
     */
    public static void addBackground(String name){

        ImageView imageView = new ImageView(new Image("/wallpapers/" + name));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);
    }

    /**
     * Permet d'ajouter un titre a la scene
     */
    public static void addTitle(){
        SokoTitle title = new SokoTitle("GENERATEUR DE NIVEAUX");
        title.setTranslateX(WIDTH/2-title.getTitleWidth() /2);
        title.setTranslateY(HEIGHT- (HEIGHT-60));
        root.getChildren().add(title);
    }


     /**
      * Lance l'animation du menu.
      * @param geneBox2 une VBox contenant les elements du menu
      */
    private static void startAnimation(VBox geneBox2){
        ScaleTransition transition = new ScaleTransition(Duration.seconds(1), line); // transition d'une seconde
        transition.setToY(1);
        transition.setOnFinished(e -> { // action à effectuer à la fin de l'animation
            for (int i =0; i< geneBox2.getChildren().size(); i++){
                Node n = geneBox2.getChildren().get(i); // on transforme l'item en node

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i*0.1), n); // transition linéaire. Le 1 + i*0.1 permet d'amener les éléments les uns après les autres.
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();  
            }
        });
        transition.play(); // on lance
    }



    /**
     * Permet de creer le menu de base de la generation de niveaux
     * @param x un double representant la position horizontale initiale de la VBox
     * @param y un double representant la positions verticale initiale de la VBox
     * @param menuBox une VBox pour contenir les elements du menu
     * @param menuData une List<Pair<String, Runnable>> contenant les noms et actions à realiser
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

    
}
