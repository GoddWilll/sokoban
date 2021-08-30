package maingame;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  

import javafx.util.Duration;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

import javafx.scene.Node;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;

import utils.*;
import windows.*;



public class MainGame {

    private static String pathImage = System.getProperty("user.dir") + "/src/main/resources/";
    private static String pathLevel = System.getProperty("user.dir") + "/src/main/resources/";

    private static double cote;
    private static VBox side;

    public static String level = LevelSelect.plevel;
    private static Line line;
    private static String folder1;
    public static char[][] departMap;
    static String path = getWallpaper();
    public static Map map;



    /**
     * Permet de définir le niveau sur lequel on joue
     * @param level Le nom du niveau
     */
    public static void setLevel(String level) {
        MainGame.level = level;
    }


    /**
     * Lance la fenêtre de jeu
     * @param level1 Le nom du niveau
     * @param folder Le nom du dossier
     */
    public static BorderPane lancement(String level1, String folder) throws Exception{
        // On définit le niveau et on initialise les Layouts
        level =  level1;
        folder1 = folder;
        GridPane grid;
        BorderPane root = new BorderPane();
        addBackground(root);

        // On crée la map avec le fichier
        map = new Map(pathLevel + folder +level1);


        // Afin d'avoir un affichage optimal, on calcule un ratio entre la longueur et la hauteur de la carte
        // pour que le côté des cases varie selon la taille de la carte
        int hauteur = map.tab.length;
        int longueur = map.tab[0].length;
        double ratio = hauteur/longueur;

        if (ratio > 1){
            cote = (550/hauteur)-1 ;
        } else if (ratio < 1){
            cote =  850/longueur*0.7;
        } else if (ratio == 1){
            cote = 550/hauteur;
        }


        // On fait un sauvegarde de la map de départ pour pouvoir recommencer plus tard
        departMap = createSave(map);
        grid = makeGrid(map, departMap);

        // On crée les boutons fléchés en commencant par charger les images
        FileInputStream inputstream = new FileInputStream(pathImage + "arrowN.png");
        ImageView viewN = new ImageView(new Image(inputstream));
        viewN.setFitHeight(50);
        viewN.setFitWidth(50);

        FileInputStream inputstream2 = new FileInputStream(pathImage + "arrowE.png");
        ImageView viewE = new ImageView(new Image(inputstream2));
        viewE.setFitHeight(50);
        viewE.setFitWidth(50);

        FileInputStream inputstream3 = new FileInputStream(pathImage + "arrowS.png");
        ImageView viewS = new ImageView(new Image(inputstream3));
        viewS.setFitHeight(50);
        viewS.setFitWidth(50);

        FileInputStream inputstream4 = new FileInputStream(pathImage + "arrowW.png");
        ImageView viewW = new ImageView(new Image(inputstream4));
        viewW.setFitHeight(50);
        viewW.setFitWidth(50);

        // On rend les images cliquables et on leur attribue l'action qu'elles effectuent
        viewN.setPickOnBounds(true);
        viewN.setOnMouseClicked((MouseEvent e) -> {
            try{
                move(map,'n', root);
            }catch(Exception error){
                error.printStackTrace();
            }
        });

        viewE.setPickOnBounds(true);
        viewE.setOnMouseClicked((MouseEvent e) -> {
            try{
                move(map,'e', root);
            }catch(Exception er){
                er.printStackTrace();
            }
        });

        viewS.setPickOnBounds(true);
        viewS.setOnMouseClicked((MouseEvent e) -> {
            try{
                move(map,'s', root);
            }catch(Exception err){
                err.printStackTrace();
            }
        });

        viewW.setPickOnBounds(true);
        viewW.setOnMouseClicked((MouseEvent e) -> {
            try{
                move(map,'w', root);
            }catch(Exception erro){
                erro.printStackTrace();
            }
        });

        // On ajoute les fleches dans un GridPane
        GridPane fleches = new GridPane();


        fleches.add(viewE,2,1);
        fleches.add(viewS,1,2);
        fleches.add(viewW,0,1);
        fleches.add(viewN,1,0);

        fleches.setHgap(-1);
        fleches.setVgap(-1);

        side = new VBox();

        fleches.setAlignment(Pos.CENTER);

        side.getChildren().add(fleches);

        side.setSpacing(50);
        side.setAlignment(Pos.CENTER);


        // On crée le titre du niveau en prenant le nom du fichier auquel on retire le .xsb

        String name = level.replaceFirst("[.][^.]+$", ""). replace('_', ' ');;
        String spread ="";
        for (char c : name.toCharArray()){
            spread += c + " "; // On espace les lettres et on les ajoute au fur et à mesure
        }


        Text title = new Text(spread);
        title.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 48)); // Police d'écriture
        title.setFill(Color.rgb(229, 193, 0));
        title.setEffect(new DropShadow(30, Color.WHITE)); // Effet appliqué au texte

        title.setTranslateX(1280/2-title.getLayoutBounds().getWidth() /2);



        // On crée des insets pour espacer les éléments
        Insets insets1 = new Insets(50);
        Insets insets2 = new Insets(75);
        Rectangle rect = new Rectangle(50, 50);
        rect.setFill(Color.TRANSPARENT);


        // On ajoute au BorderPane tout les éléments
        root.setLeft(rect);

        root.setTop(title);

        root.setCenter(grid);
        root.setRight(side);

        root.setMargin(title, insets1);
        root.setMargin(rect, insets2);
        root.setMargin(side, insets1);

        return root;
    }

    /**
     * Retourne une image de la carte
     * @param caseType Un String qui est le type de la case
     * @return Une ImageView qui est l'image demandée
     */
    public static ImageView loadImage(String caseType){
        // On charge l'image et on la retourne
        FileInputStream inputstream;
        try {
            if (caseType.equals("solPerso")){
                inputstream = new FileInputStream(pathImage + caseType +".png");
                ImageView imagesol = new ImageView(new Image(inputstream)); imagesol.setFitHeight(cote); imagesol.setFitWidth(cote);
                return imagesol;
            } else {
                inputstream = new FileInputStream(pathImage + caseType +".jpg");
                ImageView imagesol = new ImageView(new Image(inputstream)); imagesol.setFitHeight(cote); imagesol.setFitWidth(cote);
                return imagesol;
            }

        } catch (FileNotFoundException ignored) {
        }
        return null;
    }


    /**
     * Crée un tableau vide qui servira de sauvegarde
     * @param map Un objet Map que l'on souhaite sauvegarder
     * @return Le tableau vide de même taille que map
     */
    public static char[][] createSave(Map map){
        char tabPrint[][] = new char[map.tab.length][map.tab[0].length];
        return tabPrint;
    }

    /**
     * Crée un le GridPane qui contient toutes les images
     * @param map Un objet Map que l'on souhaite afficher
     * @param departMap Un tableau 2 dimensions de char qui est a sauvegarde de la map
     * @return Le GridPane avec toutes les images
     */
    public static GridPane makeGrid(Map map, char[][] departMap) throws FileNotFoundException{
        // On crée le GridPane
        GridPane root = new GridPane();

        // On transforme la map en un tableau 2d de char afin d'y ajouter le personnages et les boites
        // pour ensuite leur assigner une image dans le GridPane
        char tabPrint[][] = new char[map.tab.length][map.tab[0].length];

        for(int i = 0; i < map.tab.length; i++){
            for(int j = 0; j < map.tab[i].length; j++){

                tabPrint[i][j] = map.tab[i][j].type;
                departMap[i][j] = map.tab[i][j].type;

            }

        }

        switch(tabPrint[map.personnage.getLine()][map.personnage.getCol()]){
            case 's': tabPrint[map.personnage.getLine()][map.personnage.getCol()] = 'c'; break;
            case 'p': tabPrint[map.personnage.getLine()][map.personnage.getCol()] = '+'; break;
        }

        for(int i = 0; i < map.boites.size(); i++){
            switch(tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()]){
                case 's': tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()] = 'b'; break;
                case 'p': tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()] = '*'; break;
            }
        }
        // On remplit la sauvegarde
        for(int i = 0; i < tabPrint.length; i++){
            for(int j = 0; j < tabPrint[i].length; j++){
                switch(tabPrint[i][j]){
                    case 's': departMap[i][j] = ' '; break;
                    case 'v': departMap[i][j] = ' '; break;
                    case 'm': departMap[i][j] = '#'; break;
                    case 'p': departMap[i][j] = '.'; break;
                    case 'b': departMap[i][j] = '$'; break;
                    case 'c': departMap[i][j] = '@'; break;
                    case '*': departMap[i][j] = '*'; break;
                    case '+': departMap[i][j] = '+'; break;
                }
            }

        }

        // On remplit le GridPane des images correspondantes à la carte
        for(int i = 0; i<map.tab.length;i++){
            for(int j = 0; j<map.tab[i].length; j++){
                switch(tabPrint[i][j]){
                    case 's': root.add(loadImage("sol"),j,i); break;
                    case 'v': root.add(new Label(" "),j,i); break;
                    case 'm': root.add(loadImage("mur"),j,i); break;
                    case 'p': root.add(loadImage("point"),j,i); break;
                    case 'c': root.add(loadImage("solPerso"),j,i); break;
                    case '+': root.add(loadImage("pointPerso"),j,i); break;
                    case 'b': root.add(loadImage("solBoite"),j,i); break;
                    case '*': root.add(loadImage("pointBoite"),j,i); break;
                }
            }
        }

        root.setHgap(-1);
        root.setVgap(-1);
        return root;
    }


    /**
     * Crée un Pane pour y ajouter le fond d'écran
     * @return Le Pane avec le fond d'écran ajouté
     */
    public static Pane createPane(){
        Pane root = new Pane();
        addBackground(root);
        return root;
    }

    /**
     * Permet de modifier un fichier xsb pour le marquer comme fait (Pour débloquer le niveau d'après)
     * @param file Un String qui est le nom du fichier
     */
    public static void done(String file) throws Exception{
        // Même méthode que Map.done() sauf qu'ici on prend un String en paramètre
        FileWriter writer = null;
        try{
            writer = new FileWriter(file, true);
            writer.write("Done");
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
        writer.close();
    }

    /**
     * Permet de déplacer le personnage si toutes les conditions sont respectées et vérifie si la partie est gagnée
     * @param map Un objet map qui est la carte
     * @param direction Un char qui représente la direction dans laquelle on souhaite déplacer le personnage
     * @param root Le BorderPane auquel on change la grille
     */
    public static void move(Map map, char direction, BorderPane root) throws Exception{

        String[] nextDir = map.personnage.nextDir(map, direction); // On récupère l'élément qui se trouve dans la direction du personnage

        if(nextDir[0].equals("s")){ //Déplace le personnage si il y a un sol dans la direction
            map.personnage.move(direction);
        }else if(nextDir[0].equals("b")){ //Déplace la boite et le prsonnage si il n'y a pas de mur ou de boite en face de la celle-ci
            if(map.boites.get(Integer.parseInt(nextDir[1])).canBePushed(map, direction)){
                map.boites.get(Integer.parseInt(nextDir[1])).move(direction);
                map.personnage.move(direction);
            }
        }

        // On vérifie si toutes les boites sont sur des points pour déterminer si la partie est finie ou non
        boolean win = true;
        for(int i = 0; i<map.boites.size();i++){
            if(!map.boites.get(i).checkPoint(map)){
                win = false;
            }
        }

        GridPane grid;

        // On crée la grille avec les images et on la met dans le BorderPane
        grid = makeGrid(map, departMap);

        root.setCenter(grid);


        if(win){ // Si la partie est gagnée, on marque le niveau comme fait (S'il ne l'a pas déjà été)
            if(!map.isDone(map.filename)){
                map.done();
            }

            String path1 = System.getProperty("user.dir") + "\\src\\main\\resources\\levels\\";
            WinWindow.display(level.replaceFirst("[.][^.]+$", "").replace('_', ' ') +" reussi !!", folder1); // On lance une fenêtre qui permet de quitter la partie
            Path path = FileSystems.getDefault().getPath(pathLevel + folder1 + level.replaceFirst("[.][^.]+$", "")+"-save.xsb");
            Path path2 = FileSystems.getDefault().getPath(pathLevel + folder1 + level);

            try{ // Si la map est une sauvegarde, on marque le niveau d'origine comme fait et on supprime la sauvegarde
                if(level.replaceFirst("[.][^.]+$", "").substring(level.replaceFirst("[.][^.]+$", "").length()-4, level.replaceFirst("[.][^.]+$", "").length()).equals("save")){
                    done(path1 + level.replaceFirst("[.][^.]+$", "").substring(0, level.replaceFirst("[.][^.]+$", "").length()-5)+".xsb");
                    System.out.println(path1 + level.replaceFirst("[.][^.]+$", "").substring(0, level.replaceFirst("[.][^.]+$", "").length()-5)+".xsb");
                    System.out.println(path2);
                    Files.delete(path2);
                } else {
                    Files.delete(path);
                }

            } catch (NoSuchFileException ignored){}

        }
    }


    /**
     * Ajoute un fond d'écran a un Pane
     * @param root Le Pane auquel on va ajouter le fond d'écran
     */
    public static void addBackground(Pane root){
        String path1 = "file:///" + System.getProperty("user.dir") + "/src/main/resources/wallpapers/ingamewallpapers/";
        ImageView imageView = new ImageView(new Image(path1 + path));
        imageView.setFitHeight(720);
        imageView.setFitWidth(1280);
        root.getChildren().add(imageView);
    }

    /**
     * Ajoute le titre du niveau au Pane
     * @param level Un String qui représente le nom du niveau
     * @param root Le Pane auquel on va ajouter le titre
     */
    public static void addTitle(String level, Pane root){
        SokoTitle title = new SokoTitle(level.replaceFirst("[.][^.]+$", "").replace('_', ' '));
        title.setTranslateX(1280/2-title.getTitleWidth() /2);
        title.setTranslateY(700);
        root.getChildren().add(title);
    }

    /**
     * Lance l'animation des boutons Recommencer, Sauvegarder et Quitter
     * @param menuBox Une VBox qui contient les boutons
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
     * Permet de selectionner un fond d'écran aléatoirement dans le dossier.
     * @return Un String donnant le fond d'écran.
     */
    public static String getWallpaper(){
        String path = System.getProperty("user.dir") + "/src/main/resources/wallpapers/ingamewallpapers/"; // On récupère le fond d'écran
        File folder = new File(path); // Fichier wallpaper
        File[] files = folder.listFiles(); // Liste de fichiers
        Random random = new Random();
        int bound = files.length; // On récupère le nombre de fichiers
        int number = random.nextInt(bound); // on choisit un nombre aléatoire
        return files[number].getName(); // On recupere le nom du n-ieme fichier
    }

}