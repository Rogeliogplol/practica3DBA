/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import ia.Casilla;

/**
 *
 * @author SRJota
 */
public class AlgoritmoMosca {
    
    private final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};
    //float minimacota;
    Boolean izq;
    //Algorithm algoritmo;
    int pasosmismo;
    
    
    public String movermaspromotedor(Casilla[][] casillas){
        float cota = Float.MAX_VALUE;
        int minpisados = Integer.MAX_VALUE;
        int mini=-1, minj=-1;
        int peoi=-1, peoj=-1;
        izq=false;
        for(int i=1; i<4; i++){
            for(int j=1; j<4; j++){
                if(casillas[i][j].getRadar()==2&&casillas[i][j].getScanner()==0.0){
                    mini=i-1; minj=j-1; i=4; j=4;
                }
                else if(cota>casillas[i][j].getScanner() && casillas[i][j].getPasos()<1 && casillas[i][j].getRadar()!=1){
                    mini=i-1; minj=j-1; cota=casillas[i][j].getScanner();
                }
                if(minpisados>casillas[i][j].getPasos() && casillas[i][j].getRadar()!=1 && i!=2 && j!=2){
                    minpisados=casillas[i][j].getPasos();
                    peoi=i-1; peoj=j-1;
                }
            }
        }
        if(peoi!=-1 && (mini==1||mini==-1) && (minj==1||minj==-1) && casillas[2][2].getRadar()!=2){
            return directions[peoi][peoj];
        }
        else if (mini==-1)
            return "ERROR";
        else{
            return directions[mini][minj];
        }
    }
    
    public String process(Casilla[][] sensor) throws Exception {
        return movermaspromotedor(sensor);
    }
    
    
}
