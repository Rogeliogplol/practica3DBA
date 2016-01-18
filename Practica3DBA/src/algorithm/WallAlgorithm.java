package algorithm;

/**
 * Algoritmo para seguimiento de pared y rodear obstaculos.
 * 
 * @author SRJota (codigo original)
 * @author Fco Sueza Rodríguez (modificacion)
 */

public class WallAlgorithm extends Algorithm {
   
    public WallAlgorithm() {
        super();
    }
    
    public String process(float [][] scanner, int [][] radar){
        if(radar[0][1]== 1 && radar[0][2] == 1){
            if(radar[1][2] == 1 && radar[2][2] ==1)
                return "S";
            if(radar[1][2] ==1)
                return "SE";
            else
                return "E";
        }
        else if (radar[1][2] == 1 && radar[2][2] ==1){
            if(radar[2][1] ==1 && radar[2][0] == 1)
                return "W";
            if(radar[2][1] == 1)
                return "SW";
            else
                return "S";
        }
        else if (radar[2][1] == 1 && radar[2][0] == 1){
            if(radar[1][0]== 1 && radar[0][0]== 1){
                return "N";
            }
            if(radar[1][0] == 1){
                return "NW";
            }
            else
                return "W";
        }
        else if (radar[0][0] == 1 && radar[1][0] == 1){
            if(radar[0][1] == 1 && radar[0][2] == 1)
                return "E";
            if(radar[0][1] == 1)
                return "NE";
            else
                return "N";
        }
        
        return "ERROR";
    };
  
    /*
     * Getter/Setter    
     */
}