package algorithm;

import java.util.ArrayList;

/** 
 * Clase que implementa el algoritmo HillClimbing en su variante de Ascenso Pronunciado.
 * 
 * @author Francisco Sueza Rodriguez
 */

public class StandardAlgorithm extends Algorithm {
    
    boolean obstacle;
    WallAlgorithm pared;
    
    public StandardAlgorithm (){
        super();
        pared = new WallAlgorithm();
        obstacle = false;
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
    
    @Override
    public String process(float [][] scanner, int [][] radar){
        if (obstacle)
            return pared.process();
        
        return "move";
    }
    
    /*
     * Getter/Setter
     */
    
    public boolean getObstacle(){
        return this.obstacle;
    }
    
    public void setObstacle(boolean obstacle){
        this.obstacle = obstacle;
    }
    
}
   
