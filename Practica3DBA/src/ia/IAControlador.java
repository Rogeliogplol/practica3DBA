package ia;

/**
 * Clase que implementa la IA del agente controlador.
 * 
 * 
 * @author Fco Sueza Rodríguez
 * @author Rogelio
 * @author Daniel
 */

public class IAControlador {
    private Posicion goal;
    private boolean asignadostodos;
    private final boolean[] esquinasasignadas;
    private boolean primerGoal, esquinaIzGoal1, esquinaIzGoal2, esquinaDerGoal1, esquinaDerGoal2;
    private int maxIndex=-1, minIndex=-1;
    private final Posicion currentGoal[];
    
    public IAControlador(){
        goal = new Posicion(-1,-1);
        asignadostodos=false;
        esquinasasignadas = new boolean [4]; 
        esquinasasignadas[0]= false;
        esquinasasignadas[1]= false;
        esquinasasignadas[2]= false;
        esquinasasignadas[3]= false;
        
        primerGoal=true;
        esquinaIzGoal1=false;
        esquinaIzGoal2=false;
        esquinaDerGoal1=false;
        esquinaIzGoal2=false;
        currentGoal=new Posicion [4];
        currentGoal[0]=new Posicion (-1, -1);
        currentGoal[1]=new Posicion (-1, -1);
        currentGoal[2]=new Posicion (-1, -1);
        currentGoal[3]=new Posicion (-1, -1);
    };
    
    public Posicion calculateGoalPos(Posicion original) {
        Posicion posGoal = new Posicion(original.getX(), original.getY());
        
        if (original.getY() > 0)
            posGoal.setY(0);
        else
            posGoal.setY(99);
        
        return posGoal;
    }
    
    /**
    * Método que determina la posición objetivo a la que se moverá cada dron
    * 
    * @param original Array con las posiciones de los drones al usar este método
    * 
    * @return Los objetivos calculados para cada dron
    * 
    * @author Daniel
    * @author SRJota
    * @author Roger
    */
    
    public Posicion[] calculateGoalPos(Posicion[] original) {
        Posicion ret [] = new Posicion[original.length];
        
        // Si es la primera vez que se van a mover los drones, guarda los drones
        // con el menor y el mayor x, que será los que se encarguen al principio
        // de bordear el mapa para conocer la parte más externa (se mueven hasta
        // tocar la pared, a continuación se mueven hacia la esquina con mismo 
        // "x2 y posición "y" contraria, y por último empiezan a recorrer el
        // mapa de la forma estándar, moviéndose a posiciones aleatorias)
        if (primerGoal) {
            primerGoal=false;
            int max=-1, min=10000;
            maxIndex=-1;
            minIndex=-1;
            
            for (int i=0; i<4; i++) {
                if (original[i].getX() < min) {
                    min=original[i].getX();
                    minIndex=i;
                }
                
                if (original[i].getX() > max) {
                    max=original[i].getX();
                    maxIndex=i;
                }
            }
        }
        
        // Calcula el objetivo para cada dron
            
        for (int i=0; i<4; i++) {
            // Si el dron es el que empezó con el menor x, hace que recorra la
            // pared izquierda antes de que se mueva de forma estándar
            if (i==minIndex) {
                if (!esquinaIzGoal1) {
                    if (original[i].isEqual(new Posicion(0, original[i].getY()))) {
                        esquinaIzGoal1=true;
                        currentGoal[i]=new Posicion(0, 99-original[i].getY());
                        ret[i]=currentGoal[i];
                    } else {
                        ret[i]=new Posicion(0, original[i].getY());
                    }
                } else if (!esquinaIzGoal2) {
                    if (original[i].isEqual(currentGoal[i])) {
                        esquinaIzGoal2=true;
                        ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
                    } else {
                        ret[i]=currentGoal[i];
                    }
                } else
                    ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
            // Si el dron es el que empezó con el mayor x, hace que recorra la
            // pared derecha antes de que se mueva de forma estándar
            } else if (i==maxIndex) {
                if (!esquinaDerGoal1) {
                    if (original[i].isEqual(new Posicion(99, original[i].getY()))) {
                        esquinaDerGoal1=true;
                        currentGoal[i]=new Posicion(99, 99-original[i].getY());
                        ret[i]=currentGoal[i];
                    } else {
                        ret[i]=new Posicion(99, original[i].getY());
                    }
                } else if (!esquinaDerGoal2) {
                    if (original[i].isEqual(currentGoal[i])) {
                        esquinaDerGoal2=true;
                        ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
                    } else {
                        ret[i]=currentGoal[i];
                    }
                } else
                    ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
            // Movimiento estándar: Devuelve posiciones aleatorias
            } else
                ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
        }
        
        return ret;
    }
    
    public boolean isAsignadoATodos(){
        return asignadostodos;
    }
    
    /**
    * Calcula la casilla para llegar al goal para cada agente
    * 
    * @param posiciones Array con las posiciones actuales de los drones
    * @param mapa Matriz con las casillas conocidas por el controlador
    * 
    * @return La casilla calculada
    * 
    * @author Rubén Orgaz Baena
    * @author José Luís
    */
    
    public Posicion[] calcularSitioParaGoal(Posicion posiciones[], CasillaControlador [][] mapa){
        Posicion casillasGoal[][] = new Posicion[11][11];
        Posicion respuesta [] = new Posicion[4];
        
        //inicializamos
        for (Posicion[] casillasGoal1 : casillasGoal) {
            for (int j = 0; j<casillasGoal.length; j++) {
                casillasGoal1[j] = new Posicion();
            }
        }
        
        for(int i=0;i<respuesta.length;i++)
            respuesta[i] = new Posicion();
        
        int posResp = 0;
        
        for(int i=0; i<casillasGoal.length;i++){
            for(int j=0;j<casillasGoal.length;j++){
                int valor;
                
                if(goal.getX()-5+i>=100 || goal.getY()-5+j>=100 || goal.getX()-5+i<0 || goal.getY()-5+j<0)
                    valor=2;
                else
                    valor = mapa[goal.getX() -5 + i][goal.getY() -5 + j].getRadar();
                if(valor==3){
                    casillasGoal[i][j].set(goal.getX() - 5 + i, goal.getY() - 5 + j);
                    if(posResp<4){
                        respuesta[posResp].set(casillasGoal[i][j]);
                        posResp++;
                    }
                }
            }
        }
            
        asignadostodos = true;
        for (Posicion respuesta1 : respuesta) {
            if (respuesta1.getX() == -1) {
                asignadostodos = false;
            }
        }
        
        return respuesta;
    }
    
    public float distancia (Posicion pos1, Posicion pos2){
        return (float) Math.sqrt(Math.pow(pos1.getX()-pos2.getX(),2)+Math.pow(pos1.getY()-pos2.getY(),2));
    }
    
    /**
    * Decide qué dron se detiene cuando hay un conflicto entre drones
    * 
    * @author José Luís
    */
    
    public boolean[] quienPara (Posicion[] posiciones, Posicion[] goals, float radio, String [][] especificacionesRoles){
        boolean [] stop = new boolean[posiciones.length];
        for(int cont=0; cont < stop.length; cont++)
            stop[cont] = false;
        for (int i=0; i< posiciones.length; i++){
            for (int j=i+1; j<posiciones.length; j++){
                if (distancia(posiciones[i],posiciones[j])< radio){
                    String rol1=especificacionesRoles[i][1];
                    String rol2=especificacionesRoles[j][1];
                    
                    if(rol1.equals(rol2)){
                        if(distancia(posiciones[i],goals[i])<distancia(posiciones[j],goals[j])){
                            stop[i]=false;
                            stop[j]=true;
                        } else{
                            stop[i]=true;
                            stop[j]=false;
                        }
                    } else if(Float.valueOf(especificacionesRoles[i][2])<Float.valueOf(especificacionesRoles[j][2])){
                        stop[i]=true;
                        stop[j]=false;
                    } else {
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
                    goal = new Posicion(i,j);
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