package utils;
import java.util.ArrayList;
import java.util.Random;

public class GeneratedMap {

    public static Case tab[][];
    public static int[] positionnement;
    public ArrayList<Box> boites = new ArrayList<Box>();
    public SokoCharacter personnage;
    public int height; public int length;


    /**
     * Fonction permettant de générer le cadre vide de la map.
     * @param random_number1 L'entier entré par l'utilisateur pour la hauteur.
     * @param random_number2 L'entier entré par l'utilisateur pour la largeur.
     * @param box_number L'entier entré par l'utilisateur pour le nombre de boites.
     */
    public GeneratedMap(int random_number1, int random_number2, int box_number){
        this.height = random_number1;
        this.length = random_number2;
        tab = new Case[random_number1][random_number2];
        for (int i = 0; i<random_number1; i++){
            for (int a = 0; a<random_number2; a++){
                
                if (i == 0 || i == random_number1-1) {
                    tab[i][a] = new Case('m');
                } else if (a == 0 || a == random_number2 -1) {
                    tab[i][a] = new Case('m');
                } else {
                    tab[i][a] = new Case('s');
                }
            }
        }
        placementPerso(random_number1, random_number2);
        placementBoites(random_number1, random_number2, box_number);

        int a = 2;
        int b = 3;
        int i = 0;
        positionnement = new int[boites.size()*2 +2 ];
        positionnement[0] = personnage.getLine();
        positionnement[1] = personnage.getCol();
        while(i!=boites.size()){
            positionnement[a] = boites.get(i).getLine();
            positionnement[b] = boites.get(i).getCol();
            a+=2;
            b+=2;
            i++;
        }
       

    }     



    /**
     * Permet de placer le personnage aléatoirement sur la carte.
     * @param random_number1 L'entier entré par l'utilisateur pour la hauteur.
     * @param random_number2 L'entier entré par l'utilisateur pour la largeur.
     */
    public void placementPerso(int random_number1, int random_number2){
        int char_counter = 0;

        // ajout aléatoire du perso
        while(char_counter != 1){
            for (int u = 2; u<random_number1-2; u++){
                for(int v = 2; v<random_number2-2; v++){
                    
                    Random random = new Random();
                    int char_selector = random.nextInt(2); // on choisit un nombre aleatoire

                    if (char_selector == 1 && char_counter == 0){ // s'il y a 0 personnage et que le char_selector est egal a 1 on ajoute le personnage aux coordonnees de la boucle
                        tab[u][v] = new Case('c');
                        personnage = new SokoCharacter(u, v);
                        char_counter++;
                    }
                }
            }
        }
        
    }

    /**
     * Fonction permettant de placer les boites aléatoirement sur la carte.
     * @param random_number1 La hauteur.
     * @param random_number2 La largeur.
     * @param box_number Le nombre de boites.
     */
    public void placementBoites(int random_number1, int random_number2, int box_number){

        int box_counter = 0;
        // ajout aléatoire de box
        while(box_counter != box_number){
            for (int x = 3; x<random_number1-3; x++){
                for (int z = 3; z<random_number2-3; z++){
                    Random random = new Random();
                    int box_selector = random.nextInt(2);
                    
                    if (box_selector == 1 && (personnage.getCol() != z || personnage.getLine() != x) && box_counter != box_number){
                        boites.add(new Box(x, z));
                        box_counter++;
                    } 
                }
                
            }
        }

    }
}
