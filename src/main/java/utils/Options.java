package utils;
import maingame.*;
import windows.*;

import java.io.File;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import javafx.scene.Parent;

import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

public class Options {
    private static Pane root = new Pane();
    
    public static double WIDTH = MainFX.WIDTH;
    public static double HEIGHT = MainFX.HEIGHT;
    private static Line line;
    static MediaPlayer mediaPlayer;
    private static VBox opthbox = new VBox(15);
    private static String music; 

    private static List<Pair<String, Runnable>> menuData = Arrays.asList(
        new Pair<String, Runnable>("Changer de musique", ()-> changeMusic()),
        new Pair<String, Runnable>("Importer de la musique", ()->MusicAdder.start(MainFX.ImporterStage)),
        new Pair<String, Runnable>("Importer un fond d'ecran", () -> WallpaperAdder.start(MainFX.ImporterStage)),
        new Pair<String, Runnable>("Retour", () -> MainFX.window.setScene(MainFX.mainMenu)) // permet de quitter le jeu
        );

    /**
     * Permet de changer la musique du jeu
     */
    public static void changeMusic(){
        mediaPlayer.stop(); // on arrete le lecteur et on le recharge
        sceneCreate();
    }

    /**
     * Permet de creer la scene du menu des options.
     * @return un Parent.
     */
    public static Parent sceneCreate(){
        root = new Pane();
        opthbox = new VBox(15);
        
        music = randomMusic(); // on choisit une musique aléatoirement
        String path = System.getProperty("user.dir") + "/src/main/resources/music/"; // on récupère la musique


        // IL SEMBLERAIT QUE LE CODE POUR LA MUSIQUE NE FONCTIONNE PAS LORSQUE L'ON REND LE PROJET SUR MOODLE.
        // CELUI CI FONCTIONNE CEPENDANT SUR LES ORDINATEURS WINDOWS AINSI QUE LES ORDINATEURS LINUX DE LA SALLE ESCHER
        // DE L'UMONS



        //String song = path + music; // MUSIQUE A CHANGER
        Media h = new Media(Paths.get(path + music).toUri().toString());
        mediaPlayer = new MediaPlayer(h); // on cree le mediaPlayer
        mediaPlayer.setVolume(0.15); // on définit le volume au lancement du jeu
        mediaPlayer.play();
        Slider volumeSlider = new Slider(0, 100, 15); // on cree un slider qui va gerer le volume
        volumeSlider.setShowTickLabels(true);
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>(){ // permet de changer le volume en fonction du slider
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue()/100.0);
            }
        });
        Text volume = new Text("Volume");
       
        volume.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 13));
        volume.setFill(Color.WHITE);
        mediaPlayer.setOnEndOfMedia(new Runnable() {  // permet de relancer la musique en boucle
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
        opthbox.getChildren().addAll(volume, volumeSlider);
        addBackground(MainFX.wallName);
        addTitle();
        addMenu(MainFX.WIDTH/2-100, MainFX.HEIGHT/3+100, opthbox, menuData);  
        startAnimation(opthbox);
        return root;
    }

    /**
     * Permet de creer le fond d'ecran
     * @param name un String representant le fond d'ecran.
     */
    public static void addBackground(String name){

        ImageView imageView = new ImageView(new Image("/wallpapers/" + name));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);
    }

    /**
     * Permet d'ajouter un titre au menu Options
     */
    public static void addTitle(){
        SokoTitle title = new SokoTitle("OPTIONS");
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
     * @param menuBox une VBox qui contiendra les elements du menu
     * @param menuData une List<Pair<String, Runnable>>  qui represente les elements du menu
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
     * Permet de choisir une musique aleatoirement
     * @return un String representant le nom de la musique
     */
    public static String randomMusic(){
        String path = System.getProperty("user.dir") + "/src/main/resources/music/";
        File folder = new File(path);
        File[] files = folder.listFiles();
        int bound = files.length;
        Random random = new Random();
        int number = random.nextInt(bound);
        return files[number].getName();

    }

    
}
