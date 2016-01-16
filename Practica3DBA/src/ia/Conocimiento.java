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
        int medio=0;
        if(radar.length==3){
            medio=1;
        }
        else if(radar.length==5){
            medio=2;
        }
        else if (radar.length==11){
            medio=5;
        }
        Posicion pos = new Posicion(_pos.getX()-medio,_pos.getY()-medio);
	for (int i=0; i<radar.length; i++) {
            for (int ii=0; ii<radar.length; ii++) {
                mapa[pos.getX()+i][pos.getY()+ii].setCasilla(radar[i][ii],false);
                if(i==medio&&ii==medio)
                    mapa[pos.getX()+i][pos.getY()+ii].setCasilla(radar[i][ii],true);
                
            }
	}
    }
    
    public Casilla[][] getMapa() {
        return mapa;
    }
}
