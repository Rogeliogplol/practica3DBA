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
import practica3dba.MessageQueue;
import ia.Posicion;
import practica3dba.Traductor;
import gui.VentanaSuper;
/**
 *
 * @author SRJota
 */
public class Agente extends SingleAgent {
    
    private VentanaSuper ventanaSuper;
    private DibujarMapa dibujarMapa;
    private String nameAgent;
    private String nameAgentSend;
    private String nameAgentControlador;
    private String map;
    private Traductor miTraductor;
    private MessageQueue q1;
    private Integer fuel;
    private Conocimiento conocimiento;
    private boolean parado;
    private int Rol;

    public Agente(AgentID aid, String _nameAgentSend, String _nameMap, String _nameAgentControlador) throws Exception {
        super(aid);
        Rol =-1;
        ventanaSuper = VentanaSuper.getInstance();
        nameAgent = aid.name;
        nameAgentSend = _nameAgentSend;
        nameAgentControlador = _nameAgentControlador;
        map = _nameMap;
        q1 = new MessageQueue(100);
        miTraductor = new Traductor();
        fuel = 100;
        conocimiento = new Conocimiento(100, 100);
        parado = false;
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
            //System.out.println("\n["+this.getName()+"]-->["+msg.getSender().name+"] Encolando: "+msg.getContent());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendMessege(ACLMessage msg){
        //System.out.println("------>"+msg.getSender().name+"-->"+msg.getReceiver().name+": "+msg.getContent());
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
        String state="";
        Posicion pos = new Posicion(-1,-1);
        int tipo = -1;
        int[][] sensor;
        int bateria;
        int bateriaTotal;
        //System.out.println();
        
        /*******************************************************************/
        /*                   Conseguir clave desde el contorlador          */
        /*******************************************************************/
        waitMess();
        //System.out.println("--------------------------------------");
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
            //System.out.println("--------------------------------------");
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
            Rol = Integer.valueOf(msg);
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
            //System.out.println("--------------------------------------");
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
            //System.out.println("--------------------------------------");
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
            //System.out.println("--------------------------------------");
            state=miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        if (state.contains("CRASHED")||state.contains("NOT_UNDERTOOD")||state.contains("BAD_KEY")){
           System.err.println("Error en el agente:"+nameAgent);
           //sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
           //return;
        }
        pos = miTraductor.getGPS(state);
        sensor = miTraductor.getSensor(tipo, state);
        bateria = miTraductor.GetBateria(state);
        
        
        /*******************************************************************/
        /*                   Enviar informacion al controlador             */
        /*******************************************************************/
        
        sendMessege(miTraductor.ACDatos(getAid(), nameAgentControlador, state));
        conocimiento.refreshData(pos, sensor);
        String NombreRoles[] = {"Mosca", "Pájaro", "Halcón"};
        dibujarMapa = new DibujarMapa(this.getName(), conocimiento.getMapa(), nameAgent, bateria, conocimiento.getPosicion(), NombreRoles[Rol]);
        ventanaSuper.addPanel(dibujarMapa);
        
        /*******************************************************************/
        /*Capturar posicion, movmiento, guardar informacion y informar     */
        /*******************************************************************/
        boolean moverse = true;
        IAAgentes miIA = new IAAgentes(Rol, conocimiento);
        String movimiento = "";
        while(moverse){
            waitMess();
            try {
            //System.out.println("--------------------------------------");
                ACLMessage temporal = q1.Pop();
                msg=miTraductor.autoSelectACLMessage(temporal);
                if(msg.contains("x")){
                    miIA.SetObjetivo(miTraductor.getGPS(msg));
                    miIA.SetDemas(miTraductor.getGPSdemas(temporal),pos);
                    dibujarMapa.setObjetivo(miIA.GetObjetivo());
                }
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }
            if(bateria<=10&&!parado){
                /*******************************************************************/
                /*                   Refuel                                        */
                /*******************************************************************/
                sendMessege(miTraductor.Refuel(getAid(), nameAgentSend));
                waitMess();
                try {
                    //System.out.println("--------------------------------------");
                    msg = miTraductor.autoSelectACLMessage(q1.Pop());
                } catch (InterruptedException ex) {
                    System.err.println("Error al sacar mensaje");
                }
            }
            if(!parado){
                movimiento = miIA.NextSteep(sensor, pos);
                if(!(movimiento.contains("GOAL")||movimiento.contains("ERROR"))){
                    sendMessege(miTraductor.Moverse(getAid(), nameAgentSend, movimiento));
                    waitMess();
                    try {
                        //System.out.println("--------------------------------------");
                        msg=miTraductor.autoSelectACLMessage(q1.Pop());
                    } catch (InterruptedException ex) {
                        System.err.println("Error al sacar mensaje");
                    }
                    if (msg.contains("CRASHED")||msg.contains("NOT")||msg.contains("BAD")){
                        System.err.println("Error en el agente:"+nameAgent + "al moverse");
                        //sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
                        //return;
                    }
                }
            }
            if(!parado){
                sendMessege(miTraductor.PedirInformacion(getAid(), nameAgentSend));
                waitMess();
                try {
                    //System.out.println("--------------------------------------");
                    state=miTraductor.autoSelectACLMessage(q1.Pop());
                } catch (InterruptedException ex) {
                    System.err.println("Error al sacar mensaje");
                }

                if (state.contains("CRASHED")||state.contains("NOT_UNDERTOOD")||state.contains("BAD_KEY")){
                    System.err.println("Error al pedir informacion agente:"+nameAgent);
                    //sendMessege(miTraductor.Finalizar(getAid(), nameAgent));
                    //return;
                }
                pos = miTraductor.getGPS(state);
                sensor = miTraductor.getSensor(tipo, state);
                bateria = miTraductor.GetBateria(state);
            }

            /*******************************************************************/
            /*                   Enviar informacion al controlador             */
            /*******************************************************************/

            sendMessege(miTraductor.ACDatos(getAid(), nameAgentControlador, state));
            conocimiento.refreshData(pos, sensor);
            dibujarMapa.setBateria(bateria);
        }
        
    }
}
