/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import gui.DibujarMapa;
import ia.Conocimiento;
import ia.IAAgentes;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica3dba.MessageQueue;
import ia.Posicion;
import practica3dba.Traductor;
/**
 *
 * @author SRJota
 */
public class Agente extends SingleAgent {
    
    private String nameAgent;
    private String nameAgentSend;
    private String nameAgentControlador;
    private String map;
    private Traductor miTraductor;
    MessageQueue q1;
    int fuel;
    Conocimiento conocimiento;
    

    public Agente(AgentID aid, String _nameAgentSend, String _nameMap, String _nameAgentControlador) throws Exception {
        super(aid);
        nameAgent = aid.name;
        nameAgentSend = _nameAgentSend;
        nameAgentControlador = _nameAgentControlador;
        map = _nameMap;
        q1 = new MessageQueue(100);
        miTraductor = new Traductor();
        fuel = 100;
        conocimiento = new Conocimiento(100, 100);
    }
    
    public String getNameAgentSend(){
        return nameAgentSend;
    }
    
    public void setNameAgentSend(String _nameAgentSend){
        nameAgentSend = _nameAgentSend;
    }
    
    @Override
    public void onMessage(ACLMessage msg)  {
        try {
            q1.Push(msg); // Cada mensaje nuevo que llega se encola en el orden de llegada
            System.out.println("\n["+this.getName()+"] Encolando: "+msg.getContent());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendMessege(ACLMessage msg){
        this.send(msg);
    }
    
    private void waitMess(){
        while (q1.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void execute(){
        String msg="";
        Posicion pos = new Posicion(-1,-1);
        int tipo = -1;
        int[][] sensor;
        int bateria;
        int bateriaTotal;
        System.out.println();
        
        /*******************************************************************/
        /*                   Conseguir clave desde el contorlador          */
        /*******************************************************************/
        waitMess();
        System.out.println("--------------------------------------");
        try{
            msg=miTraductor.CAautoSelectACLMessage(q1.Pop());
            //System.out.println("Key dada desde el controlador: "+ msg);
        } catch (InterruptedException ex) {
            System.err.println("\n["+this.getName()+"] Error al conseguir key del controlador");
        }
        
        /*******************************************************************/
        /*                   Checkin al servidor                           */
        /*******************************************************************/
        sendMessege(miTraductor.PreguntarRol(getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("\n["+this.getName()+"]Error al sacar mensaje");
        }
        if (msg!="-1"){
            tipo = Integer.valueOf(msg);
        }
        else{
            System.err.println("\n["+this.getName()+"] Error al consguir el rol");
            sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
            return;
        }
        
        
        
        /*******************************************************************/
        /*                   Enviar rol al controlador                     */
        /*******************************************************************/
        sendMessege(miTraductor.ACSendRol(getAid(), nameAgentControlador, msg));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg = miTraductor.CAautoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("\n["+this.getName()+"]Error al sacar mensaje");
        }
        if(msg!="OK"){
            return;
        }
        /*******************************************************************/
        /*                   Refuel                                        */
        /*******************************************************************/
        sendMessege(miTraductor.Refuel(getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        /*******************************************************************/
        /*                   Pedir informacion al servidor                 */
        /*******************************************************************/
        
        sendMessege(miTraductor.PedirInformacion(getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg=miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        if (msg.contains("CRASHED")||msg.contains("NOT_UNDERTOOD")||msg.contains("BAD_KEY")){
           System.err.println("Error en el agente:"+nameAgent);
           sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
           return;
        }
        pos = miTraductor.getGPS(msg);
        sensor = miTraductor.getSensor(tipo, msg);
        bateria = miTraductor.GetBateria(msg);
        
        
        /*******************************************************************/
        /*                   Enviar informacion al controlador             */
        /*******************************************************************/
        
        sendMessege(miTraductor.ACDatos(getAid(), nameAgentControlador, msg));
        conocimiento.refreshData(pos, sensor);
        DibujarMapa dibujar = new DibujarMapa(this.getName(), conocimiento.getMapa());
        
        /*******************************************************************/
        /*Capturar posicion, movmiento, guardar informacion y informar     */
        /*******************************************************************/
        boolean moverse = true;
        IAAgentes miIA = new IAAgentes();
        String movimiento = "";
        while(moverse){
            waitMess();
            try {
            System.out.println("--------------------------------------");
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
                if(msg.contains("x")){
                    miIA.SetObjetivo(miTraductor.getGPS(msg));
                }
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }
            
            movimiento = miIA.NextSteep(sensor, pos);
            sendMessege(miTraductor.Moverse(getAid(), nameAgentSend, movimiento));
            waitMess();
            try {
                System.out.println("--------------------------------------");
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }
            if (msg.contains("CRASHED")||msg.contains("NOT")||msg.contains("BAD")){
                System.err.println("Error en el agente:"+nameAgent);
                sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
                return;
            }
            sendMessege(miTraductor.PedirInformacion(getAid(), nameAgentSend));
            waitMess();
            try {
                System.out.println("--------------------------------------");
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }

            if (msg.contains("CRASHED")||msg.contains("NOT_UNDERTOOD")||msg.contains("BAD_KEY")){
               System.err.println("Error en el agente:"+nameAgent);
               sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
               return;
            }
            pos = miTraductor.getGPS(msg);
            sensor = miTraductor.getSensor(tipo, msg);
            bateria = miTraductor.GetBateria(msg);


            /*******************************************************************/
            /*                   Enviar informacion al controlador             */
            /*******************************************************************/

            sendMessege(miTraductor.ACDatos(getAid(), nameAgentControlador, msg));
            conocimiento.refreshData(pos, sensor);
            dibujar.repaint();
        
        }
        
        
        
        
        /*
        sendMessege(miTraductor.Suscribirse(map,getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            System.out.println(miTraductor.autoSelectACLMessage(q1.Pop()));
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        sendMessege(miTraductor.PreguntarRol(getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        if (msg!="-1"){
            tipo = Integer.valueOf(msg);
        }
        else{
            System.err.println("\n["+this.getName()+"] Error al consguir el rol");
            sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
            return;
        }
        sendMessege(miTraductor.Refuel(getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        if(msg!=miTraductor.GetKey()){
            System.err.println("\n["+this.getName()+"] Error al repostar");
            sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
            return;
        }
        sendMessege(miTraductor.PedirInformacion(getAid(), nameAgentSend));
        waitMess();
        try {
            System.out.println("--------------------------------------");
            msg=miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        if (msg.contains("CRASHED")||msg.contains("NOT_UNDERTOOD")||msg.contains("BAD_KEY")){
           System.err.println("Error en el agente:"+nameAgent);
           sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
           return;
        }
        pos = miTraductor.getGPS(msg);
        sensor = miTraductor.getSensor(tipo, msg);
        bateria = miTraductor.GetBateria(msg);
        bateriaTotal = miTraductor.GetBateriaTotal(msg);
        
        
        
        /*--------Avisar al servidor que todo esta preparado------------------*/
        
        /*
        String mov;
        boolean abajo = false;
        if(pos.getY()==0){
            mov = "S";
        }
        else{
            mov ="N";
        }
        
        for (int cont=0; cont < 100; cont++){
            sendMessege(miTraductor.Moverse(mov, getAid(), nameAgentSend));
            waitMess();
            try {
                System.out.println("--------------------------------------");
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }
            if (msg.contains("CRASHED")||msg.contains("NOT_UNDERTOOD")||msg.contains("BAD_KEY")){
                System.err.println("Error en el agente:"+nameAgent);
                sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
                return;
            }
            sendMessege(miTraductor.PedirInformacion(getAid(), nameAgentSend));
            waitMess();
            try {
                System.out.println("--------------------------------------");
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }
            
            if (msg.contains("CRASHED")||msg.contains("NOT_UNDERTOOD")||msg.contains("BAD_KEY")){
                System.err.println("Error en el agente:"+nameAgent);
                sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
                return;
             }
             pos = miTraductor.getGPS(msg);
             sensor = miTraductor.getSensor(tipo, msg);
             bateria = miTraductor.GetBateria(msg);
             bateriaTotal = miTraductor.GetBateriaTotal(msg);
        }
        System.out.println("Finalizado:"+nameAgent);
        sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
                
                */
    }
}
