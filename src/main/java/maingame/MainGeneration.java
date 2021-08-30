package maingame;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import utils.*;
import windows.*;


public class MainGeneration {

    public  int height, length, box_number;
    public static String[] moves; // permet d'avoir les mouvements effectués par le robot lors de la génération des niveaux
    public static int generationCount = 0;

    /**
     * Fonction main de la génération de la map aléatoire.
     * @param args
     * @throws InterruptedException
     */
    public static void main (String[] args) throws InterruptedException{
        start();
    }

    /**
     * Lance la génération
     */
    public static void start(){
        try{
            // on definit les dimensions et le nombre de boites selon les TEXTFIELDS du menu graphique
            int height =heightSelect();
            int length = lengthSelect();
            int box_number = boxSelect();
           // si la taille est nulle on abandonne
            if (height ==0 || length == 0 || box_number == 0){
                return;
            }
            char [][] map = init(height, length, box_number); // on cree la map
            int counter = 0;
            while(isCorrect(map, box_number) != true || counter < 35){
                map = init(height, length, box_number); // on relance la création de map tant que le nombre de boites n'est pas correct
                counter++;
            }
            mapOutput(map);

       

        } catch (NumberFormatException ignored){}
        
    }

    /**
     * Permet de definir la hauteur du niveau.
     * @return un int
     */
    public static int  heightSelect(){
        int height = Integer.valueOf(GenerationScene.heiField.getText());
     
        if (height < 6 || height > 20){
            Alert errorAlert = new Alert(AlertType.ERROR);  // si la taille n'est pas bonne on cree une fenetre d'alerte
            errorAlert.setHeaderText("Entr\u00e9e invalide");
            errorAlert.setContentText("Veuillez entrer une hauteur correcte.");
            Optional<ButtonType> result = errorAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                GenerationScene.heiField.clear();
                return 0;
            }
        }
        return height;
    }

    /**
     * Permet de definir la longueur du niveau.
     * @return un int
     */
    public static int  lengthSelect(){
        int length = Integer.valueOf(GenerationScene.lenField.getText());
        
        if (length < 6 || length > 20){ // si la taille n'est pas bonne on cree une fenetre d'alerte
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Entr\u00e9e invalide");
            errorAlert.setContentText("Veuillez entrer une longueur correcte.");
            Optional<ButtonType> result = errorAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                GenerationScene.lenField.clear();
                return 0;
                
            }
        }
            
        return length;
    }

    /**
     * Permet de definir le nombre de boites dans le niveau.
     * @return un int
     */
    public static int boxSelect(){
        int box_number = Integer.valueOf(GenerationScene.boxesField.getText()); 
        if (box_number < 1 || box_number>15 ){ // si le nombre de boites n'est pas bon on cree une fenetre d'alerte
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Entr\u00e9e invalide");
            errorAlert.setContentText("Veuillez entrer un bombre de boites correcte.");
            Optional<ButtonType> result = errorAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                GenerationScene.boxesField.clear();
                return 0;
                
            }
        }
        return box_number;
    }
 



    /**
     * Permet seulement d'afficher la map.
     * @param map La carte.
     */
    public static void printMappp(char[][] map){
        for (int i=0; i<map.length; i++){
            for (int a=0; a< map[0].length; a++){
                System.out.print(map[i][a]);
            }
            System.out.println();

        }
    }

    /**
     * Retourne la map finale.
     * 
     * @return La map finale et fonctionnelle.
     */
    public static char[][] init(int height, int length, int box_number){
         
        GeneratedMap map = new GeneratedMap(height, length, box_number); 
        int[] positions = map.positionnement;
        char [][] newCarte = randomMove(map, positions, height, length);
        char [][] finalCarte = goodOutput(newCarte);
        return finalCarte;
        
    }

    /**
     * Donne la direction de manière aléatoire pour le robot.
     * @return un charactère indiquant la direction.
     */
    public static String direction(){
        Random random = new Random();
        int new_random = random.nextInt(4);
        String s = " "; 
        switch(new_random){
            case 0 : s = "s"; break;
            case 1 : s = "n"; break;
            case 2 : s = "w"; break;
            case 3 : s = "e"; break; 
        }
        
        String direction = s;
        return direction;
    }

    /**
     * Permet de faire les mouvements du robot pour la génération de la map.
     * @param carte 
     * @return la carte après les mouvements du robot.
     */
    public static char[][] randomMove(GeneratedMap carte, int[] positions, int height, int length){ 


        // IL FAUT POUVOIR SAUVEGARDER LES EMPLACEMENTS DE BASE DES OBJETS 
        height = carte.height;
        length = carte.length;

        
        int counter = 0;
        int maxi = 0; // nombre de déplacements. Il faudrait la choisir en fonction de la taille de la map.
        if (height <= 9 || length <= 9){
            maxi = 90;
        } else if (( 9<height && height <=13) && ( 9<length && length <=13) ){
            maxi = 130;
        }else {
            maxi = 210;
        }
        moves = new String[maxi];
        String s =" ";

        while(counter < maxi){
            s = direction();
            char direction = s.charAt(0); // prend le char entré
            String[] nextDir = carte.personnage.nextDirG(carte, direction); //Déplace le perso si sol dans la direction
            if(nextDir[0].equals("s")){ // si la direction (nextDir) est un 's' == ' '

                carte.personnage.move(direction);
                carte.tab[carte.personnage.getLine()][carte.personnage.getCol()] = new Case('k'); // on déplace le personnage
                moves[counter] = s; 
                
                
            } else if (nextDir[0].equals("b")){ //Déplace la boite et le personnage si il n'y a pas de mur en face de la boite

                if (carte.boites.get(Integer.parseInt(nextDir[1])).canBePushedG(carte, direction)){ // on vérifie que la boite puisse etre poussée
                    carte.boites.get(Integer.parseInt(nextDir[1])).move(direction); // on déplace la boite
                    carte.personnage.move(direction);
                    carte.tab[carte.personnage.getLine()][carte.personnage.getCol()] = new Case('k');
                    moves[counter] = s;

                } else {
                    moves[counter] =" ";
                }
            } else {
                moves[counter] =" ";
            }
            counter ++;
        }

        char[][] newMap = randomMap(carte, carte.personnage, positions);
        return newMap;
        
    }


    /**
     * Permet de sauvegarder la map finale avant sa conversion en caractères directs.
     * @param map la carte suivant les mouvements du robot.
     * @param perso Le robot.
     * @return La map finale 
     */
    public static char[][] randomMap(GeneratedMap map, SokoCharacter perso, int[] positions){
        

        char tabPrint[][] = new char[map.tab.length][map.tab[0].length]; // on créé un nouveau tableau de la taille de la map après les mouvements 
        
        for(int i = 0; i < map.tab.length; i++){
            for(int j = 0; j < map.tab[i].length; j++){
                tabPrint[i][j] = map.tab[i][j].type;    // on rempli le nouveau tableau avec les char de la map après les mouvements du robot
            }
        }

        tabPrint[perso.getLine()][perso.getCol()] = 'c';  // on convertit le char sous le perso en 'c' 
        
        for(int i = 0; i < map.boites.size(); i++){
            tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()] = 'b';  // on remplace les char de bases par des char de boites
        }

        tabPrint[perso.getLine()][perso.getCol()] = 'k'; // on change la position finale du perso en case vide
        for (int i = 0; i<tabPrint.length; i++){
            for (int a = 0; a<tabPrint[0].length; a++){
                if (tabPrint[i][a] == 'b'){
                    tabPrint[i][a] = 'p'; // pour la map finale, on change les emplacements finaux des boites en points d'objectifs                   
                       
                } 
            }
        }

        tabPrint[positions[0]][positions[1]] = 'c';
        for (int i = 2; i<positions.length; i += 2){
            switch(tabPrint[positions[i]][positions[i+1]]){
                case 'b' : tabPrint[positions[i]][positions[i+1]] = 'k'; break;
                case 'p' : tabPrint[positions[i]][positions[i+1]] = 'k'; break;
                case 'k' : tabPrint[positions[i]][positions[i+1]] = 'b'; break;
            }
        }

        return tabPrint; 

    }


    

    /**
     * Permet de créer l'output avec les bons caractères plutot que ceux personnalisés.
     * @param newMap la carte finale.
     * @return Un tableau avec les caractères convertis.
     */
    public static char[][] goodOutput(char[][] newMap){
        char[][] goodMap = new char[newMap.length][newMap[0].length];
        for (int i = 0; i< goodMap.length; i++){
            for (int a = 0; a< goodMap[0].length; a++){
                switch(newMap[i][a]){
                    case 'm': goodMap[i][a] = '#'; break;
                    case 's': goodMap[i][a] = '#'; break;
                    case 'c': goodMap[i][a] = '@'; break;
                    case 'p': goodMap[i][a] = '.'; break;
                    case 'k': goodMap[i][a] = ' '; break;
                    case 'b': goodMap[i][a] = '$'; break;
                }
            }           
        }
        return goodMap;
    }


   
    /**
     * Permet de vérifier qu'il y a autant de points d'objectifs que de boites.
     * Dans le cas contraire, on recommence une génération.
     * @param finalMap La carte finale avec les caractères convertis.
     * @return Vrai ou faux en fonction de la faisabilité de la carte.
     */
    public static boolean isCorrect(char[][] finalMap, int box_number){
        int pointsCounter = 0, boxCounter =0;
        for (int i = 0; i< finalMap.length; i++){
            for (int a =0; a<finalMap[0].length; a++){
                switch(finalMap[i][a]){
                    case '.' : pointsCounter++; break;
                    case '$' : boxCounter++; break;
                }
            }
        }
        if (pointsCounter != boxCounter /*|| boxCounter < 2 || mapEffectiveness(finalMap) < 3*/ || boxCounter != box_number){
            return false;
        } return true; 
    }

    // ajouter une methode pour calcul de la viabilité et de l'effectiveness

    /**
     * Permet de déterminer la distance entre 2 points (des boites en l'occurence).
     * @param map On prend une carte en entrée.
     * @return Un double permettant d'avoir la distance entre les deux boites.
     * 
     */

    /**
     * Permet de definir une efficacite de la map(a quel point elle est interessante) CETTE METHODE N'EST PLUS UTILISEE
     * @param map un char[][] representant la carte generee
     * @return un double
     */
    public static double mapEffectiveness(char[][] map){
        int x1=0, y1=0, x2=0, y2=0, counter = 0;
        double distance =0;
        for (int i=0; i< map.length; i++){
            for (int a=0; a< map[0].length; a++){
                if (map[i][a]=='.' && counter == 0){
                    x1 = i;
                    y1 = a;
                    counter++;
                } else if (map[i][a] == '.' && counter ==1){  // idée : stocker tous les col et distances dans des 
                    x2 = i;
                    y2 = a;
                }
            }
        }
        distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return distance;
        
    }

    
    /**
     * Permet de créér le fichier .xsb avec la map générée automatiquement.
     * @param map La carte finale.
     */
    public static void mapOutput(char[][] map){
  
        String path = System.getProperty("user.dir") + "/src/main/resources/customlevels/";
        String gen = "generated"; // ce string permettra de creer un nom automatiquement si celui ci n'est pas precise lors de la generation
        File folder = new File(path);
        File[] files = folder.listFiles();
        int bound =0;
        if(files != null){
            bound = files.length;   // si le dossier customlevels n'est pas nul on prend sa taille
        }
        
        
        String number = String.valueOf(bound + 1);
        String ext = ".xsb"; // on rajoute l'extension
        String fileName = "";
        if (GenerationScene.nameField.getText() == null){ // si auncun nom n'est precise on en utilise un automatiquement
            fileName = gen + number + ext;
        } else {
            fileName = GenerationScene.nameField.getText() + ext;
        }
        
        
        
        
            
            File file = new File(path + fileName);
            
            if (file.exists()){  // si le fichier existe deja on renvoie une erreur
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setHeaderText("Fichier existant.");
                errorAlert.setContentText("un fichier avec ce nom existe deja.");
                Optional<ButtonType> result = errorAlert.showAndWait();
                if (result.get() == ButtonType.OK){
                    GenerationScene.nameField.clear();               
                }
            }
          

          try {
            // PERMET D'ECRIRE LA CARTE DANS LE FILE VIDE
            FileWriter myWriter = new FileWriter(path + fileName);
            for (int x = 0; x<map.length; x++){
            for (int a = 0; a<map[0].length; a++) { 
                myWriter.write(map[x][a]);  // permet d'écrire les caractères dans le fichier final
            }
            myWriter.write("\n");
            }
            myWriter.write("Author : Custom.");
            
            GenerationScene.heiField.clear();
            GenerationScene.lenField.clear();
            GenerationScene.boxesField.clear();
            GenerationScene.nameField.clear();
            myWriter.write("\n");
            myWriter.close();
            
 

            } catch (IOException e) {
                System.out.println("Une erreur est survenue.");
                e.printStackTrace();
            }
    }
}
