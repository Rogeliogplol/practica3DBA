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
 * @author José Luís
 * @author Daniel
 */

public class Agente extends SingleAgent {
    private final VentanaSuper ventanaSuper;
    private DibujarMapa dibujarMapa;
    private final String nameAgent;
    private String nameAgentSend;
    private final String nameAgentControlador;
    private final Traductor miTraductor;
    private final MessageQueue q1;
    private final Conocimiento conocimiento;
    private final boolean parado;
    private int Rol;

    public Agente(AgentID aid, String nameAgentSend, String nameMap, String nameAgentControlador) throws Exception {
        super(aid);
        Rol =-1;
        ventanaSuper = VentanaSuper.getInstance();
        nameAgent = aid.name;
        this.nameAgentSend = nameAgentSend;
        this.nameAgentControlador = nameAgentControlador;
        q1 = new MessageQueue(100);
        miTraductor = new Traductor();
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
    
    /**
    * Bucle de ejecución del agente
    * 
    * @author Juan Manuel Navarrete Carrascosa
    * @author José Luís
    * @author Daniel
    */
    
    @Override
    public void execute(){
        String msg="";
        String state="";
        Posicion pos;
        int tipo = -1;
        int[][] sensor;
        int bateria;
        
        /*******************************************************************/
        /*                   Conseguir clave desde el contorlador          */
        /*******************************************************************/
        waitMess();

        try{
            msg=miTraductor.CAautoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("\n["+this.getName()+"] Error al conseguir key del controlador");
        }
        
        /*******************************************************************/
        /*                   Checkin al servidor                           */
        /*******************************************************************/
        
        sendMessege(miTraductor.preguntarRol(getAid(), nameAgentSend));
        waitMess();
        
        try {
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
            Rol = Integer.valueOf(msg);
        } catch (InterruptedException ex) {
            System.err.println("\n["+this.getName()+"]Error al sacar mensaje");
        }
        
        if (!msg.equals("-1")){
            tipo = Integer.valueOf(msg);
        } else{
            System.err.println("\n["+this.getName()+"] Error al consguir el rol");
            sendMessege(miTraductor.finalizar(getAid(), nameAgent));
            
            return;
        }
        
        /*******************************************************************/
        /*                   Enviar rol al controlador                     */
        /*******************************************************************/
        sendMessege(miTraductor.ACSendRol(getAid(), nameAgentControlador, msg));
        waitMess();
        
        try {
            msg = miTraductor.CAautoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("\n["+this.getName()+"]Error al sacar mensaje");
        }
        
        if(!msg.equals("OK")){
            return;
        }
        
        /*******************************************************************/
        /*                   refuel                                        */
        /*******************************************************************/
        
        sendMessege(miTraductor.refuel(getAid(), nameAgentSend));
        waitMess();
        
        try {
            msg = miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        /*******************************************************************/
        /*                   Pedir informacion al servidor                 */
        /*******************************************************************/
        
        sendMessege(miTraductor.pedirInformacion(getAid(), nameAgentSend));
        waitMess();
        
        try {
            state=miTraductor.autoSelectACLMessage(q1.Pop());
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        if (state.contains("CRASHED")||state.contains("NOT_UNDERTOOD")||state.contains("BAD_KEY")){
           System.err.println("Error en el agente:"+nameAgent);
        }
        
        pos = miTraductor.getGPS(state);
        sensor = miTraductor.getSensor(tipo, state);
        bateria = miTraductor.getBateria(state);
        
        /*******************************************************************/
        /*                   Enviar informacion al controlador             */
        /*******************************************************************/
        
        sendMessege(miTraductor.ACDatos(getAid(), nameAgentControlador, state));
        conocimiento.refreshData(pos, sensor);
        String NombreRoles[] = {"Mosca", "Pájaro", "Halcón"};
        dibujarMapa = new DibujarMapa(conocimiento.getMapa(), nameAgent, bateria, conocimiento.getPosicion(), NombreRoles[Rol]);
        ventanaSuper.addPanel(dibujarMapa);
        
        /*******************************************************************/
        /*Capturar posicion, movmiento, guardar informacion y informar     */
        /*******************************************************************/
        
        boolean moverse = true;
        IAAgentes miIA = new IAAgentes(Rol, conocimiento);
        String movimiento;
        
        while(moverse){
            waitMess();
            
            try {
                ACLMessage temporal = q1.Pop();
                msg=miTraductor.autoSelectACLMessage(temporal);
                
                if(msg.contains("x")){
                    miIA.setObjetivo(miTraductor.getGPS(msg));
                    miIA.setDemas(miTraductor.getGPSdemas(temporal),pos);
                    dibujarMapa.setObjetivo(miIA.getObjetivo());
                }
            } catch (InterruptedException ex) {
                System.err.println("Error al sacar mensaje");
            }
            
            if(bateria<=10&&!parado){
                /*******************************************************************/
                /*                   refuel                                        */
                /*******************************************************************/
                sendMessege(miTraductor.refuel(getAid(), nameAgentSend));
                waitMess();
                
                try {
                    msg = miTraductor.autoSelectACLMessage(q1.Pop());
                } catch (InterruptedException ex) {
                    System.err.println("Error al sacar mensaje");
                }
            }
            if(!parado){
                movimiento = miIA.nextSteep(sensor, pos);
                
                if(!(movimiento.contains("GOAL")||movimiento.contains("ERROR"))){
                    sendMessege(miTraductor.moverse(getAid(), nameAgentSend, movimiento));
                    waitMess();
                    
                    try {
                        msg=miTraductor.autoSelectACLMessage(q1.Pop());
                    } catch (InterruptedException ex) {
                        System.err.println("Error al sacar mensaje");
                    }
                    
                    if (msg.contains("CRASHED")||msg.contains("NOT")||msg.contains("BAD")){
                        System.err.println("Error en el agente:"+nameAgent + "al moverse");
                    }
                }
            }
            if(!parado){
                sendMessege(miTraductor.pedirInformacion(getAid(), nameAgentSend));
                waitMess();
                
                try {
                    state=miTraductor.autoSelectACLMessage(q1.Pop());
                } catch (InterruptedException ex) {
                    System.err.println("Error al sacar mensaje");
                }

                if (state.contains("CRASHED")||state.contains("NOT_UNDERTOOD")||state.contains("BAD_KEY")){
                    System.err.println("Error al pedir informacion agente:"+nameAgent);
                }
                
                pos = miTraductor.getGPS(state);
                sensor = miTraductor.getSensor(tipo, state);
                bateria = miTraductor.getBateria(state);
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