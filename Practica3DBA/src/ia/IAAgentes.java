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
        float[][] scanner = new float[tamanho][tamanho];
        System.out.println("--> Estoy en: "+ gps.getX() + "," + gps.getY());
        System.out.println("Voy a: "+ objetivo.getX() + "," + objetivo.getY());
        System.out.println("Los valores del scanner son:");
        for(int i=0; i<tamanho; i++){
            for(int j=0; j<tamanho; j++){
                scanner[i][j] = GetScanner(i, j);
                System.out.print(scanner[i][j]+" ");
                if(minimo>scanner[i][j]||(i<=medio+1&&i>=medio-1&&j<=medio+1&&j>=medio-1)){
                    di=i;
                    dj=j;
                    minimo=scanner[i][j];
                }
            }
            System.out.println();
        }
        
        di = di%3;
        dj = dj%3;
        String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}}; 
        
        return directions[di][dj];
    }
}
