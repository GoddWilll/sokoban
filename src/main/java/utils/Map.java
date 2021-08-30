package utils;

import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import maingame.*;
import windows.*;


public class Map{
    public Case tab[][];
    public ArrayList<Box> boites = new ArrayList<Box>();
    public SokoCharacter personnage;
    public File file;
    public String filename;
    private static String pathMoves = System.getProperty("user.dir") + "/src/main/resources/moves/";
    private static String pathLevel = System.getProperty("user.dir") + "/src/main/resources/levels/";


    /**
     * Permet de créer la map à partir d'un fichier xsb
     * @param filename le nom du fichier.
     */
    public Map(String filename){
        file = new File(filename);
        this.filename = filename;

        try{
            // On charge le fichier doné en paramètre
            FileReader filereadermax = new FileReader(file);
            BufferedReader readermax = new BufferedReader(filereadermax);
            String line = readermax.readLine();
            // On parcourt toute la map pour déterminer les dimensions
            int maxwidth = 0;
            int maxheight = 0;
            while(!((int) line.charAt(0) >= 65 && (int) line.charAt(0) <= 90)){ // On lit les lignes jusqu'à arriver à une lettre majuscule
                if(line.length() > maxwidth){
                    maxwidth = line.length();
                }
                line = readermax.readLine();
                maxheight++;
            }

            readermax.close();

            tab = new Case[maxheight][maxwidth];

            //On lit une nouvelle fois le fichier pour remplir le tableau de Case par chaque élément du décor
            FileReader filereader = new FileReader(file);
            BufferedReader reader = new BufferedReader(filereader);
            line = reader.readLine();
            int i = 0;

            boolean videgauche = false;
            boolean videdroite = false;
            while(!((int) line.charAt(0) >= 65 && (int) line.charAt(0) <= 90)){ // On lit les lignes jusqu'à arriver à une lettre majuscule
                if(line.length() < maxwidth){
                    line = line + new String(new char[maxwidth-line.length()]).replace("\0", " "); // On complète la ligne par des " " afin d'avoir une carte rectangulaire
                }
                String[] carac = line.split("");
                // On lit les lignes par la gauche, jusqu'au milieu et de la droite jusqu'au milieu.
                // Cela sert a déterminer si un " " est un décor vide ou un décor sol
                // Pour chaque caractère, on crée un objet de Case en fonction de celui ci
                for(int j = 0; j<carac.length/2;j++){
                    switch(carac[j]){
                        case "#": tab[i][j] = new Case('m'); videgauche = true; break;
                        case ".": tab[i][j] = new Case('p'); break;
                        case "$": tab[i][j] = new Case('s'); boites.add(new Box(i,j)); break;
                        case "*": tab[i][j] = new Case('p'); boites.add(new Box(i,j)); break;
                        case "@": tab[i][j] = new Case('s'); personnage = new SokoCharacter(i,j); break;
                        case "+": tab[i][j] = new Case('p'); personnage = new SokoCharacter(i,j); break;
                        case " ": if(videgauche == false){ tab[i][j] = new Case('v');}else{tab[i][j] = new Case('s');} break;
                    }
                }
                videgauche = false;
                for(int k = carac.length-1; k>(carac.length/2)-1;k--){
                    switch(carac[k]){
                        case "#": tab[i][k] = new Case('m'); videdroite = true; break;
                        case ".": tab[i][k] = new Case('p'); break;
                        case "$": tab[i][k] = new Case('s'); boites.add(new Box(i,k)); break;
                        case "*": tab[i][k] = new Case('p'); boites.add(new Box(i,k)); break;
                        case "@": tab[i][k] = new Case('s'); personnage = new SokoCharacter(i,k); break;
                        case "+": tab[i][k] = new Case('p'); personnage = new SokoCharacter(i,k); break;
                        case " ": if(videdroite == false){ tab[i][k] = new Case('v');}else{tab[i][k] = new Case('s');} break;
                    }
                }
                videdroite = false;
                i++;
                line = reader.readLine();
            }

            reader.close();
        } catch(IOException e){
            e.printStackTrace(); // Lance une exception si il y a un problème de fichier
        }
    }

    /**
     * Permet de retourner une map a partir d'une map d'origine, sur laquelle on a effectué des mouvements
     * @param map La Map sur laquelle on applique les mouvements
     * @param pathMemory Une ArrayList de String qui représente chaque mouvement par une direction "n","s","e" ou "w" pour north, south, east, west
     * @return Un objet Map qui est la map avec les déplacements appliqués
     */
    public static Map makeMoves(Map map, ArrayList<String> pathMemory){

        for(int i = 0; i<pathMemory.size();i++){
            char direction = pathMemory.get(i).charAt(0);
            String[] nextDir = map.personnage.nextDir(map, direction);
            if(nextDir[0].equals("s")){ //Déplace le perso si sol dans la direction
                map.personnage.move(direction);
            }else if(nextDir[0].equals("b")){ //Déplace la boite et le prsonnage si il n'y a pas de mur en face de la boite
                if(map.boites.get(Integer.parseInt(nextDir[1])).canBePushed(map, direction)){
                    map.boites.get(Integer.parseInt(nextDir[1])).move(direction);
                    map.personnage.move(direction);
                }
            }
        }
        return map;
    }

    /**
     * Permet d'appliquer un fichier .mov sur un fichier .xsb et de donner le resultat en sortie.
     * @param inputXsb un String representant le niveau en entree
     * @param inputMov un String representant le fichier .mov
     * @param outputXsb un String representant le niveau en sortie
     */
    public static void inputMov(String inputXsb, String inputMov, String outputXsb){
        // On crée l'objet Map correspondant au fichier donné en input
        Map map = new Map(inputXsb);
        // On récupère touts les mouvements du fichier .mov et on les mets dans une arraylist
        ArrayList<String> pathMemory = new ArrayList<String>();
        try{
            FileReader filereader = new FileReader(inputMov);
            BufferedReader reader = new BufferedReader(filereader);
            String line = reader.readLine();
            for(int i = 0; i<line.length(); i++){
                pathMemory.add(String.valueOf(line.charAt(i)));
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        // On applique les mouvements sur la map
        Map mapMoved = makeMoves(map,pathMemory);
        // On crée le fichier de sortie et on écrit dedans la map et les informations
        try{
            File file = new File(outputXsb);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);

            // On crée un tableau de char qui représente le type des cases
            char tabPrint[][] = new char[map.tab.length][map.tab[0].length];

            for(int i = 0; i < mapMoved.tab.length; i++){
                for(int j = 0; j < mapMoved.tab[i].length; j++){
                    tabPrint[i][j] = mapMoved.tab[i][j].getType();
                }
            }

            // On rajoute le personnages et les boites en fonction de la case sur laquelle ils se trouvent
            if(tabPrint[mapMoved.personnage.getLine()][mapMoved.personnage.getCol()] == 's'){
                tabPrint[mapMoved.personnage.getLine()][mapMoved.personnage.getCol()] = 'c'; // Sur sol
            }else{
                tabPrint[mapMoved.personnage.getLine()][mapMoved.personnage.getCol()] = '+'; // Sur point
            }

            for(int i = 0; i < mapMoved.boites.size(); i++){
                if(tabPrint[mapMoved.boites.get(i).getLine()][mapMoved.boites.get(i).getCol()] == 's'){
                    tabPrint[mapMoved.boites.get(i).getLine()][mapMoved.boites.get(i).getCol()] = 'b'; // Sur sol
                }else{
                    tabPrint[mapMoved.boites.get(i).getLine()][mapMoved.boites.get(i).getCol()] = '*'; // Sur point
                }
            }

            // On met tout les caracètre dans un ligne puis on l'écrit dans le fichier de sortie et on passe à la suivante
            for(int i = 0; i < tabPrint.length; i++){
                String ligne = "";
                for(int j = 0; j < tabPrint[i].length; j++){
                    switch(tabPrint[i][j]){
                        case 's': ligne += " "; break;
                        case 'v': ligne += " "; break;
                        case 'm': ligne += "#"; break;
                        case 'p': ligne += "."; break;
                        case 'b': ligne += "$"; break;
                        case 'c': ligne += "@"; break;
                        case '+': ligne += "+"; break;
                        case '*': ligne += "*"; break;
                    }
                }
                bw.write(ligne);
                bw.newLine();
            }

            // On récupère les informations qu'il y a en fin de fichier en copiant la fin du fichier d'input à la fin du fichier output
            FileInputStream fileInput = new FileInputStream(inputXsb);
            Scanner scanner = new Scanner(fileInput);
            String lineD = scanner.nextLine();

            while(!((int) lineD.charAt(0) >= 65 && (int) lineD.charAt(0) <= 90)){ // On parcourt toutes les lignes jusqu'à arriver aux détails
                lineD = scanner.nextLine();
            }
            bw.write(lineD);
            while(scanner.hasNextLine()){
                lineD = scanner.nextLine();
                bw.write(lineD);
                bw.newLine();
            }

            bw.close();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Permet de modifier un fichier xsb pour le marquer comme fait (Pour débloquer le niveau d'après)
     */
    public void done() throws Exception{
        // On prend le fichier de l'objet Map et on le parcourt en entier, à la fin on rajoute "Done"
        FileWriter writer = null;
        try{
            writer = new FileWriter(file, true);

            writer.write("\nDone");
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }

        writer.close();

    }


    /**
     * Permet de vérifier si un niveau a déjà été fait
     * @param filename Un String qui est le nom du fichier
     * @return true si le niveau a déjà été fait, false sinon
     */
    public boolean isDone(String filename){
        // On lit tout le fichier jusqu'à arriver à la derniere ligne
        try{
            Scanner scanner = new Scanner(new File(filename));
            String line = null;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
            }
            // Si la dernière ligne est "Done", on retourne true
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
     * Permet de sauvegarder une partie en cours sous forme d'un fichier .mov
     * @param levelName Un String qui est le nom du niveau
     */
    public void save(String levelName){
        // On récupère la liste des mouvements du personnage puis on l'écrit d'un un nouveau fichier .mov
        try{
            String line = String.join("", this.personnage.pathMemory);
            String level = levelName.replaceFirst("[.][^.]+$", "");
            File save = new File(pathMoves + level +".mov");
            save.createNewFile();
            FileWriter fileWriter = new FileWriter(save);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(line);

            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Permet de charger une partie grâce à un fichier .mov
     * @param levelName Un String qui est le nom du niveau
     * @return Une Map avec les déplacements d'un fichier mov effectués sur un fichier .xsb correspondant
     */
    public Map loadSave(String levelName){
        // On crée la map puis on lit le fichier .mov pour ensuite appliquer le .mov sur le .xsb
        Map map = new Map(pathLevel + levelName +".xsb");
        try{
            File save = new File(pathMoves + levelName + ".mov");
            FileReader filereader = new FileReader(save);
            BufferedReader reader = new BufferedReader(filereader);

            String line = reader.readLine();

            ArrayList<String> movesMemory = new ArrayList<String>(Arrays.asList(line.split("")));
            reader.close();
            return makeMoves(map, movesMemory);
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }
}