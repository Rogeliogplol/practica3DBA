/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import ia.Conocimiento;

/**
 *
 * @author SRJota
 */
public class Inteligencia {
    
    private int pasos;
    private Conocimiento miConocimiento;
    private Algoritmo miAlgoritmo;
    private AlgorithSeguirPared algo2;
    private int tipo;
    boolean izquierda;
    
    public Inteligencia(Conocimiento _miConocimiento, int _tipo) {
        pasos = 100;
        miConocimiento = _miConocimiento;
        izquierda = true;
        miAlgoritmo = new Algoritmo();
        algo2 = new AlgorithSeguirPared();
    }
    

    public String nextSteep(int[][] radar, float[][] scanner) {
        
            try {
                if(tipo==0){
                    return miAlgoritmo.process(miConocimiento.Conocido(scanner, radar));
                }
                else{
                    return algo2.process(miConocimiento.Conocido(scanner, radar));
                }
            } catch (Exception ex){
                System.err.println("Error con algoritmo: " + ex.getMessage());
                return "Error";
            }
    }            
}
