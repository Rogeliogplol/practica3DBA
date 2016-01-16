/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

/**
 *
 * @author SRJota
 */
public class IAAgentes {
    Posicion objetivo;
    public IAAgentes(){
        objetivo=new Posicion(-1,-1);
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
        int tamanho = sensores.length;
        float minimo = Float.MAX_VALUE;
        int di = -1;
        int dj = -1;
        int medio = (sensores.length-1)/2;
        Posicion [][] gpsrelativo = new Posicion[3][3];
        Posicion postemporal = new Posicion(gps.getX()-1,gps.getY()-1);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                gpsrelativo[j][i] = new Posicion();
                gpsrelativo[j][i].setX(postemporal.getX()+i);
                gpsrelativo[j][i].setY(postemporal.getY()+j);
                System.out.print(gpsrelativo[j][i].getX()+";"+gpsrelativo[j][i].getY()+"\t");
            }
            System.out.println();
        }
        float[][] scanner = new float[3][3];
        System.out.println("--> Estoy en: "+ gps.getX() + "," + gps.getY());
        System.out.println("Voy a: "+ objetivo.getX() + "," + objetivo.getY());
        System.out.println("Los valores del sensor son:");
        for (int i=0; i<tamanho; i++){
            for (int j=0; j<tamanho; j++){
                System.out.print(sensores[i][j]+ "\t");
            }
            System.out.println();
            
        }
        System.out.println("Valor medio: "+medio +" Ventana");
        int [][] acotado = new int[3][3];
        int ai=0, aj=0;
        for(int i=medio-1; i<medio+2; i++){
            for (int j=medio-1; j<medio+2; j++){
                acotado[ai][aj] = sensores[i][j];
                System.out.println("["+i+";"+j+"]\t");
                aj++;
            }
            System.out.println();
            aj=0;
            ai++;
        }
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                scanner[i][j] = GetScanner(gpsrelativo[i][j].getX(), gpsrelativo[i][j].getY());
                System.out.print(scanner[i][j]+";"+sensores[i][j]+"\t");
                if(minimo>scanner[i][j]){
                    di=i;
                    dj=j;
                    minimo=scanner[i][j];
                }
            }
            System.out.println();
        }
        di = di;
        dj = dj;
        String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}}; 
        System.out.println("di="+di+"; dj="+dj+"; --->"+ directions[di][dj]);
        
        
        
        return directions[di][dj];
    }
}
