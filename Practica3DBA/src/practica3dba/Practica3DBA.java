/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3dba;

import agent.Agente;
import agent.AgenteControlador;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;
import java.util.Scanner;

/**
 *
 * @author SRJota
 */
public class Practica3DBA {
    
    
    static public String MenuSelecionMapa(){
        int select=-1;
        Scanner scanner = new Scanner(System.in);
        while(select != 0){
			//Try catch para evitar que el programa termine si hay un error
			try{
				System.out.println("Elige opción:\n"+ 
                                                "1.- map1\t8.- map10\n" +
						"2.- map2\t9.- map20\n" +
						"3.- map3\t10.- map30\n" +
						"4.- map4\t11.- map40\n" +
                                                "5.- map5\t12.- map50\n"+
                                                "6.- map6\t13.- map60\n"+
                                                "7.- map7\t14.- map70\n"+
						"");
				//Recoger una variable por consola
                                System.out.print("¿Que mapa deseas?");
				select = Integer.parseInt(scanner.nextLine()); 
	
				
				switch(select){
				case 1: 
					return "map1";
				case 2: 
					return "map2";
				case 3: 
					return "map3";
				case 4: 
					return "map4";
                                case 5: 
					return "map5";
				case 6: 
					return "map6";
				case 7: 
					return "map7";
                                case 8: 
					return "map10";
				case 9: 
					return "map20";
				case 10: 
					return "map30";
				case 11: 
					return "map40";
                                case 12: 
					return "map50";
				case 13: 
					return "map60";
				case 14: 
					return "map70";
				default:
					System.out.println("Número no reconocido");
				}
				
				System.out.println("\n");
				
			}catch(Exception e){
				System.out.println("Uoop! Error!");
                                return("");
			}
		}
                return "map10";
	}
	

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            String mapa;
            
            AgentsConnection.connect("isg2.ugr.es",6000, "Elnath", "Toro", "Pitol", false);
            System.out.println("Conectando");
            //mapa = "map50";
            mapa=MenuSelecionMapa();
            String AgenteControlador = "Yoda";
            String [] nombreAgentes = {"Chubaca","HanSolo","DarthVader","ObiWan"};
            AgenteControlador agente007 = new AgenteControlador (new AgentID(AgenteControlador), "Elnath", nombreAgentes, mapa );
            Agente agente1 = new Agente (new AgentID(nombreAgentes[0]), "Elnath", mapa, AgenteControlador);
            Agente agente2 = new Agente (new AgentID(nombreAgentes[1]), "Elnath", mapa, AgenteControlador);
            Agente agente3 = new Agente (new AgentID(nombreAgentes[2]), "Elnath", mapa, AgenteControlador);
            Agente agente4 = new Agente (new AgentID(nombreAgentes[3]), "Elnath", mapa, AgenteControlador);
            agente1.start();
            agente2.start();
            agente3.start();
            agente4.start();
            agente007.start();
            
            // TODO code application logic here
        }
        catch (Exception ex){
            System.err.println("Error al conectar:"+ex.toString());
        }
    }
    
}
