package algorithm;

/**
 * Algoritmo para seguimiento de pared y rodear obstaculos.
 * 
 * @author SRJota
 * @author Fco Sueza Rodríguez
 */

public class WallAlgorithm extends Algorithm {
   
    private final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};
    float minimacota;

    public WallAlgorithm() {
        minimacota= Float.MAX_VALUE;
    }
    
    public String process(float [][] scanner, int [][] radar){
        return "move";
    };
  
    /*
     * Getter/Setter    
     */
}