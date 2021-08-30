package utils;

import javafx.beans.binding.Bindings;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import maingame.*;
import windows.*;
public class SokoMenuItem extends Pane {
    private Text text;
    private Effect shadow = new DropShadow(5, Color.GREY); // on créé un effet d'ombre
    private Effect blur = new BoxBlur(1, 1, 1); // on créé un effet de flou

    /**
     * Permet de créer l'item pour le menu.
     * @param name Le nom de l'option du menu (String).
     */
    public SokoMenuItem(String name, int size){
        Rectangle background = new Rectangle(200, 30);
        background.setEffect(new GaussianBlur()); 

        background.fillProperty().bind(
            Bindings.when(pressedProperty()).then(Color.color(0, 0, 0, 0.75)).otherwise(Color.color(0, 0, 0, 0.25)) // Couleur quand on clique et quand on ne clique pas
        );

        text = new Text(name);
        text.setTranslateX(5);
        text.setTranslateY(20);
        //String path = System.getProperty("user.dir") + "/src/main/resources/";
        //text.setFont(Font.loadFont(path + "Penumbra-HalfSerif.ttf", size ));
        text.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), size));
        //text.setFont(Font.loadFont(MainFX.class.getResource("/Penumbra-HalfSerif.ttf").toExternalForm(), size)); // police d'écriture
        text.setFill(Color.WHITE);
        text.effectProperty().bind(
            Bindings.when(hoverProperty()) //effet lorsque l'on passe la souris dessus 
            .then(shadow)
            .otherwise(blur)
        );

        getChildren().addAll(background, text);

    }


    /**
     * Permet de lancer l'action quand on reçoit un click de souris.
     * @param action L'action à effectuer.
     */
    public void setOnAction(Runnable action){
        setOnMouseClicked(e -> action.run());
    }
}
