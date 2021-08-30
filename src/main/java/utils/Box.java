package utils;
import maingame.*;
import windows.*;


public class Box extends Mobile{


    /**
     * Represente les boites
     * @param line La ligne (int)
     * @param col la colonne (int)
     */
    public Box(int line, int col){
        super(line,col);
    }


    /**
     * Permet de vérifier si on est sur un objectif.
     * @param map La carte(Map).
     * @return un booléen.
     */
    public boolean checkPoint(Map map){
        return map.tab[this.getLine()][this.getCol()].type == 'p';
    }

    /**
     * Permet de vérifier si on est sur un objectif (pour la génération aléatoire).
     * @param map La carte(Map).
     * @return un booléen.
     */
    public boolean checkPointG(GeneratedMap map){ // pour generation
        return map.tab[this.getLine()][this.getCol()].type == 'p';
    }


    /**
     * Permet de déterminer si une boite peut etre pousséé ou non.
     * @param map La carte (Map).
     * @param dir La direction (char).
     * @return un booléen.
     */
    public boolean canBePushed(Map map, char dir){ 
        boolean mur = false;
        switch(dir){
            case 'n': mur = (map.tab[this.getLine()-1][this.getCol()].type != 'm' && checkBox(map,this.getLine()-1,this.getCol())); break; 
            case 's': mur = (map.tab[this.getLine()+1][this.getCol()].type != 'm' && checkBox(map,this.getLine()+1,this.getCol())); break;
            case 'w': mur = (map.tab[this.getLine()][this.getCol()-1].type != 'm' && checkBox(map,this.getLine(),this.getCol()-1)); break;
            case 'e': mur = (map.tab[this.getLine()][this.getCol()+1].type != 'm' && checkBox(map,this.getLine(),this.getCol()+1)); break;
        }
        return mur;
    }

    /**
     * Permet de déterminer si une boite peut etre poussée ou non.
     * @param map la carte (Map)
     * @param dir la direction (char)
     * @return un booléen.
     */
    public boolean canBePushedG(GeneratedMap map, char dir){ // pour generation
        boolean mur = false;
        switch(dir){
            case 'n': mur = (map.tab[this.getLine()-1][this.getCol()].type != 'm' && checkBoxG(map,this.getLine()-1,this.getCol())); break; 
            case 's': mur = (map.tab[this.getLine()+1][this.getCol()].type != 'm' && checkBoxG(map,this.getLine()+1,this.getCol())); break;
            case 'w': mur = (map.tab[this.getLine()][this.getCol()-1].type != 'm' && checkBoxG(map,this.getLine(),this.getCol()-1)); break;
            case 'e': mur = (map.tab[this.getLine()][this.getCol()+1].type != 'm' && checkBoxG(map,this.getLine(),this.getCol()+1)); break;
        }
        return mur;
    }

    /**
     * Permet de vérifier s'il y a une boite.
     * @param map La carte(Map).
     * @param line La ligne (int)
     * @param col La colonne (int)
     * @return un booleen.
     */
    public boolean checkBox(Map map,int line, int col){
        for(int i = 0; i<map.boites.size();i++){
            if(map.boites.get(i).getLine() == line && map.boites.get(i).getCol() == col){
                return false;
            }
        }
        return true;
    }


    /**
     * Permet de vérifier s'il y a une boite pour le génération de niveau.
     * @param map La carte(Map).
     * @param line La ligne (int)
     * @param col La colonne (int)
     * @return un booleen.
     */
    public boolean checkBoxG(GeneratedMap map,int line, int col){
        for(int i = 0; i<map.boites.size();i++){
            if(map.boites.get(i).getLine() == line && map.boites.get(i).getCol() == col){
                return false;
            }
        }
        return true;
    }
}