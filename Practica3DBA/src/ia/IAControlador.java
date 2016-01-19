package ia;

/**
 * Clase que implementa la IA del agente controlador.
 * 
 * 
 * @author Fco Sueza Rodríguez
 */

public class IAControlador {
    
    Posicion goal;
    boolean goalencontrado;
    boolean asignadostodos;
    int esquinas;
    boolean[] esquinasasignadas;
    boolean primerGoal, esquinaIzGoal1, esquinaIzGoal2, esquinaDerGoal1, esquinaDerGoal2;
    int maxIndex=-1, minIndex=-1;
    Posicion currentGoal[];
    
    public IAControlador(){
        goal = new Posicion(-1,-1);
        goalencontrado=false;
        asignadostodos=false;
        esquinas=0;
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
        
       // Posicion posGoal = new Posicion(50,50);
        Posicion posGoal = new Posicion(original.getX(), original.getY());
        
        if (original.getY() > 0)
            posGoal.setY(0);
        else
            posGoal.setY(99);
        //System.out.println("Desde --> "+original.getX() + ";" + original.getY() 
                           //+"\nHacia-->"+posGoal.getX() + ";" + posGoal.getY());
        
        return posGoal;
    }
    
    
    public  Posicion[] calculateGoalPos(Posicion[] original) {
        Posicion ret [] = new Posicion[original.length];
        
        
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
            
        for (int i=0; i<4; i++) {
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
                    if (original[i].isEqual(new Posicion(0, 99-original[i].getY()))) {
                        esquinaIzGoal2=true;
                        ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
                    } else {
                        ret[i]=currentGoal[i];
                    }
                } else
                    ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
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
                    if (original[i].isEqual(new Posicion(99, 99-original[i].getY()))) {
                        esquinaDerGoal2=true;
                        ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
                    } else {
                        ret[i]=currentGoal[i];
                    }
                } else
                    ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
            } else
                ret[i]=new Posicion((int) (Math.random()*100),(int) (Math.random()*100));
        }
        
        return ret;
    }
    
    public boolean isAsignadoATodos(){
        return asignadostodos;
    }
    public Posicion[] calcularSitioParaGoal(Posicion posiciones[], CasillaControlador [][] mapa){
        
        Posicion casillasGoal[][] = new Posicion[11][11];
        Posicion respuesta [] = new Posicion[4];
        //inicializamos 
        for(int i=0; i<casillasGoal.length;i++){
            for(int j=0;j<casillasGoal.length;j++){
                casillasGoal[i][j] = new Posicion();
            }
        }
        for(int i=0;i<respuesta.length;i++)
            respuesta[i] = new Posicion();
        int posResp = 0;
        for(int i=0; i<casillasGoal.length;i++){
            for(int j=0;j<casillasGoal.length;j++){
                int valor; //= mapa[goal.getX() -5 + i][goal.getY() -5 + j].getRadar();
                if(goal.getX()-5+i>=100 || goal.getY()-5+j>=100 || goal.getX()-5+i<0 || goal.getY()-5+j<0)
                    valor=2;
                else
                    valor = mapa[goal.getX() -5 + i][goal.getY() -5 + j].getRadar();
                if(valor==3){
                    casillasGoal[i][j].Set(goal.getX() - 5 + i, goal.getY() - 5 + j);
                    if(posResp<4){
                        respuesta[posResp].Set(casillasGoal[i][j]);
                        posResp++;
                    }
                }
            }
        }
            
        
        /*
        Posicion[] pos = new Posicion[posiciones.length];
        Posicion[][] posicionesgoals = new Posicion[5][5];
        boolean [][] ocupado = new boolean [5][5];
        
        Posicion base = new Posicion (goal.getX()-2, goal.getY()-2);
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                posicionesgoals[i][j] = new Posicion(base.getX()+i,base.getY()+j);
                ocupado[i][j] = false;
            }
        }
        float mejorscanner;
        for(int cont=0; cont<pos.length; cont++){
            mejorscanner = Float.MAX_VALUE;
            for(int i=0; i<5; i++){
                for (int j=0; j<5; j++){
                    if (mapa[posicionesgoals[i][j].getX()][posicionesgoals[i][j].getY()].getRadar()==2){
                        
                    }
                }
            }
        }
        return pos;
                */
        asignadostodos = true;
        for(int i=0;i<respuesta.length;i++)
            if(respuesta[i].getX()==-1)
                asignadostodos = false;
        
        return respuesta;
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
                        if(Distancia(posiciones[i],goals[i])<Distancia(posiciones[j],goals[j])){
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
                    goalencontrado =true;
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