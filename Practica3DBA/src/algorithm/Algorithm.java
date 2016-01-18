package algorithm;

import ia.Posicion;

/**
 *  Clase abstracta para el manejo y definición de algoritmos. 
 * 
 * @author Fco Sueza Rodríguez
 */

public abstract class Algorithm {
    
    protected String lastMove;
    protected final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};
    
   
    /*
     * Constructor por defecto
     */
    
    public Algorithm (){};
    
    
    /**
     * Metodo de procesamiento de percepción y elección de movimiento.
     * 
     * @param scanner Matriz con la distancia en cada casilla al objetivo
     * @param radar Matriz con los obstaculos en cada casilla
     * 
     * @return String con la dirección escogida
     * @author Francisco Sueza Rodríguez
     */
    
    public abstract String process(float [][] scanner, int [][] radar);
    
   
    /*
     * Getters/Setters
     */
    
    public void setLastMove(String last){
        this.lastMove = last;
    }
    
    public String getLastMove(){
        return this.lastMove;
    }
}