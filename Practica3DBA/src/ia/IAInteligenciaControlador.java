package ia;

import java.util.Hashtable;

/**
 * Clase que implementa la IA del agente controlador.
 * 
 * 
 * @author Fco Sueza Rodríguez
 */

public class IAInteligenciaControlador {
    
    Hashtable<, Object>
    
    
    public IAInteligenciaControlador(){};
    
    /**
     * 
     * @param original Posicion original 
     * @return 
     */
    
    public  Posicion calculateGoalPos(Posicion original) {
        
        Posicion posGoal = new Posicion(original.getX(), original.getY());
        
        if (original.getY() <= 99 && original.getY() >= 50)
            original.setY(0);
        else
            original.setY(99);
        
        return posGoal;
    }
    
    
    
}
