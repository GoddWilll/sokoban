package maingame;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;


import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.util.Pair;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;



import utils.*;
import windows.*;



// ###################### FONCTIONNE DE LA MEME MANIERE QUE MAINFX #####################



public class LevelSelect  {

    
    private static Pane root = new Pane();
    private static HBox menuhBox1 = new HBox(15);
    private static HBox menuhBox2 = new HBox(15);
    private static HBox menuhBox3 = new HBox(15);

    public static double WIDTH = MainFX.WIDTH;
    public static double HEIGHT = MainFX.HEIGHT;
    private static Line line; 
    public static String plevel;
    public static String wallp = MainFX.wallName;
    
 

    /**
     * Verifie s'il existe deja une sauvegarde du niveau.
     * @param files un File[] qui represente le dossier contenant les niveaux
     * @param level un String qui represente la carte
     * @return un boolean
     */
    private static boolean isSaved(File[] files, String level){

        for(File file : files){
            if((level.replaceFirst("[.][^.]+$", "")+"-save.xsb").equals(file.getName())){ // si le niveau + save existe on renvoit true
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
        String path = System.getProperty("user.dir") + "/src/main/resources/levels/";
        File folder = new File(path);
        File[] files = folder.listFiles();
        
        ArrayList<Pair<String, Runnable>> listeFinale = new ArrayList<Pair<String, Runnable>>() ;
        for ( File level : files){
            
            if(isSaved(files, level.getName())){  // si la sauvegarde du fichier existe, on n'ajoute pas le fichier au menu des niveaux.
                continue;
            } else  {
                
                listeFinale.add(new Pair<String, Runnable>(level.getName().replaceFirst("[.][^.]+$", "").replace('_', ' '), ()->launchGame(level.getName(), files))); // on ajoute les niveaux sauvegardés ainsi que ceux qui ne le sont pas 
            }
               
        }  
       
        listeFinale.add(new Pair<String, Runnable>("Refresh", () -> refresh())); // on ajoute le bouton pour rafraichir les niveaux
        listeFinale.add(new Pair<String, Runnable>("Retour", () -> MainFX.window.setScene(MainFX.mainMenu))); // on ajoute le bouton de retour
        
        return listeFinale; // on return la liste de "boutons"
    }


    /**
     * Permet de rafraichir la liste des niveaux
     */
    public static void refresh(){
        MainFX.levelSelector = new Scene(sceneCreate()); // on recree la scene pour reinitialiser la liste des niveaux affichés
        MainFX.window.setScene(MainFX.levelSelector); // on redefinit la scene du stage principal
    }

    /**
     * Permet de vérifier si le niveau a ete reussi ou non 
     * @param filename un String representant le niveau 
     * @return un boolean
     */
    public static  boolean isDone(String filename){
        try{
            Scanner scanner = new Scanner(new File(filename));
            String line = null;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
            }

            if(line.equals("Done")){
                return true;
            }
            scanner.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Permet de savoir si le niveau peut etre joue ou non en fonction du niveau precedent
     * @param level un String representant le niveau
     * @param folder un File[] representant le dossier contenant les niveaux
     * @return un boolean
     */
    public static boolean isUnlocked(String level, File[] folder){
        int numero = Integer.parseInt(String.valueOf(level.replaceFirst("[.][^.]+$", "").charAt(7))); // on recupere le numero du niveau
        
        Integer numInf = numero-1; 
        String path = System.getProperty("user.dir") + "/src/main/resources/levels/";

        if (numero == 0){
            return true;
        } else {
            for (File file : folder){
                if (Integer.parseInt(String.valueOf(file.getName().replaceFirst("[.][^.]+$", "").charAt(7))) == numInf){  // on parcourt la liste des fichiers jusqu'a arriver au nombre correspondant
                    
                    if (!isDone(path  + "NIVEAU_" + numInf.toString() + ".xsb")){ // si le niveau precedent n'est pas "Done", retourne false
                    
                        return false; 
                    } else {
                        
                        return true; // return true si le niveau precedent a ete reussi
                    }
                }
            }
        }
        return false;

    }


    /**
     * Permet de lancer le jeu en fonction de si le niveau est debloque ou non
     * @param level un String representant le niveau
     * @param folder un File[] representant le dossier contenant les niveaux
     */
    public static void launchGame(String level, File[] folder) {
        
        if (isUnlocked(level, folder )){ // si le niveau est debloque
            Scene jeu = new Scene(GameBase.sceneCreate(level, "/levels/", "level")); // on peut creer une nouvelle scene du jeu 
            MainFX.window.setScene(jeu); 
        } 
        
        
    }

    private static List<Pair<String, Runnable>> menuData = levelDisplay();
    
    /**
     * Permet de creer un Pane avec ses composants pour la scene.
     * @return un Pane pour la scene.
     */
    public static Parent sceneCreate(){
        
        root = new Pane();
        menuhBox1 = new HBox(15);
        menuhBox2 = new HBox(15);
        menuhBox3 = new HBox(15);

        menuData = levelDisplay();
        
        addBackground(MainFX.wallName);
        addTitle();

        addMenu(10, 180, menuhBox1, 5, 0);
        startAnimation(menuhBox1);

        addMenu(10, 400, menuhBox2, 10, 5);
        startAnimation(menuhBox2);

        addMenu(520, 600, menuhBox3, 10, 0);
        startAnimation(menuhBox3);


        return root;
    }

    /**
     * Permet de convertir les elements d'une HBox en Nodes
     * @param menuBox une HBox contenant les elements du menu
     */
    public static void startAnimation(HBox menuBox){

            for (int i =0; i< menuBox.getChildren().size(); i++){
                Node node = menuBox.getChildren().get(i);
                node.setClip(null);
                
            }
        
    }

    /**
     * Permet d'ajouter un fond d'ecran a la scene
     * @param name un String representant le fond d'ecran
     */
    public static void addBackground(String name){

        ImageView imageView = new ImageView(new Image("/wallpapers/" + name)); // on cree un imageview du fond d'ecran
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        root.getChildren().add(imageView);
    }

    /**
     * Permet d'ajouter un titre a la scene
     */
    public static void addTitle(){
        SokoTitle title = new SokoTitle("SELECTION DU NIVEAU"); // on cree un nouveau titre
        title.setTranslateX(WIDTH/2-title.getTitleWidth() /2);
        title.setTranslateY(HEIGHT- (HEIGHT-60));
        root.getChildren().add(title);
    }


    /**
     * Permet de creer le menu de la selection de niveaux.
     * @param x un double representant la position horizontale initiale du menu
     * @param y un double representant la position verticale initiale du menu
     * @param menuBox une HBox contenant les items du menu
     * @param number un int representant le nombre max d'items dans le HBox
     * @param minBound un int representant le nombre min d'items dans le HBox
     */
    public static void addMenu(double x, double y, HBox menuBox, int number, int minBound){
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);

        // cette section permet de creer les boutons non imagés
        if (menuBox == menuhBox3){
            SokoMenuItem item = new SokoMenuItem(menuData.get(10).getKey(), 13);  // on recupere les clés (qui correspondent aux Strings)
            item.setOnAction(menuData.get(10).getValue()); // on recupere les valeurs (qui correspondent aux Runnables)

            Rectangle clip = new Rectangle(300, 30); // on cree un rectangle qui sera lié au niveau pour en faire un gros "bouton"
            item.setClip(clip);

            SokoMenuItem item2 = new SokoMenuItem(menuData.get(11).getKey(), 13);
            item2.setOnAction(menuData.get(11).getValue());

            Rectangle clip2 = new Rectangle(300, 30);
            item2.setClip(clip2);

            menuBox.getChildren().addAll(item, item2);

        } else {
            // permet de créer les boutons des niveaux
            for (int i =minBound; i<number; i++){

                SokoLevelItem item = null;
                try {
                    item = new SokoLevelItem(menuData.get(i).getKey());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                item.setOnAction(menuData.get(i).getValue());
                Rectangle clip = new Rectangle(300, 30);
                item.setClip(clip);
                menuBox.getChildren().addAll(item);
                
            }
        }
    
        root.getChildren().add(menuBox);
    }
    
}