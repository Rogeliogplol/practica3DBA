/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SRJota
 */
public class Agente extends SingleAgent{

    MessageQueue q;
    String User;
    String Pass;
    
    public Agente(AgentID aid, String _User, String _Pass) throws Exception {
        super(aid);
        User = _User;
        Pass = _Pass;
        q = new MessageQueue(100);
    }
    
    @Override
    public void onMessage(ACLMessage msg)  {
        try {
            q.Push(msg); // Cada mensaje nuevo que llega se encola en el orden de llegada
            System.out.println("\n["+this.getName()+"] Encolando: "+msg.getContent());
        } catch (InterruptedException ex) {
            System.err.println("Error al rescatar un mensaje");
            ex.printStackTrace();
        }
    }
    
    
    void login (){
        ACLMessage outbox = new ACLMessage();
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(this.getAid());
        outbox.setContent("{\"user\":\""+User+"\",\"password\":\""+Pass+"\"}");
        outbox.addReceiver(new AgentID("Shenron"));
        this.send(outbox);  
    }
    
    void reiniciar(){
        ACLMessage outbox = new ACLMessage();
        outbox.setPerformative(ACLMessage.QUERY_REF);
        outbox.setSender(this.getAid());
        outbox.setContent("{\"user\":\""+User+"\",\"password\":\""+Pass+"\"}");
        outbox.addReceiver(new AgentID("Shenron"));
        this.send(outbox);
    }



    @Override
    public void start() {
        System.out.println("");
        ACLMessage msg;
        login();
        while (q.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        try {
            msg = q.Pop();
        } catch (InterruptedException ex) {
            System.err.println("Error");
            return;
        }
        if (msg.getPerformativeInt()==ACLMessage.FAILURE){
            System.err.println("Fallo desde Shenron al logearse");
            return;
        }
        else if (msg.getPerformativeInt()==ACLMessage.NOT_UNDERSTOOD){
            System.err.println("Shenron no ha entendido al logearse");
            return;
        }
        else if (msg.getPerformativeInt()==ACLMessage.INFORM){
            if ("OK".equals(msg.getContent()))
                System.out.println("Shenron ha confirmado el log:"+ msg.getContent());
            else{
                System.err.println("Shenron no ha enviado lo que se esperaba...");
                return;
            }
        }
        else{
            System.err.println("Algo salio mal en el login");
            return;
        }
        
        reiniciar();
        while (q.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        try {
            msg = q.Pop();
        } catch (InterruptedException ex) {
            System.err.println("Error");
            
        }
        if (msg.getPerformativeInt()==ACLMessage.FAILURE){
            System.err.println("Fallo desde Shenron al reiniciar servidor");
            
        }
        else if (msg.getPerformativeInt()==ACLMessage.NOT_UNDERSTOOD){
            System.err.println("Shenron no ha entendido al reiniciar");
        }
        else if (msg.getPerformativeInt()==ACLMessage.INFORM){
            System.out.println("Ha llegado esta información:");
            System.out.println(msg.getContent());
        }
        else{
            System.out.println("Lo ultimo no se ha echo nada...");
        }
    }
    
}
