package algorithm;

import ia.Posicion;

/**
 *  Clase abstracta que maneja los dos algoritmos 
 * @author Fco Sueza Rodríguez
 */

public abstract class Algorithm {
    
    String lastMove;
    
    protected final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};
    
   
    public Algorithm (){};
    
    
    
    public abstract String process(float [][] scanner, int [][] radar);
    
   
    public void setLastMove(String last){
        this.lastMove = last;
    }
    
    public String getLastMove(){
        return this.lastMove;
    }
}
