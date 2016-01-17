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
        /*Posicion posGoal = new Posicion(original.getX(), original.getY());
        
        if (original.getY() > 0)
            posGoal.setY(0);
        else
            posGoal.setY(99);
        System.out.println("Desde --> "+original.getX() + ";" + original.getY() 
                           +"\nHacia-->"+posGoal.getX() + ";" + posGoal.getY());
        */
        return posGoal;
    }
    
    public float Distancia (Posicion pos1, Posicion pos2){
        return (float) Math.sqrt(Math.pow(pos1.getX()-pos2.getX(),2)+Math.pow(pos1.getY()-pos2.getY(),2));
    }
    
    public boolean[] quienPara (Posicion[] posiciones, Posicion[] goals, float radio, String [][] especificacionesRoles){
        boolean [] stop = new boolean[posiciones.length];
        for(int cont=0; cont < stop.length; cont++)
            stop[cont] = false;
        int p = 0;
        for (int i=0; i< posiciones.length; i++){
            for (int j=i+1; j<posiciones.length; j++){
                if (Distancia(posiciones[i],posiciones[j])< radio){
                    String rol1=especificacionesRoles[i][1];
                    String rol2=especificacionesRoles[j][1];
                    if(rol1.equals(rol2)){
                        if(Distancia(posiciones[i],goals[i])<=Distancia(posiciones[j],goals[j])){
                            stop[i]=false;
                            stop[j]=true;
                        }
                        else{
                            stop[i]=true;
                            stop[j]=false;
                        }
                    }
                    else if(Float.valueOf(especificacionesRoles[i][2])<Float.valueOf(especificacionesRoles[j][2])){
                        stop[i]=true;
                        stop[j]=false;
                    }
                    else{
                        stop[i]=false;
                        stop[j]=true;
                    }
                }
            }
        }
        return stop;
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
