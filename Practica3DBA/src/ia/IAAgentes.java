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
        return (float) Math.sqrt((X*X)+(Y*Y));
    }
    
    public String NextSteep (int [][] sensores, Posicion gps){
        int tamanho = sensores.length;
        float minimo = Float.MAX_VALUE;
        int di = -1;
        int dj = -1;
        int medio = (sensores.length-1)/2;
        float[][] scanner = new float[tamanho][tamanho];
        for(int i=0; i<tamanho; i++){
            for(int j=0; j<tamanho; j++){
                scanner[j][i] = GetScanner(j, i);
                if(minimo>scanner[i][j]||(i<=medio+1&&i>=medio-1&&j<=medio+1&&j>=medio-1)){
                    di=i;
                    dj=j;
                    minimo=scanner[i][j];
                }
            }
        }
        String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}}; 
        
        return directions[di][dj];
    }
}
