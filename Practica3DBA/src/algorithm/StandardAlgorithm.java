    /*
     * TODO: 
     *
     * - Refactoring node selection to improve code (DRY)
     *
     * - Refactoring class using getter/setter instead direct access to
     *   class atributes. 
     *
     * - Improve memory usage deleting unnecessary vars. 
     */

package algorithm;

import java.util.ArrayList;

/** 
 * Clase que implementa el algoritmo HillClimbing en su variante de Ascenso Pronunciado.
 * 
 * @author Francisco Sueza Rodriguez
 */

public class StandardAlgorithm{
    
    private final int xStartNode = 2;      // Coordenada X de la casilla de inicio
    private final int yStartNode = 2;      // Coordenada Y de la casilla de inicio
    private final int MatrixDetected  = 5; // Tamaño de matriz de percepcion
    private final int MatrixNeighbors = 3; // tamaño de matriz de vecinos
    private final int MaxSteps = 500*500;    // Numero de pasos para considerar el mapa sin solucion
    
    private final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};   
    private int pasos;
    private boolean ObstacleMode;
    
    
   
    }
    
    /**
     * Procesa la percepcion del agente como un conjunto de nodos y devuelve
     * el nodo mas prometedor como resultado.
     *  
     * @param sensor Matriz de tipo Casilla que representa la percepcion
     * @return Devuelve una cadena con la direccion selecionada 
     * @throws Exception Excepcion en caso de que la matriz sea nula o este formada incorrectamente
     *     
     * @author Francisco Sueza Rodriguez
     */
    

   
