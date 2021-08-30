package utils;

import javafx.beans.binding.Bindings;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import maingame.*;
import windows.*;

import java.io.File;
import java.net.MalformedURLException;

public class SokoLevelItem extends Pane{
    private Text text;
    private Effect shadow = new DropShadow(5, Color.GREY); // on créé un effet d'ombre
    private Effect blur = new BoxBlur(1, 1, 1); // on créé un effet de flou

    /**
     * Permet de créer l'item pour le menu.
     * @param name Le nom de l'option du menu (String).
     */
    public SokoLevelItem(String name) throws MalformedURLException {
        Rectangle background = new Rectangle(240, 150);

        // IMAGE DE FOND = PREVISUALISATION DU LVL

        String path = System.getProperty("user.dir") + "/src/main/resources/levelimages/";
        File file = new File((path + name.substring(0, 8) + ".PNG").replace(' ', '_'));

        String localUrl = file.toURI().toURL().toString();
        
        ImageView imageView = new ImageView(new Image(localUrl));
        imageView.setFitHeight(150);
        imageView.setFitWidth(240);

        background.setEffect(new GaussianBlur());

        background.fillProperty().bind(
            Bindings.when(pressedProperty()).then(Color.color(0, 0, 0, 0.75)).otherwise(Color.color(0, 0, 0, 0.25)) // Couleur quand on clique et quand on ne clique pas
        );


        text = new Text(name);
        text.setTranslateX(80);
        text.setTranslateY(80);
        text.setFont(Font.loadFont(MainFX.class.getResourceAsStream("/Penumbra-HalfSerif.ttf"), 18)); // police d'écriture
        text.setFill(Color.WHITE);
        text.effectProperty().bind(
            Bindings.when(hoverProperty()) //effet lorsque l'on passe la souris dessus 
            .then(shadow)
            .otherwise(blur)
        );

        getChildren().addAll(background, imageView, text);

    }


    /**
     * Permet de lancer l'action quand on reçoit un click de souris.
     * @param action L'action à effectuer.
     */
    public void setOnAction(Runnable action){
        setOnMouseClicked(e -> action.run());
    }
}
