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
public class ConocimientoControlador {
    private int ancho, alto;
    private CasillaControlador mapa[][];
    private Posicion posicion;
	
    public ConocimientoControlador(int ancho, int alto) {
	this.ancho=ancho;
	this.alto=alto;
        this.mapa=new CasillaControlador[this.ancho][this.alto];
	for (int i=0; i<this.ancho; i++){
            for (int ii=0; ii<this.alto; ii++){
                this.mapa[i][ii]=new CasillaControlador();
            }
        }
        this.posicion= new Posicion();
        posicion.setX(Integer.MAX_VALUE);
        posicion.setY(Integer.MAX_VALUE);
	
    }
    
    // Actualiza el mapa y la situación actual
    public void refreshData(Posicion _pos, int [][]radar, int id) {
        
        int medio = (radar.length-1)/2;
        
        Posicion pos = new Posicion(_pos.getX(),_pos.getY());
        
        
	for (int i=0; i<radar.length; i++) {
            for (int ii=0; ii<radar.length; ii++) {
                Posicion posicionMapa = new Posicion((pos.getX()+ii-medio), (pos.getY()+i-medio));
                if(posicionMapa.getX()>=0&&posicionMapa.getX()<ancho&&posicionMapa.getY()>=0&&posicionMapa.getY()<alto){
                    mapa[posicionMapa.getX()][posicionMapa.getY()].setCasilla(radar[i][ii],id,(i==medio&&ii==medio));
                }
            }
	}
    }
    
    public CasillaControlador[][] getMapa() {
        return mapa;
    }
    
}
