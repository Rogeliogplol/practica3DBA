
package algorithm;

import ia.Casilla;
import ia.Conocimiento;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Juan Manuel Navarrete Carrascosa
 *
 */
public class AlgorithSeguirPared {
    private final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};
    float minimacota;
    Boolean izq;
    //Algorithm algoritmo;
    int pasosmismo;
    
    public AlgorithSeguirPared() {
        minimacota= Float.MAX_VALUE;
        izq = false;
        //algoritmo = new Algorithm();
        pasosmismo=0;
    }
    
    
    /*@author Juan Manuel Navarrete Carrascosa
    */
    public String movermaspromotedor(Casilla[][] casillas){
        float cota = Float.MAX_VALUE;
        int minpisados = Integer.MAX_VALUE;
        int mini=-1, minj=-1;
        int peoi=-1, peoj=-1;
        izq=false;
        for(int i=1; i<4; i++){
            for(int j=1; j<4; j++){
                if(casillas[i][j].getRadar()==2){
                    mini=i-1; minj=j-1; i=4; j=4;
                }
                else if(cota>casillas[i][j].getScanner() && casillas[i][j].getPasos()<1 && casillas[i][j].getRadar()!=1){
                    mini=i-1; minj=j-1; cota=casillas[i][j].getScanner();
                }
                if(minpisados>casillas[i][j].getPasos() && casillas[i][j].getRadar()!=1 && i!=2 && j!=2){
                    minpisados=casillas[i][j].getPasos();
                    peoi=i-1; peoj=j-1;
                }
            }
        }
        if(peoi!=-1 && (mini==1||mini==-1) && (minj==1||minj==-1) && casillas[2][2].getRadar()!=2){
            return directions[peoi][peoj];
        }
        else if (mini==-1)
            return "ERROR";
        else{
            minimacota = cota;
            return directions[mini][minj];
        }
    }
    
    /*@author Juan Manuel Navarrete Carrascosa
     * Ayuda con Roger
    */
    public String moverpared(Casilla[][] casillas){
        System.out.println("Drch");
        if(casillas[2][2].getRadar()==2){
            return "GOAL";
        }
        for(int i=1; i < 4; i ++){
            for(int j=1;j <4; j++){
                if(casillas[i][j].getRadar()==2){
                    return directions[i-1][j-1];
                }
            }
        }
        
        if(casillas[1][3].getRadar()==1 && casillas[2][3].getRadar()!=1){
            System.out.println("1");
            return "E";
        }
        if(casillas[1][2].getRadar()==1 && casillas[1][3].getRadar()==1 &&  casillas[2][3].getRadar()!=1){
            System.out.println("2");
            return "E";
        }
        if(casillas[1][2].getRadar()==1 &&casillas[1][1].getRadar()==1 &&casillas[1][3].getRadar()==1 && casillas[2][3].getRadar()!=1){
            System.out.println("3");
            return "E";
        }
        if(casillas[1][2].getRadar()==1 && casillas[1][3].getRadar()!=1){
            System.out.println("4");
            return "NE";
        }
        if(casillas[1][1].getRadar()==1 && casillas[1][2].getRadar()!=1){
            System.out.println("5");
            return "N";
        }
        if(casillas[1][3].getRadar()==1 && casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()==1 && casillas[3][2].getRadar()!=1){
            System.out.println("6");
            return "S";
        }
        if(casillas[1][3].getRadar()==1 && casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()!=1){
            System.out.println("7");
            return "SE";
        }
        if(casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()==1 && casillas[3][2].getRadar()!=1){
            System.out.println("8");
            return "S";
        }
        if(casillas[3][3].getRadar()==1 && casillas[3][2].getRadar()!=1){
            System.out.println("9");
            return "S";
        }
        if(casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()!=1){
            System.out.println("10");
            return "SW";
        }
        if(casillas[3][3].getRadar()==1 && casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()==1 && casillas[2][1].getRadar()!=1){
            System.out.println("11");
            return "W";
        }
        if(casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()==1 && casillas[2][1].getRadar()!=1){
            System.out.println("12");
            return "W";
        }
        if(casillas[3][1].getRadar()==1 && casillas[2][1].getRadar()!=1){
            System.out.println("13");
            return "W";
        }
        if(casillas[2][1].getRadar()==1 && casillas[1][1].getRadar()!=1){
            System.out.println("14");
            return "NW";
        }
        if(casillas[3][1].getRadar()==1 && casillas[2][1].getRadar()==1 && casillas[1][1].getRadar()==1 && casillas[1][2].getRadar()!=1){
            System.out.println("15");
            return "N";
        }
        if(casillas[2][1].getRadar()==1 && casillas[1][2].getRadar()!=1){
            System.out.println("16");
            return "N";
        }
        if(casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()!=1){
            System.out.println("17");
            return "SE";
        }
        if(casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()!=1){
            System.out.println("18");
            return "SW";
        }
        if(casillas[2][1].getRadar()==1 && casillas[1][1].getRadar()!=1){
            System.out.println("19");
            return "NW";
        }
        if(casillas[1][2].getRadar()==1 && casillas[1][3].getRadar()!=1){
            System.out.println("20");
            return "NE";
        }
        if(casillas[3][2].getRadar()==1 &&casillas[3][3].getRadar()==1 &&casillas[2][3].getRadar()==1 &&casillas[3][1].getRadar()!=1){
           System.out.println(21);
           return "SW";
        }
        if(casillas[3][1].getRadar()==1 &&casillas[3][2].getRadar()==1 &&casillas[2][1].getRadar()==1 &&casillas[1][1].getRadar()!=1){
           System.out.println(22);
           return "NW";
        }
        if(casillas[2][1].getRadar()==1 &&casillas[1][1].getRadar()==1 &&casillas[1][2].getRadar()==1 &&casillas[1][3].getRadar()!=1){
           System.out.println(23);
           return "NE";
        }
        if(casillas[1][2].getRadar()==1 &&casillas[1][3].getRadar()==1 &&casillas[2][3].getRadar()==1 &&casillas[3][3].getRadar()!=1){
           System.out.println(24);
           return "SE";
        }
        else{
            return movermaspromotedor(casillas);
        }
    }
    /*@author Juan Manuel Navarrete Carrascosa
     * Ayuda con Roger
    */
    public String moverparedizq(Casilla[][] casillas){
        System.out.println("Izq");
        if(casillas[2][2].getRadar()==2){
            return "GOAL";
        }
        for(int i=1; i < 4; i ++){
            for(int j=1;j <4; j++){
                if(casillas[i][j].getRadar()==2){
                    return directions[i-1][j-1];
                }
            }
        }
        if(casillas[1][3].getRadar()==1 && casillas[1][2].getRadar()!=1){
            System.out.println("1");
            return "N";
        }
        if(casillas[1][2].getRadar()==1 && casillas[1][1].getRadar()!=1){
            System.out.println("2");
            return "NW";
        }
        if(casillas[1][2].getRadar()==1 &&casillas[1][1].getRadar()==1 &&casillas[1][3].getRadar()==1 && casillas[2][1].getRadar()!=1){
            System.out.println("3");
            return "W";
        }
        if(casillas[1][2].getRadar()==1 &&casillas[1][1].getRadar()==1 && casillas[2][1].getRadar()!=1){
            System.out.println("4");
            return "W";
        }
        if(casillas[1][1].getRadar()==1 && casillas[2][1].getRadar()!=1){
            System.out.println("5");
            return "W";
        }
        if(casillas[1][3].getRadar()==1 && casillas[2][3].getRadar()==1 && casillas[3][3].getRadar()==1 && casillas[1][2].getRadar()!=1){
            System.out.println("6");
            return "N";
        }
        if(casillas[1][3].getRadar()==1 && casillas[2][3].getRadar()==1 && casillas[1][2].getRadar()!=1){
            System.out.println("7");
            return "N";
        }
        if(casillas[2][3].getRadar()==1 && casillas[1][3].getRadar()!=1){
            System.out.println("8");
            return "NE";
        }
        if(casillas[3][3].getRadar()==1 && casillas[2][3].getRadar()!=1){
            System.out.println("9");
            return "E";
        }
        if(casillas[3][3].getRadar()==1 && casillas[3][2].getRadar()==1 && casillas[2][3].getRadar()!=1){
            System.out.println("10");
            return "E";
        }
        if(casillas[3][3].getRadar()==1 && casillas[3][2].getRadar()==1 && casillas[3][1].getRadar()==1 && casillas[2][3].getRadar()!=1){
            System.out.println("11");
            return "E";
        }
        if(casillas[3][2].getRadar()==1 && casillas[3][3].getRadar()!=1){
            System.out.println("12");
            return "SE";
        }
        if(casillas[3][1].getRadar()==1 && casillas[3][2].getRadar()!=1){
            System.out.println("13");
            return "S";
        }
        if(casillas[3][1].getRadar()==1 && casillas[2][1].getRadar()==1 && casillas[3][2].getRadar()!=1){
            System.out.println("14");
            return "S";
        }
        if(casillas[3][1].getRadar()==1 && casillas[2][1].getRadar()==1 && casillas[1][1].getRadar()==1 && casillas[3][2].getRadar()!=1){
            System.out.println("15");
            return "S";
        }
        if(casillas[2][1].getRadar()==1 && casillas[3][1].getRadar()!=1){
            System.out.println("16");
            return "SW";
        }
        if(casillas[2][3].getRadar()==1 && casillas[1][3].getRadar()!=1){
            System.out.println("17");
            return "NE";
        }
        if(casillas[3][2].getRadar()==1 && casillas[3][3].getRadar()!=1){
            System.out.println("18");
            return "SE";
        }
        if(casillas[2][1].getRadar()==1 && casillas[3][1].getRadar()!=1){
            System.out.println("19");
            return "SW";
        }
        if(casillas[1][2].getRadar()==1 && casillas[1][1].getRadar()!=1){
            System.out.println("20");
            return "NW";
        }
        if(casillas[3][2].getRadar()==1 &&casillas[3][3].getRadar()==1 &&casillas[2][3].getRadar()==1 &&casillas[1][3].getRadar()!=1){
           System.out.println(21);
           return "NE";
        }
        if(casillas[3][1].getRadar()==1 &&casillas[3][2].getRadar()==1 &&casillas[2][1].getRadar()==1 &&casillas[3][3].getRadar()!=1){
           System.out.println(22);
           return "SE";
        }
        if(casillas[2][1].getRadar()==1 &&casillas[1][1].getRadar()==1 &&casillas[1][2].getRadar()==1 &&casillas[3][1].getRadar()!=1){
           System.out.println(23);
           return "SW";
        }
        if(casillas[1][2].getRadar()==1 &&casillas[1][3].getRadar()==1 &&casillas[2][3].getRadar()==1 &&casillas[1][1].getRadar()!=1){
           System.out.println(24);
           return "NW";
        }
        else{
            return movermaspromotedor(casillas);
        }
    }
    /*@author Juan Manuel Navarrete Carrascosa
    */
    public String process(Casilla[][] sensor) throws Exception {
        float aux = minimacota;
        String masprometedor = movermaspromotedor(sensor);
        
        if (aux>minimacota){
            return masprometedor;
        }
        else{
            String movpar;
            if(sensor[2][2].getPasos()<=2)//1
                movpar= moverpared(sensor);
            else if(sensor[2][2].getPasos()>=3 && sensor[2][2].getPasos()<10 || izq==true){
                izq=true;
                if(sensor[2][2].getCambioSentido()){
                    pasosmismo++;
                    sensor[2][2].setCambioSentido();
                }else
                    pasosmismo=0;
                if(pasosmismo==10)
                {
			return "ERROR";
                    //return "IMPOSIBLE";
                }
                movpar= moverparedizq(sensor);
            }
            else if(sensor[2][2].getPasos()>=10){
                return movermaspromotedor(sensor);
            }
            else
                movpar="ERROR";
            System.out.println(movpar);
            return movpar;
        }
    }
    
}