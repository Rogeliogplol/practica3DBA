package practica2dba;

/**
 * Clase que implementa el tipo Casilla que contiene un float con el scanner, 
 * un int con radar y un int pasos en los cuales se guarda la información de scanner, radar y el número de
 * pasos en una casilla de la matriz.
 *
 * @author Roger
 * @author Daniel
 */
public class Casilla {
    private int radar;
    private int pasos;
    private boolean cambioSentido;
    
    //#######################################################
    //----------------- CONSTRUCTOR -------------------------
    //#######################################################
	/**
	* Constructor por defecto
	* 
	* @author Roger
        * @author Daniel
	*/
    public Casilla (){
        radar = -1;
        pasos = 0;
        cambioSentido = false;
    }

	/**
	* @param ra la información del radar a guardar.
	* 
	* @author Roger
        * @author Daniel
	*/
    public void setRadar (int ra){
        radar = ra;
    }
	/**
	* Le suma uno cuando haya pasado por esa casilla.
	* 
	* @author Roger
        * @author Daniel
	*/
    public void setPasos (){
        pasos ++;
    }
	/**
	* @param p la información de pasos a guardar.
	* 
	* @author Roger
        * @author Daniel
	*/
    public void setPasos (int p){
        pasos = p;
    }
    /** 
     * cambia cambioSentido a true
     * 
     * @author Roger
     * @author Daniel
     */
    public void setCambioSentido (){
        cambioSentido = true;
    }

	/**
	* @return radar devuelve la información guardada en radar.
	* 
	* @author Roger
        * @author Daniel
	*/
    public int getRadar (){
        return radar;
    }
	/**
	* @return pasos devuelve la información guardada en pasos.
	* 
	* @author Roger
        * @author Daniel
	*/
    public int getPasos (){
        return pasos;
    }
    /**
	* @return pasos devuelve la información guardada en cambioSentido.
	* 
	* @author Roger 
        * @author Daniel
	*/
    public boolean getCambioSentido(){
        return cambioSentido;
    }
    //#######################################################
    //------------------ SET CASILLA ------------------------
    //#######################################################
	/**
	* @param ra información de radar a guardar
	* @param casilla boolean que nos dice si es la casilla en la que nos situamos para sumarle 
	* el número de pasos.
	*
	* @author Roger
        * @author Daniel
	*/
    public void setCasilla (int ra, boolean casilla){
        this.setRadar(ra);
        if (casilla){
            this.setPasos();
        }
    }
    
}
