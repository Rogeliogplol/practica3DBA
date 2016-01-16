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

public class Conocimiento {
    private final int ancho, alto;
    private Casilla mapa[][];
    private Posicion posicion;
	
    public Conocimiento(int ancho, int alto) {
	this.ancho=ancho+6;
	this.alto=alto+6;
        this.mapa=new Casilla[this.ancho][this.alto];
	for (int i=0; i<this.ancho; i++){
            for (int ii=0; ii<this.alto; ii++){
                this.mapa[i][ii]=new Casilla();
            }
        }
        this.posicion= new Posicion();
        posicion.setX(Integer.MAX_VALUE);
        posicion.setY(Integer.MAX_VALUE);
	
    }
    
    // Actualiza el mapa y la situación actual
    public void refreshData(Posicion _pos, int [][]radar) {
        
    }
    
    public Casilla[][] getMapa() {
        return mapa;
    }
}
