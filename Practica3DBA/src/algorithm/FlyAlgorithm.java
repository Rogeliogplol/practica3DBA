package algorithm;

/**
 * Algoritmo para el desplazamiento del drone tipo mosca.
 * 
 * @author Fco Sueza Rodríguez
 */

public class FlyAlgorithm extends Algorithm {
    
    public FlyAlgorithm(){
        super();
    }
    
    /**
     * Metodo para procesar la percepción y elegir el movimiento adecuado, sin
     * tener en cuenta los obstaculos.
     * 
     * @param scanner Matriz con la distancia al objetivo
     * @param radar Matriz con el radar indicando los obstaculos
     * 
     * @return String con la dirección de movimiento 
     * @author Francisco Sueza Rodríguez
     */
    
    @Override
    public String process(float [][] scanner, int [][] radar){
        int indexi=0, indexj=0;
        float minim=Float.MAX_VALUE;
        String move;
        
        for (int i=0; i<scanner.length;i++)
            for (int j=0; j<scanner[0].length;j++)
                if (scanner[i][j] < minim && radar[i][j] != 4) {
                    indexi=i;
                    indexj=j;
                    minim = scanner[i][j];
                }
        
        // Si el scanner es menor de 2 esque estamos al lado del objetivo
        
        if (scanner[indexi][indexj] < 2)
            move = "GOAL"; 
        else 
            move = directions[indexi][indexj];
        
        this.setLastMove(move);
        return move;
    }
}
