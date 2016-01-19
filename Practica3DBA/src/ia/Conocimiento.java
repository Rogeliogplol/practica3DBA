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
    private Posicion pos;
	
    public Conocimiento(int ancho, int alto) {
	this.ancho=ancho;
	this.alto=alto;
        this.mapa=new Casilla[this.ancho][this.alto];
	for (int i=0; i<this.ancho; i++){
            for (int ii=0; ii<this.alto; ii++){
                this.mapa[i][ii]=new Casilla();
            }
        }
        pos= new Posicion(Integer.MAX_VALUE, Integer.MAX_VALUE);
        //posicion.setX(Integer.MAX_VALUE);
        //posicion.setY(Integer.MAX_VALUE);
	
    }
    
    // Actualiza el mapa y la situación actual
    public void refreshData(Posicion _pos, int [][]radar) {
        int medio = (radar.length-1)/2;
        
        pos.Set(_pos.getX(),_pos.getY());
        //Posicion pos = new Posicion();
        
        
	for (int i=0; i<radar.length; i++) {
            for (int ii=0; ii<radar.length; ii++) {
                Posicion posicionMapa = new Posicion((pos.getX()+ii-medio), (pos.getY()+i-medio));
                if(posicionMapa.getX()>=0&&posicionMapa.getX()<ancho&&posicionMapa.getY()>=0&&posicionMapa.getY()<alto){
                    mapa[posicionMapa.getX()][posicionMapa.getY()].setCasilla(radar[i][ii],(i==medio&&ii==medio));
                }
            }
	}
    }
    
    public Posicion getPosicion () {
        return pos;
    }
    
    public Casilla[][] getMapa() {
        return mapa;
    }
    
    public boolean HaSidoPisado(int i, int j){
        int x= pos.getX()-1+i;
        int y= pos.getY()-1+j;
        if(x<0 || y<0 || x >=100 || y>=100)
            return true;
        if(mapa[x][y].getPasos()>0){
            return true;
        }
        return false;
    }
    
    public int VecesPisado(int i, int j){
        int x= pos.getX()-1+i;
        int y= pos.getY()-1+j;
        if(x<0 || y<0 || x >=100 || y>=100)
            return Integer.MAX_VALUE;
        return mapa[x][y].getPasos();
    }
    
    public Casilla[][] Conocido(float [][] scanner, int [][] sensor){
        Casilla vista[][] = new Casilla[5][5];
        int medio = (5-1)/2;
        
        //for(int i=0; i<5; i++){
            //for(int j=0; j<5; j++){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                int x = pos.getX()-medio;
                int y = pos.getY()-medio;
                vista[i][j] = new Casilla();
                if(mapa[x][y]!=null){
                    vista[i][j].setPasos(mapa[x][y].getPasos());
                    vista[i][j].setRadar(mapa[x][y].getRadar());
                    if(vista[i][j].getRadar()==1 || vista[i][j].getRadar()==2 ||vista[i][j].getRadar()==4){
                        vista[i][j].setRadar(1);
                    }
                }
            }
        }
        for (int i=1; i<4; i++){
            for (int j=1; j<4; j++){
                vista[i][j].setRadar(sensor[i-1][j-1]);
                if(vista[i][j].getRadar()==1 || vista[i][j].getRadar()==2 ||vista[i][j].getRadar()==4){
                        vista[i][j].setRadar(1);
                }
                vista[i][j].setScanner(scanner[i-1][j-1]);
            }
        }
        
        return vista;
        /*
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                int x= pos.getX()+i-medio;
                int y= pos.getY()+j-medio;
                if(i==medio && j==medio){
                    System.out.println("Paraaaaaa");
                }
                if(x>=0&&y>=0&&x<100&&y<100){
                    vista[i][j]=mapa[x][y];
                    if(i>=1&&j>=1&&i<4&&j<4){
                            mapa[x][y].setRadar(sensor[i-1][j-1]);
                            mapa[x][y].setScanner(scanner[i-1][j-1]);
                    }
                    if(mapa[x][y].getRadar()==4||mapa[x][y].getRadar()==1||mapa[x][y].getRadar()==2){
                        mapa[x][y].setRadar(1);
                    }
                    
                }
                else {
                    vista[j][i]=new Casilla();
                    vista[j][i].setRadar(1);
                }
            }
        }*/
        //return vista;
    }
    
    public Casilla[][] ConocidoMosquitos(float [][] scanner, int [][] sensor){
        Casilla vista[][] = new Casilla[5][5];
        int medio = (5-1)/2;
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                int x= pos.getX()+i-medio;
                int y= pos.getY()+j-medio;
                if(x>=0&&y>=0&&x<100&&y<100){
                    vista[i][j]=mapa[x][y];
                    if(i>=1&&j>=1&&i<4&&j<4){
                            mapa[x][y].setRadar(sensor[j-1][j-1]);
                            mapa[x][y].setScanner(scanner[i-1][j-1]);
                    }
                    if(mapa[x][y].getRadar()==4||mapa[x][y].getRadar()==2){
                        mapa[x][y].setRadar(1);
                    }
                    else if (mapa[x][y].getRadar()==1){
                        mapa[x][y].setRadar(0);
                    }
                    
                }
                else {
                    vista[i][j]=new Casilla();
                    vista[i][j].setRadar(1);
                }
            }
        }
        return vista;
    }
    
    
}
