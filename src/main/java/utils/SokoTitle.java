package utils;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import maingame.*;
import windows.*;

public class SokoTitle extends Pane{
    private Text text;
    
    /**
     * Représente le titre.
     * @param name Le titre (String).
     */
    public SokoTitle(String name){
        String spread ="";
        for (char c : name.toCharArray()){
            spread += c + " "; // on espace les lettres et on les ajoute au fur et à mesure
        }
        
        text = new Text(spread);
        text.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 48)); //Police d'écriture et taille.
        text.setFill(Color.rgb(229, 193, 0)/*WHITE*/);
        text.setEffect(new DropShadow(30, Color.WHITE)); // effet appliqué au texte 
        getChildren().addAll(text);
    }

    /**
     * Permet d'obtenir la longueur du titre.
     * @return la longueur (double)
     */
    public double getTitleWidth(){
        return text.getLayoutBounds().getWidth();
    }

    /**
     * Permet d'obtenir la hauteur du titre.
     * @return la hauteur (double)
     */
    public double getTitleHeight(){
        return text.getLayoutBounds().getHeight();
    }
}
