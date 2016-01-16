package ia;

/**
 * Clase que implementa la IA del agente controlador.
 * 
 * 
 * @author Fco Sueza Rodríguez
 */

public class IAControlador {
    
    Posicion goal;
    
    public IAControlador(){
        goal = new Posicion(-1,-1);
        
    };
    
    public  Posicion calculateGoalPos(Posicion original) {
        
        Posicion posGoal = new Posicion(50,50);
        //Posicion posGoal = new Posicion(original.getX(), original.getY());
        
        //if (original.getY() <= 99 && original.getY() >= 50)
        //    original.setY(0);
        //else
        //    original.setY(99);
        
        return posGoal;
    }
    
    public boolean vistoelobjetivo (CasillaControlador [][] mapa){
        for(int i=0; i<mapa.length; i++){
            for(int j=0; j<mapa.length; j++){
                if(mapa[i][j].getRadar()==3){
                    goal = new Posicion(j,i);
                    return true;
                }
            }
        }
        return false;
    }
    
    public Posicion getPosicionGoal(){
        return goal;
    }
    
}
