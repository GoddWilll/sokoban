package maingame;
import utils.*;
import windows.*;

public class Sokoban {
    private static String pathLevel = System.getProperty("user.dir") + "/src/main/resources/inputlevels/";
    private static String pathMoves = System.getProperty("user.dir") + "/src/main/resources/moves/";
    private static String pathOutput = System.getProperty("user.dir") + "/src/main/resources/outputlevels/";

    /**
     * Permet , via le terminal, d'appliquer un fichier .mov sur un fichier .xsb et de créer un fichier .xsb qui sera le résultat des 2
     */
    public static void main(String[] args){
        Map.inputMov(pathLevel + args[0],pathMoves + args[1],pathOutput + args[2]);
    }
}
