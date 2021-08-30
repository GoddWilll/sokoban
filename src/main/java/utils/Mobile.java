package utils;
import maingame.*;
import windows.*;
public class Mobile {
    protected int line;
    protected int col;

    /**
     * Implemente les Mobiles.
     * @param line La ligne
     * @param col La colonne
     */
    public Mobile(int line, int col){
        this.line = line;
        this.col = col;
    }

    /**
     * Permet de récupérer la ligne du mobile.
     * @return La ligne du mobile (int)
     */
    public int getLine(){
        return this.line;
    }

    /**
     * Permet de récupérer la colonne du mobile.
     * @return la colonne du mobile (int)
     */
    public int getCol(){
        return this.col;
    }

    /**
     * Permet de déplacer le Mobile.
     * @param dir un char représentant la direction à suivre.
     */
    public void move(char dir){
        switch(dir){ // On modifie les valeurs de line ou de col en fonction de la direction souhaitée
            case 'n': line = line - 1; break;
            case 's': line = line + 1; break;
            case 'w': col = col - 1; break;
            case 'e': col = col + 1; break;
        }
    }
}