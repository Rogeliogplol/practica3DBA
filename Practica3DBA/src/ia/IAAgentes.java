package ia;
import algorithm.*;
import static java.lang.Math.pow;

/**
 *
 * @author SRJota
 */

public class IAAgentes {
    private Posicion objetivo;
    private final Posicion[] demas;
    private final Inteligencia alg;
    
    public IAAgentes(int rol, Conocimiento _conocimiento){
        objetivo=new Posicion(-1,-1);
        demas = new Posicion[3];
        
        alg = new Inteligencia(_conocimiento, rol);

        for(int cont=0; cont < 3; cont++){
            demas[cont] = new Posicion();
        }
    }
    
    public void setDemas(Posicion [] listaposiciones, Posicion miposicion){
        int i=0;
        for (Posicion listaposicione : listaposiciones) {
            if ((miposicion.getX() != listaposicione.getX()) || (miposicion.getY() != listaposicione.getY())) {
                demas[i].setX(listaposicione.getX());
                demas[i].setY(listaposicione.getY());
                if(demas[i].getX()==-1 || demas[i].getY()==-1 ){
                    System.err.println("------------------------------------------");
                }
                i++;
            }
        }
    }
    
    public float distanciaEuclidea (Posicion p1, Posicion p2){
        return (float) Math.sqrt(pow(p1.getX()-p2.getX(),2)+pow(p1.getY()-p2.getY(),2));
    }
    
    public int[][] insertarMovimientosDemas (int [][] sensor, Posicion miposicion){
        Posicion[][] agentepropio = new Posicion[3][3];
        Posicion Posicionrelativa = new Posicion(miposicion.getX()-1, miposicion.getY()-1);
        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                agentepropio[i][j]= new Posicion();
                agentepropio[i][j].setX(Posicionrelativa.getX()+i);
                agentepropio[i][j].setY(Posicionrelativa.getY()+j);
            }
        }
                
        Posicion otroAgente = new Posicion();
        
        for(int cont=0; cont < 3; cont++){
            if(distanciaEuclidea(miposicion,demas[cont])<3){
                for(int i=0; i< 3; i++){
                    for (int j=0; j<3; j++){
                        otroAgente.setX((demas[cont].getX()-1)+i);
                        otroAgente.setY((demas[cont].getY()-1)+j);
                        for(int ii=0; ii<3; ii++){
                            for(int jj=0; jj<3; jj++){
                                if(otroAgente.getX()==agentepropio[ii][jj].getX()&&otroAgente.getY()==agentepropio[ii][jj].getY()){
                                    sensor[jj][ii]=4;
                                }
                            }
                        }
                    }
                }
            }
        }
        return sensor;
    }
    
    public void setObjetivo(Posicion objetivo){
        this.objetivo = objetivo;
    }
    
    public Posicion getObjetivo (){
        return objetivo;
    }
    
    public float getScanner(int X, int Y){
        return (float) Math.sqrt(Math.pow(X-objetivo.getX(),2)+Math.pow(Y-objetivo.getY(),2));
    }
    
    public String nextSteep (int [][] sensores, Posicion gps){
        int medio = (sensores.length-1)/2;
        if(gps.isEqual(objetivo)){
            return "GOAL";
        }
        
        Posicion [][] gpsRelativo = new Posicion[3][3];
        Posicion postemporal = new Posicion(gps.getX()-1,gps.getY()-1);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                gpsRelativo[j][i] = new Posicion();
                gpsRelativo[j][i].setX(postemporal.getX()+i);
                gpsRelativo[j][i].setY(postemporal.getY()+j);
            }
        }
        
        float[][] scanner = new float[3][3];
        int [][] acotado = new int[3][3];
        int ai=0, aj=0;
        
        for(int i=medio-1; i<medio+2; i++){
            for (int j=medio-1; j<medio+2; j++){
                acotado[ai][aj] = sensores[i][j];
                aj++;
            }
            
            aj=0;
            ai++;
        }
        
        acotado = insertarMovimientosDemas(acotado, gps);

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                scanner[i][j] = getScanner(gpsRelativo[i][j].getX(), gpsRelativo[i][j].getY());
            }
        }

        return alg.nextSteep(acotado, scanner);
    }
}