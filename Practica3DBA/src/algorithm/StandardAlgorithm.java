package algorithm;

import java.util.ArrayList;

/** 
 * Clase que implementa el algoritmo de desplazamiento estandar para los drones
 * halcon y pajaro.
 * 
 * @author Francisco Sueza Rodriguez
 */

public class StandardAlgorithm extends Algorithm {
    
    private boolean obstacle;
    private WallAlgorithm pared;
    
    public StandardAlgorithm (){
        super();
        pared = new WallAlgorithm();
        obstacle = false;
    }
    
    /**
     * 
     * Método para el procesamiento de la percepción y devolución de la 
     * dirección de movimiento.
     *  
     * @param scanner Matriz con la distancia al objetivo
     * @param radar Matriz de percepción con los obstaculos
     * 
     * @return Devuelve una cadena con la direccion selecionada   
     * @author Francisco Sueza Rodriguez
     */
    
    @Override
    public String process(float [][] scanner, int [][] radar){
        String move;
        
        if (radar[0][0] == 1 && radar[0][1] == 1 && radar[0][2] == 1)
            this.setObstacle(true);
        
        if (this.getObstacle())
           move =  pared.process(scanner, radar);
        else
           move = this.selectMove(scanner, radar);
        
        
            
       return move;
    }
    
    /**
     * Calcula la dirección a moverse en base a la percepción
     * 
     * @param scanner Matriz con la percepción de las distancia al objetivo 
     * @param radar Matriz con la percepción de obstaculos
     * @return 
     */
    
    private String selectMove(float [][] scanner, int [][] radar){
        String move;
        int indexi=0, indexj=0;
        float minimo = Float.MAX_VALUE;
        
        for (int i=0; i<scanner.length; i++)
            for (int j=0; j<scanner[0].length; j++)
                if (scanner[i][j] < minimo && radar[i][j] != 1 && radar[i][j] != 4) {
                    indexi = i;
                    indexj = j;
                    minimo = scanner[i][j];
                }
        
        if (scanner[indexi][indexj] < 2)
            move = "GOAL"; 
        else 
            move = directions[indexi][indexj];
        
        
        return move;
        
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
   
