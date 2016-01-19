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

/**
 *
 * @author SRJota
 */
public class Practica3DBA {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            String mapa;
            
            AgentsConnection.connect("isg2.ugr.es",6000, "Elnath", "Toro", "Pitol", false);
            System.out.println("Conectando");
            mapa = "map40";
            String AgenteControlador = "ControladorB";
            String [] nombreAgentes = {"PrimerBotA","SegundoBotA","TercerBotA","CuartoBotA"};
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
