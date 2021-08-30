package maingame;
import java.io.File;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import club.minnced.discord.rpc.*;


import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.scene.media.MediaPlayer;
import utils.*;
import windows.*;


public class MainFX extends Application {
    
    public static final double WIDTH = 1280; // taille de la fenetre 
    public static final double HEIGHT = 720;
    static Stage primaryStage = new Stage(); // on créé une nouvelle fenetre
    Stage LevelStage;  // Le stage représente entre guillemets la fenetre
    public static Stage ImporterStage = new Stage(); // je créé la fenetre pour ouvrir l'importer de niveaux
    
    public static Stage window;
    public static Scene mainMenu;
    
    
    

    // ######## SELECTION DE NIVEAU ######### 

    public static Scene levelSelector ; // je créé la scène pour le selecteur de niveau
    public static Scene levelGSelector ;  // scene selecteur de niveaux persos
    Scene options; // scene options
    Scene generateur; // scene generateur
    Scene movesApplier;
    


    // ######## MENU PRINCIPAL ########
    /*
    une partie du code est inspirée par https://stackoverflow.com/questions/53639607/pairstring-runnable-in-a-game-menu
    on créé une liste de Pair, qui fonctionnent avec un système Clé/Valeur un peu comme un dico,
    avec Pair(K, V)  K: clé; V: valeur
    on peut récuperer cette clé en faisant getKey(), et la valeur en faisant getValue().

    Dans la List ci dessous, on assigne un String à un Runnable, en utilisant l'implementation Lambda de Runnable
    http://tutorials.jenkov.com/java-concurrency/creating-and-starting-threads.html
    Dès que le joueur clique sur le titre on execute ce qui est entre parenthèses.
    */
    private List<Pair<String, Runnable>> menuData = Arrays.asList(
        new Pair<String, Runnable>("Jouer", () -> window.setScene(levelSelector)), // le bouton jouer change la scene pour prendre celle du selecteur de niveaux
        new Pair<String, Runnable>("Partie personnalis\u00e9e", () -> window.setScene(levelGSelector)),
        new Pair<String, Runnable>("Importer des niveaux", () -> LevelAdder.start(ImporterStage)), // ce bouton lance la méthode d'importation de niveaux
        new Pair<String, Runnable>("G\u00e9n\u00e9rateur de niveaux", () -> window.setScene(generateur)),
        new Pair<String, Runnable>("Appliquer un .mov", () -> window.setScene(movesApplier)),
        new Pair<String, Runnable>("Options", () -> window.setScene(options)),
        new Pair<String, Runnable>("Quitter", () -> fin()) // permet de quitter le jeu
    );

   
    private Pane root = new Pane(); 
    private VBox menuBox = new VBox(1);
    private Line line; 
    private boolean finir = false;

    /**
     * Permet de fermer le jeu correctement.
     * @return un boolean pour fermer le jeu.
     */
    private boolean fin(){  
        window.close();
        finir = true;
        return finir;
    }


    /**
     * Permet de créer le menu en invoquant plusieurs méthodes
     * @return un parent.
     */
    private Parent sceneCreate(){
        
        addBackground(); // ajoute le fond d'écran
        addTitle(); // ajoute le titre

        double lineX = WIDTH/2-100; // on place le menu aux bonnes coordonnées 
        double lineY = HEIGHT/3+100;

        addMenu(lineX, lineY ); // ajoute le menu
        startAnimation(); // demarre l'animation
 
        return root;
    }



    /**
     * Permet d'appliquer le fond d'écran.
     */
    private void addBackground(){
      
        ImageView imageView = new ImageView(new Image("/wallpapers/chuteshd.gif")); // on créé un nouvel imageview
        imageView.setFitHeight(HEIGHT); // de la taille de la hauteur et largeur définies en amont
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView); // on ajoute l'image view
    }


    /**
     * Permet d'ajouter un titre à la scene.
     */
    private void addTitle(){
        SokoTitle title = new SokoTitle("SOKOBAN"); // le titre
        title.setTranslateX(WIDTH/2-title.getTitleWidth() /2); // sa position
        title.setTranslateY(HEIGHT/3);
        root.getChildren().add(title);
    }

    /**
     * Demarre l'animation de Transition du demarrage du jeu.
     */
    private void startAnimation(){
        ScaleTransition transition = new ScaleTransition(Duration.seconds(1), line); // transition d'une seconde
        transition.setToY(1);
        transition.setOnFinished(e -> { // action à effectuer à la fin de l'animation
            for (int i =0; i< menuBox.getChildren().size(); i++){
                Node n = menuBox.getChildren().get(i); // l'item du menu convertit en node
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
     * @param x un double donnant la position horizontale du menu
     * @param y
     */
    private void addMenu(double x, double y){
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
     * Permet de selectionner un fond d'écran aléatoirement dans le dossier.
     * @return un String donnant le fond d'écran.
     */
    public static String getWallpaper(){
        String path = System.getProperty("user.dir") + "/src/main/resources/wallpapers/"; // on récupère le fond d'écran
        File folder = new File(path); // fichier wallpaper
        File[] files = folder.listFiles(); //liste de fichiers
        if (files == null){
            return "tempole.gif"; // si le dossier est vide, on return directement tempole.gif
        }
        Random random = new Random(); 
        int bound = files.length; // le nombre de fichiers
        int number = random.nextInt(bound); // on choisit un nombre aléatoire 
        return files[number].getName(); // on recupere le nom du n-ieme fichier
    }
    //static String path = System.getProperty("user.dir") + "/src/main/resources/wallpapers/chuteshd.gif";
    public static String wallName = "chuteshd.gif";
    





    


    /**
     * Permet de lancer le Discord Rich Presence.
     */
    public void discordRPC(){  // code inspiré par la documentation fournie par Discord.
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "835493075366838274";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.largeImageKey = "rtyur";
        presence.smallImageKey = "jeu";
        presence.largeImageText = MainGame.level; // MODIFIER POUR METTRE LE NOM DU NIVEAU!
        presence.details = "Sokoban";// mettre le niveau
        presence.state = "Version : 0.4";
        lib.Discord_UpdatePresence(presence);
        
        new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) { // tant que le thread n'est pas interrompu..
                if (finir == true){
                    return;
                }
                lib.Discord_RunCallbacks();
                try {
                    if (window.getScene() == levelSelector || window.getScene() == mainMenu){
                        presence.state = "Dans les menus";
                        lib.Discord_UpdatePresence(presence);
                    }else {
                        return;
                    }
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }               
        }, "RPC-Callback-Handler").start();
    }
    

    @Override
    /**
     * Permet de démarrer le programme.
     */
    public void start(Stage primaryStage) throws Exception{
        
      

        levelSelector = new Scene(LevelSelect.sceneCreate()); // je créé la scène pour le selecteur de niveau
        levelGSelector = new Scene(LevelSelectGeneration.sceneCreate()); //créé la scène pour le selecteur de niveaux persos 
        options = new Scene(Options.sceneCreate()); // créé le menu des options
        generateur = new Scene(GenerationScene.sceneCreate()); // créé la scene pour le generateur de niveaux
        movesApplier = new Scene(MovesApplier.sceneCreate()); //
        window = primaryStage; // création de la fenetre
        mainMenu = new Scene(sceneCreate()); // on créé le menu
        window.setTitle("Sokoban"); // titre de l'app 
        window.setScene(mainMenu); // on définitla scene de base (menu)
        Image icon = new Image("/icone.png"); // on va chercher l'icone pour l'app
        window.getIcons().add(icon);  // on l'applique
        window.setResizable(false); // on définit la fenetre comme non re-dimensionnable 
        window.setOnCloseRequest(e-> fin());
        discordRPC();
        
        
        window.show(); // affichage de la fenetre
        

        

    }

    public static void main(String[] args) {
        launch(args);
    }

}
