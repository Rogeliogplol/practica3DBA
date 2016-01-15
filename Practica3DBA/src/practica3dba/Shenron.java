/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3dba;

import agent.Agente;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author SRJota
 */
public class Shenron {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String User = "Toro";
            String Pass = "Pitol";
            AgentsConnection.connect("isg2.ugr.es",6000, "test", User , Pass, false);
            System.out.println("Conectado");
            Agente agente = new Agente (new AgentID("grupo5"), User, Pass);
            agente.start();
        } catch (Exception ex) {
            System.out.println("");
            System.err.println("---->No conectado Exception = "+ex.getMessage());
        }
    }
    
}
