package maingame;

import java.util.Scanner;
import utils.*;
import windows.*;

public class LogicApp{

    /**
     * La fonction principale pour jouer.
     * @param args
     */
    public static void main(String[] args){ 
        
        play(args[0]);                 
    }

    /**
     * Lancement du jeu.
     * @param level Un String du nom du niveau.
     */
    public static void play(String level){
        // On crée la map avec le fichier correspondant
        String path = System.getProperty("user.dir") + "/src/main/resources/inputlevels/";
        String pathGame = path + level;


        Map carte = new Map(pathGame);
        printTab(carte, carte.personnage);
        boolean win = false; 
        while(!win){
            System.out.println("Direction ? (utilisez les points cardinaux : n, s, e, w");
            Scanner in = new Scanner(System.in); 
            String s = in.nextLine(); // permet de chopper l'entree de l'utilisateur
            char direction = s.charAt(0); // prend le char entré
            String[] nextDir = carte.personnage.nextDir(carte, direction); //Déplace le perso si sol dans la direction
            if(nextDir[0].equals("s")){ // si la direction (nextDir) est un 's' == ' '
                carte.personnage.move(direction); // on déplace le personnage 
            }else if(nextDir[0].equals("b")){ //Déplace la boite et le prsonnage si il n'y a pas de mur en face de la boite
                if(carte.boites.get(Integer.parseInt(nextDir[1])).canBePushed(carte, direction)){ // on vérifie que la boite puisse etre poussée
                    carte.boites.get(Integer.parseInt(nextDir[1])).move(direction); // on déplace la boite
                    carte.personnage.move(direction);
                }
            }

            win = true;
            for(int i = 0; i<carte.boites.size();i++){
                if(!carte.boites.get(i).checkPoint(carte)){
                    win = false;
                }
            }
            printTab(carte, carte.personnage); 
        }

        System.out.println("Vous avez gagné");
    }




    /**
     * Permet d'afficher l'état du jeu actuel.
     * @param map La map du jeu.
     * @param perso Le personnage et sa position.
     */
    public static void printTab(Map map, SokoCharacter perso){
        // On crée un tableau de char qui sera affiché par la suite
        char tabPrint[][] = new char[map.tab.length][map.tab[0].length];
        // On remplit le tableau avec les caractères correspondants aux cases
        for(int i = 0; i < map.tab.length; i++){
            for(int j = 0; j < map.tab[i].length; j++){
                tabPrint[i][j] = map.tab[i][j].type;
            }
        }
        
        // On rajoute le personnages et les boites en fonction de la case sur laquelle ils se trouvent
            if(tabPrint[map.personnage.getLine()][map.personnage.getCol()] == 's'){
                tabPrint[map.personnage.getLine()][map.personnage.getCol()] = 'c'; // Sur sol
            }else{
                tabPrint[map.personnage.getLine()][map.personnage.getCol()] = '+'; // Sur point
            }

            for(int i = 0; i < map.boites.size(); i++){
                if(tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()] == 's'){
                    tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()] = 'b'; // Sur sol
                }else{
                    tabPrint[map.boites.get(i).getLine()][map.boites.get(i).getCol()] = '*'; // Sur point
                }
            }
        // On parcourt le tableau et on affiche chaque caractère en fonction de son type
        for(int i = 0; i < tabPrint.length; i++){
            for(int j = 0; j < tabPrint[i].length; j++){
                switch(tabPrint[i][j]){
                    case 's': System.out.print(" "); break;
                    case 'v': System.out.print(" "); break;
                    case 'm': System.out.print("#"); break;
                    case 'p': System.out.print("."); break;
                    case 'b': System.out.print("$"); break;
                    case 'c': System.out.print("@"); break;
		    case '+': System.out.print("+"); break;
                    case '*': System.out.print("*"); break;
                }
            }
            System.out.println("");
        }
    }
}
