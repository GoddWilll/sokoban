package utils;
import maingame.*;
import windows.*;
public class Case{

    public char type;
    
    /**
     * Représente une case.
     * @param type le type de case(char)
     */
    public Case(char type){
        this.type = type;
    }

    /**
     * Représente une case vide.
     */
    public Case(){ //On gère le cas où l'on crée un tableau de Case vides
        this.type = ' ';
    }


    /**
     * Permet de récupérer le type de case.
     * @return le type de case (char).
     */
    public char getType(){
        return type;
    }

    /**
     * Permet de définir le type de la case.
     * @param type un char représentant la case.
     */
    public void setType(char type){
        this.type = type;
    }
}