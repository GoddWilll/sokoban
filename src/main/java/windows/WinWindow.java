package windows;
import maingame.*;
import utils.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;
import java.util.Arrays;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class WinWindow {
    
    static Stage window = new Stage();

    /**
     * Permet d'afficher et de creer une fenetre de win
     * @param folder un String representant le dossier
     */
    public static void display(String message, String folder){
        // on cree les elements de base : la fenetre, le pane et le HBox
        window = new Stage();
        Pane root = new Pane();
        HBox menuhBox3 = new HBox();

       
        
        sceneCreate(folder, root, menuhBox3);
        HBox testBox = new HBox();

        // on cree le texte qui servira a afficher le message de reussite
        Text texte = new Text(message);
        texte.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 21)); // police d'écriture
        texte.setFill(Color.WHITE);
        
        // on ajoute le texte et on configure ses coordonnees
        testBox.getChildren().add(texte);
        testBox.setTranslateX(100);
        testBox.setTranslateY(100);
        root.getChildren().add(testBox);
        startAnimation(testBox);
     

        window.initModality(Modality.APPLICATION_MODAL); // permet d'empecher l'utilisateur de cliquer autre part que sur quitter la fenetre
        window.setTitle("Bravo!");
        Scene scene = new Scene(root);
        window.setScene(scene);
        Image icon = new Image("/icone.png");
        window.getIcons().add(icon);
        window.setResizable(false);
        if(folder.equals("/customlevels/")){  // permet de choisir la manière de fermer le niveau et l'action a executer
            window.setOnCloseRequest(e-> close2());
        } else {
            window.setOnCloseRequest(e-> close1());
        }
        window.showAndWait();

    }


    public static double WIDTH = 400;
    public static double HEIGHT = 300;
    private static Line line; 
    public static String plevel;
    public static String wallp = MainFX.wallName;
    

    


    private static List<Pair<String, Runnable>> menuData = Arrays.asList(
        new Pair<String, Runnable>("Quitter", ()->close1())
    );
    private static List<Pair<String, Runnable>> menuData2 = Arrays.asList(
        new Pair<String, Runnable>("Quitter", ()->close2())
    );

    /**
     * Permet de fermer la fenetre et de definir la scene du selecteur de niveaux
     */
    public static void close1(){
        MainFX.window.setScene(MainFX.levelSelector);
        window.close();
    }

    /**
     * Permet de fermer la fenetre et de definir la scene du selecteur de niveaux personnalises
     */
    public static void close2(){
        MainFX.window.setScene(MainFX.levelGSelector);
        window.close();
    }

    /**
     * Permet de creer la scene de base de la fenetre
     * @param folder un String representant le niveau de base
     * @param root un Pane
     * @param menuhBox3 un HBox
     * @return un Pane
     */
    public static Pane sceneCreate(String folder, Pane root, HBox menuhBox3){
      
        
        addBackground(MainFX.wallName, root);
      
        // change la creation de menu selon l'emplacement du fichier
        if (folder.equals("/customlevels/")){
            addMenu(230, 250, menuhBox3, menuData2, root);
        } else {
            addMenu(230, 250, menuhBox3, menuData, root);
        }
        
        startAnimation(menuhBox3);

        return root;
    }

    /**
     * Permet de creer les animations et de changer les elements en Nodes
     * @param menuBox un HBox contenant les elements du menu
     */
    public static void startAnimation(HBox menuBox){
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
     * Permet d'ajouter un fond d'ecran
     * @param name un String representant le nom du fond d'ecran
     * @param root un Pane
     */
    public static void addBackground(String name, Pane root){

        ImageView imageView = new ImageView(new Image("/wallpapers/" + name));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);
    }

    /**
     * Permet de creer le menu
     * @param x un double pour la position horizontale
     * @param y un double pour la position verticale
     * @param menuBox un HBox contenant les elements du menu
     * @param menuData un List menuData contenant les elements a ajouter au menu
     * @param root un Pane
     */
    public static void addMenu(double x, double y, HBox menuBox, List<Pair<String, Runnable>> menuData, Pane root){
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        SokoMenuItem item = new SokoMenuItem(menuData.get(0).getKey(), 13);
        item.setOnAction(menuData.get(0).getValue());
        item.setTranslateX(-300);

        Rectangle clip = new Rectangle(300, 30);
        clip.translateXProperty().bind(item.translateXProperty().negate());

        item.setClip(clip);


        menuBox.getChildren().addAll(item);
        
        root.getChildren().add(menuBox);
    }


    
}
