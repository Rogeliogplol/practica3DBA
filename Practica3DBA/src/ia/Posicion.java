package ia;

/**
 * Clase quen implementa el tipo Posicion, él cual contiene int X y int Y las cuales
 * guardan las coordenadas x,y respectivamente de por donde va el agente.
 *
 * @author Roger
 */

public class Posicion {
    private int X;
    private int Y;
    
    //#######################################################
    //----------------- CONSTRUCTORES -----------------------
    //#######################################################
    /**
    * Constructor por defecto
    * 
    * @author Roger
    */
    
    public Posicion (){
        X = -1;
        Y = -1;
    }
	
    /**
    * Constructor que guarda las posiciones del Agente.
    * @param x la coordenada x del agente.
    * @param y la coordenada y del agente.
    * 
    * @author Roger
    */
    
    public Posicion (int x, int y){
        X = x;
        Y = y;
    }
    
    //#######################################################
    //--------------------- SET -----------------------------
    //#######################################################
    
    /**
    * @param x la información de la coordenada x a guardar.
    * @param y la información de la coordenada y a guardar.
    * 
    * @author Roger
    */
    
    public void set (int x, int y){
        X=x;
        Y=y;
    }
    
    /**
    * @param pos la posición a la que se igualará ésta
    * 
    * @author Roger
    */
    
    public void set (Posicion pos){
        X=pos.getX();
        Y=pos.getY();
    }
    
    /**
    * @param x la información de la coordenada x a guardar.
    * 
    * @author Roger
    */
    
    public void setX (int x){
        X = x;
    }
    
    /**
    * @param y la información de la coordenada y a guardar.
    * 
    * @author Roger
    */
    
    public void setY (int y){
        Y = y;
    }
    
    //#######################################################
    //--------------------- GET -----------------------------
    //#######################################################
    /**
    * @return X devulve la coordenada x del agente.
    * 
    * @author Roger
    */
    
    public int getX (){
        return X;
    }
    /**
    * @return Y devulve la coordenada y del agente.
    * 
    * @author Roger
    */
    
    public int getY (){
        return Y;
    }
    
    //#######################################################
    //--------------------- GET -----------------------------
    //#######################################################
    /**
    * @param _pos Posicion
    * @return boolean. Devuelve el resultado de la comparación.
    * 
    * @author Rubén Orgaz Baena
    */
    
    public boolean isEqual (Posicion _pos){
        return getX() == _pos.getX() && getY() == _pos.getY();
    }
}
    
    
  
