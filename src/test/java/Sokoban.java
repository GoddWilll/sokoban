

import org.junit.jupiter.api.*;
import utils.*;
import windows.*;

public class Sokoban {
    private static String pathLevel = System.getProperty("user.dir") + "/src/test/resources/inputlevels/";
    private static String pathMoves = System.getProperty("user.dir") + "/src/test/resources/moves/";
    private static String pathOutput = System.getProperty("user.dir") + "/src/test/resources/outputlevels/";


    public static void main(String[] args){
        Map.inputMov(pathLevel + "NIVEAU_0.xsb" ,pathMoves + "NIVEAU_0.mov",pathOutput + "NIVEAU_0TEST.xsb" );
    }

    @Test
    void movesApply1(){  // VERIFIE QUE LE PERSONNAGE NE PEUT PAS TRAVERSER LES MURS
        Map.inputMov(pathLevel + "NIVEAU_0.xsb" ,pathMoves + "moveset1.mov",pathOutput + "moveset1applied.xsb" );
    }

    @Test
    void movesApply2(){ // VERIFIE QUE LES BOITES NE SE PASSENT PAS AU TRAVERS QUE CE SOIT VERTICALEMENT OU HORIZONTALEMENT
        Map.inputMov(pathLevel + "NIVEAU_0.xsb" ,pathMoves + "moveset2.mov",pathOutput + "moveset2applied.xsb" );
    }

    @Test
    void movesApply3(){ // VERIFIE QUE LES BOITES ONT LE BON COMPORTEMENT SUR LES OBJECTIFS ET QUE LES BOITES NE PASSENT PAS A TRAVERS LES MURS
        Map.inputMov(pathLevel + "NIVEAU_0.xsb" ,pathMoves + "moveset3.mov",pathOutput + "moveset3applied.xsb" );
    }

}
