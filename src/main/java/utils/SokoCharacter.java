package utils;
import maingame.*;
import windows.*;
import java.util.ArrayList;



public class SokoCharacter extends Mobile{
    public char orientation;
    public ArrayList<String> pathMemory = new ArrayList<String>();


    /**
     * Crée un nouveau personnage.
     * @param line La ligne sur laquelle le personnage est.
     * @param col La colonne sur laquelle le personnage est.
     */
    public SokoCharacter(int line, int col){
        super(line,col);
    }

    @Override
    /**
     * Permet de déplacer le personnage.
     */
    public void move(char dir){
        switch(dir){ // On modifie les valeurs de line ou de col en fonction de la direction souhaitée
            case 'n': line = line - 1; pathMemory.add("n"); break;
            case 's': line = line + 1; pathMemory.add("s"); break;
            case 'w': col = col - 1; pathMemory.add("w"); break;
            case 'e': col = col + 1; pathMemory.add("e"); break;
        }
    }


    /**
     * Permet de determiner l'objet dans la direction souhaitée.
     * @param map La carte du jeu.
     * @param dir La direction souhaitée.
     * @return On retourne un string de la direction.
     */
    public String[] nextDir(Map map, char dir){ //Retourne un caractère qui décrit l'élément dans un direction
        int colDir = 0;
        int lineDir = 0;
        switch(dir){
            case 'n': lineDir = -1; break;
            case 's': lineDir = 1; break;
            case 'w': colDir = -1; break;
            case 'e': colDir = 1; break;
        }
        if(map.tab[map.personnage.getLine()+lineDir][map.personnage.getCol()+colDir].type == 'm'){ //on recupere le position du perso et on ajoute celle désirée. Si c'est un mur, on retourne que c'est un mur
            String[] rep = {"m"};
            return rep;
        }
        for(int i =0; i<map.boites.size();i++){ // On vérifie la liste des boites pour voir si il y en a une dans la direction voulue
            if(map.boites.get(i).getLine() == map.personnage.getLine()+lineDir && map.boites.get(i).getCol() == map.personnage.getCol()+colDir){
                String[] rep = {"b", String.valueOf(i)};
                return rep;
            }
        }
        String[] rep = {"s"};
        return rep;
    }

   
    /**
     * Permet de determiner l'objet dans la direction souhaitée (Version pour le robot de génération).
     * @param map La carte du jeu.
     * @param dir La direction souhaitée.
     * @return On retourne un string de la direction.
     */
    public String[] nextDirG(GeneratedMap map, char dir){ //Retourne un caractère qui décrit l'élément dans un direction
        int colDir = 0;
        int lineDir = 0;
        switch(dir){
            case 'n': lineDir = -1; break;
            case 's': lineDir = 1; break;
            case 'w': colDir = -1; break;
            case 'e': colDir = 1; break;
        }
        if(map.tab[map.personnage.getLine()+lineDir][map.personnage.getCol()+colDir].type == 'm'){ //on recupere le position du perso et on ajoute celle désirée. Si c'est un mur, on retourne que c'est un mur
            String[] rep = {"m"};
            return rep;
        }
        for(int i =0; i<map.boites.size();i++){ // On vérifie la liste des boites pour voir si il y en a une dans la direction voulue
            if(map.boites.get(i).getLine() == map.personnage.getLine()+lineDir && map.boites.get(i).getCol() == map.personnage.getCol()+colDir){
                String[] rep = {"b", String.valueOf(i)};
                return rep;
            }
        }
        String[] rep = {"s"};
        return rep;
    }
}