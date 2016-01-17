package algorithm;

/**
 *
 * @author SRJota
 */
public class AlgorithSeguirPared {
   
    private final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};
    float minimacota;

    public AlgorithSeguirPared() {
        minimacota= Float.MAX_VALUE;
    }
    
    
    
    public String movermaspromotedor(Casilla[][] casillas){
        float cota = Float.MAX_VALUE;
        int mini=-1, minj=-1;
        for(int i=1; i<4; i++){
            for(int j=1; j<4; j++){
                if(casillas[i][j].getRadar()==2){
                    mini=i-1; minj=j-1; i=4; j=4;
                }
                else if(cota>casillas[i][j].getScanner() && casillas[i][j].getPasos()<1 && casillas[i][j].getRadar()!=1){
                    mini=i-1; minj=j-1; cota=casillas[i][j].getScanner();
                }
            }
        }
        if (mini==-1)
            return "IMPOSIBLE";
        else{
            minimacota = cota;
            return directions[mini][minj];
        }
    }
    
    public String moverpared(Casilla[][] casillas){
        if(casillas[1][2].getRadar()==1 && casillas[1][3].getRadar()==1){
            if(casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()==1)
                return "S";
            if(casillas[2][3].getRadar()==1)
                return "SE";
            else
                return "E";
        }
        else if (casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()==1){
            if(casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()==1)
                return "W";
            if(casillas[3][2].getRadar()==1)
                return "SW";
            else
                return "S";
        }
        else if (casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()==1){
            if(casillas[2][1].getRadar()==1 && casillas[1][1].getRadar()==1){
                return "N";
            }
            if(casillas[2][1].getRadar()==1){
                return "NW";
            }
            else
                return "W";
        }
        else if (casillas[1][1].getRadar()==1 && casillas[2][1].getRadar()==1){
            if(casillas[1][2].getRadar()==1 && casillas[1][3].getRadar()==1)
                return "E";
            if(casillas[1][2].getRadar()==1)
                return "NE";
            else
                return "N";
        }
        else{
            return "Error";
        }
    }
    
    public String process(Casilla[][] sensor) throws Exception {
        float aux = minimacota;
        String masprometedor = movermaspromotedor(sensor);
        if ("GOAL".equals(masprometedor)){
            return masprometedor;
        }
        if (aux>minimacota){
            System.out.println("Alg no pared");
            return masprometedor;
        }
        else{
            String movpar= moverpared(sensor);
            System.out.println("Alg pared");
            if(sensor[2][2].getPasos()==2){
                return "IMPOSIBLE";
            }
            return movpar;
        }
    }
    
}