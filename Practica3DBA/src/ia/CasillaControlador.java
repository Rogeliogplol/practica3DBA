package ia;

/**
 * Clase que implementa el tipo Casilla que contiene un float con el scanner, 
 * un int con radar y un int pasos en los cuales se guarda la información de scanner, radar y el número de
 * pasos en una casilla de la matriz.
 *
 * @author Roger
 * @author Daniel
 */
public class CasillaControlador {
    private int radar;
    private final int dronVisitado[];
    private int idUltimoDron;
    
    //#######################################################
    //----------------- CONSTRUCTOR -------------------------
    //#######################################################
	/**
	* Constructor por defecto
	* 
	* @author Roger
        * @author Daniel
	*/
    public CasillaControlador (){
        idUltimoDron=-1;
        radar = -1;
        dronVisitado = new int[4];
        for (int i=0; i<4; i++) {
            dronVisitado[i]=0;
        }
    }

        /**
	* @return array del número de pasos dados en la casilla por cada dron
	* 
	* @author Roger
        * @author Daniel
	*/
    public int[] getDronVisitado() {
        return dronVisitado;
    }

        /**
	* @param idDron el dron que ha visitado la casilla
	* 
	* @author Roger
        * @author Daniel
	*/
    public void setDronVisitado(int idDron) {
        dronVisitado[idDron]++;
        idUltimoDron=idDron;
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
	* @return radar devuelve la información guardada en radar.
	* 
	* @author Roger
        * @author Daniel
	*/
    public int getRadar (){
        return radar;
    }
    
        /**
	* @return radar devuelve la información guardada en radar.
	* 
	* @author Roger
        * @author Daniel
	*/
    
    public int getLastDrone () {
        return idUltimoDron;
    }

    //#######################################################
    //------------------ SET CASILLA ------------------------
    //#######################################################
	/**
	* @param ra información de radar a guardar
        * @param id id del drone
	*
	* @author Roger
        * @author Daniel
	*/
    public void setCasilla (int ra, int id, boolean casilla){
        this.setRadar(ra);
        if(casilla)
            this.setDronVisitado(id);
    }
}
