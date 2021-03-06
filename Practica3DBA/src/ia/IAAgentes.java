/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;
import algorithm.*;
//import algorithm.Algorithm;
//import algorithm.FlyAlgorithm;
//import algorithm.StandardAlgorithm;
import static java.lang.Math.pow;

/**
 *
 * @author SRJota
 */
public class IAAgentes {
    Posicion objetivo;
    Posicion[] demas;
    Inteligencia alg;
    int Rol;
    Conocimiento conocimiento;
    
    public IAAgentes(int rol, Conocimiento _conocimiento){
        objetivo=new Posicion(-1,-1);
        demas = new Posicion[3];
        Rol = rol;
        
        alg = new Inteligencia(_conocimiento, rol);
        /*if(rol==0){
            alg = new FlyAlgorithm(_conocimiento);
        }else{
            alg = new StandardAlgorithm(_conocimiento);
        }*/
        for(int cont=0; cont < 3; cont++){
            demas[cont] = new Posicion();
        }
        conocimiento = _conocimiento;
    }
    public void SetDemas(Posicion [] listaposiciones, Posicion miposicion){
        int i=0;
        for(int cont=0; cont < listaposiciones.length; cont++){
            if((miposicion.getX()!=listaposiciones[cont].getX())||(miposicion.getY()!=listaposiciones[cont].getY())){
                demas[i].setX(listaposiciones[cont].getX());
                demas[i].setY(listaposiciones[cont].getY());
                if(demas[i].getX()==-1 || demas[i].getY()==-1 ){
                    System.err.println("------------------------------------------");
                }
                i++;
            }
            
        }
    }
    
    public float DistanciaEuclidea (Posicion p1, Posicion p2){
        return (float) Math.sqrt(pow(p1.getX()-p2.getX(),2)+pow(p1.getY()-p2.getY(),2));
    }
    
    public int[][] InsertarMovmientosDemas (int [][] sensor, Posicion miposicion){
        Posicion[][] agentepropio = new Posicion[3][3];
        Posicion Posicionrelativa = new Posicion(miposicion.getX()-1, miposicion.getY()-1);
        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                agentepropio[i][j]= new Posicion();
                agentepropio[i][j].setX(Posicionrelativa.getX()+i);
                agentepropio[i][j].setY(Posicionrelativa.getY()+j);
            }
        }
                
        Posicion otroagente = new Posicion();
        
        for(int cont=0; cont < 3; cont++){
            if(DistanciaEuclidea(miposicion,demas[cont])<3){
                for(int i=0; i< 3; i++){
                    for (int j=0; j<3; j++){
                        otroagente.setX((demas[cont].getX()-1)+i);
                        otroagente.setY((demas[cont].getY()-1)+j);
                        for(int ii=0; ii<3; ii++){
                            for(int jj=0; jj<3; jj++){
                                if(otroagente.getX()==agentepropio[ii][jj].getX()&&otroagente.getY()==agentepropio[ii][jj].getY()){
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
    
    public void SetObjetivo(Posicion _objetivo){
        objetivo = _objetivo;
    }
    
    public Posicion GetObjetivo (){
        return objetivo;
    }
    
    public float GetScanner(int X, int Y){
        return (float) Math.sqrt(Math.pow(X-objetivo.getX(),2)+Math.pow(Y-objetivo.getY(),2));
    }
    
    public String NextSteep (int [][] sensores, Posicion gps){
        //int tamanho = sensores.length;
        float minimo = Float.MAX_VALUE;
        int di = -1;
        int dj = -1;
        int medio = (sensores.length-1)/2;
        if(gps.isEqual(objetivo)){
            return "GOAL";
        }
        
        Posicion [][] gpsrelativo = new Posicion[3][3];
        Posicion postemporal = new Posicion(gps.getX()-1,gps.getY()-1);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                gpsrelativo[j][i] = new Posicion();
                gpsrelativo[j][i].setX(postemporal.getX()+i);
                gpsrelativo[j][i].setY(postemporal.getY()+j);
                
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
        acotado = InsertarMovmientosDemas(acotado, gps);
        //if(acotado[1][1]==3)
            //return "GOAL";
        /*for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                scanner[i][j] = GetScanner(gpsrelativo[i][j].getX(), gpsrelativo[i][j].getY());
                if(acotado[i][j]==3 && i==1 && j==1){//Estoy encima de un goal
                    return "GOAL";
                } else if (acotado[i][j]==3){ //Es solucion pero puede que alguien lo pise
                    di=i;
                    dj=j;
                    minimo = -1;
                }
                else if(minimo>scanner[i][j] && acotado[i][j]!=4 && acotado[i][j]!=1 && acotado[i][j]!=2 && !(i==1 && j==1)){ //Es el valor mas bajo y no puede que nadie lo pise
                    di=i;
                    dj=j;
                    minimo=scanner[i][j];
                }
            }
        }
        //if (di==-1 && dj==-1) //no he selecionado nada
            //return "GOAL";
        //String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}}; 
        //return directions[di][dj];*/
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                scanner[i][j] = GetScanner(gpsrelativo[i][j].getX(), gpsrelativo[i][j].getY());
            }
        }
        //return alg.process(scanner, acotado);
        return alg.nextSteep(acotado, scanner);
    }
}
