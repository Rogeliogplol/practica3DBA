/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import ia.Casilla;
import ia.Conocimiento;

/**
 *
 * @author SRJota
 */
public class Inteligencia {
    
    private int pasos;
    private Conocimiento miConocimiento;
    private AlgoritmoMosca miAlgoritmo;
    private AlgorithSeguirPared algo2;
    private int tipo;
    boolean izquierda;
    
    public Inteligencia(Conocimiento _miConocimiento, int _tipo) {
        tipo = _tipo;
        pasos = 100;
        miConocimiento = _miConocimiento;
        izquierda = true;
        miAlgoritmo = new AlgoritmoMosca();
        algo2 = new AlgorithSeguirPared();
    }
    

    public String nextSteep(int[][] radar, float[][] scanner) {
            Casilla[][] copia = new Casilla[5][5];
            int medio = (radar.length-1)/2;
            
            
            try {
                        if(tipo==0){
                            for(int i=0; i<5; i++){
                                for(int j=0; j<5; j++){
                                    copia[i][j]=new Casilla();
                                        if((i>=1&&i<4)&&(j>=1&&j<4)){
                                        int r = radar[i-1][j-1];
                                        if(r==-1||r==2||r==4)
                                        r=1;
                                    else if (r==3)
                                        r=2;
                                    else if (r==1)
                                        r=0;
                                    copia[i][j].setRadar(r);
                                    copia[i][j].setScanner(scanner[i-1][j-1]);
                                }
                            }
                        }
                            
                    return miAlgoritmo.process(copia);
                }
                else{
                            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    copia[i][j]=new Casilla();
                    if((i>=1&&i<4)&&(j>=1&&j<4)){
                        int r = radar[i-1][j-1];
                        if(r==-1||r==1||r==2||r==4)
                            r=1;
                        else if (r==3)
                            r=2;
                        copia[i][j].setRadar(r);
                        copia[i][j].setScanner(scanner[i-1][j-1]);
                    }
                }
            }
                    return algo2.process(copia);
                }
            } catch (Exception ex){
                System.err.println("Error con algoritmo: " + ex.getMessage());
                return "Error";
            }
    }            
}
