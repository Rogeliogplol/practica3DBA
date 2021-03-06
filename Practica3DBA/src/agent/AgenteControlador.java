/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import ia.ConocimientoControlador;
import ia.Posicion;
import practica3dba.MessageQueue;
import practica3dba.Traductor;
import gui.DibujarMapaControlador;
import ia.IAControlador;
import java.util.ArrayList;
import gui.VentanaSuper;

/**
 *
 * @author SRJota
 */
public class AgenteControlador extends SingleAgent{
    private VentanaSuper ventanaSuper;
    private DibujarMapaControlador dibujarMapa;
    String [][] AgentesRoles;
    String [] NameAgentSend;
    private String nameAgent;
    private String nameServer;
    private Traductor miTraductor;
    private String nameMap;
    MessageQueue q1, q2, q3, q4, qservidor;
    private ConocimientoControlador conocimiento;
    private IAControlador miInteligencia;
    boolean [] enobjetivo;
    boolean [] engoal;
    private int energy;
    
    
    public boolean TodosEnGoal(){
        for(int cont=0;cont<4; cont++){
            if(!engoal[cont])
                return false;
        }
        return true;
    }
    
    public boolean AlgunoEnObjetivo(){
        boolean algunoobjetivo = false;
        for(int cont=0; cont<4; cont++){
            if(enobjetivo[cont]){
                AumentarValor(cont);
                algunoobjetivo = true;
            }
            else{
                DisminuirValor(cont);
            }
        }
        return algunoobjetivo;
    }
    
    public void AumentarValor(int id){
        float valor = Float.valueOf(AgentesRoles[id][2]);
        valor = (float) (valor * 1.25);
        AgentesRoles[id][2] = String.valueOf(valor);
        
    }
    
    public void DisminuirValor(int id){
        float valor = Float.valueOf(AgentesRoles[id][2]);
        valor = (float) (valor * 0.01);
        AgentesRoles[id][2] = String.valueOf(valor);
        
    }
    
    public AgenteControlador(AgentID aid, String _nameServer, String[] _nameAgentSend, String _nameMap) throws Exception {
        super(aid);
        ventanaSuper=VentanaSuper.getInstance();
        conocimiento = new ConocimientoControlador(100, 100);
        AgentesRoles = new String [_nameAgentSend.length][3];
        nameMap = _nameMap;
        nameServer = _nameServer;
        nameAgent = aid.name;
        enobjetivo = new boolean [_nameAgentSend.length];
        NameAgentSend = new String [_nameAgentSend.length];
        engoal = new boolean [_nameAgentSend.length];
        for (int cont=0; cont < _nameAgentSend.length; cont++){
            enobjetivo[cont] = false;
            engoal[cont] = false;
            AgentesRoles[cont][0] = _nameAgentSend[cont];
            NameAgentSend[cont] = _nameAgentSend[cont];
        }
        q1 = new MessageQueue(100);
        q2 = new MessageQueue(100);
        q3 = new MessageQueue(100);
        q4 = new MessageQueue(100);
        qservidor = new MessageQueue(100);
        miTraductor = new Traductor();
        miInteligencia = new IAControlador();
        energy = Integer.MAX_VALUE;
    }
    
    String valorPaso(String tipo){
        int valor;
        float valores[] = {2/9*100,1/25*100,4/121*100};
        valor = Integer.parseUnsignedInt(tipo);
        return String.valueOf(valores[valor]);
        
    }
    
    @Override
    public void onMessage(ACLMessage msg)  {
        try {
            String Receiver = msg.getReceiver().name;
            String Sender = msg.getSender().name;
            if(NameAgentSend[0].equals(Sender))
                q1.Push(msg); // Cada mensaje nuevo que llega se encola en el orden de llegada
            else if(NameAgentSend[1].equals(Sender))
                q2.Push(msg);
            else if(NameAgentSend[2].equals(Sender))
                q3.Push(msg);
            else if(NameAgentSend[3].equals(Sender))
                q4.Push(msg);
            else
                qservidor.Push(msg);
            //System.out.println("\n["+this.getName()+"] Encolando: "+msg.getContent());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendMessege(ACLMessage msg){
        this.send(msg);
    }
    
    private void sendMessege(ACLMessage[] msg){
        for(int cont=0; cont < msg.length; cont++)
            this.send(msg[cont]);
    }
    
    
    private void waitMess(){
        while (qservidor.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void waitMess(int cantidad){
        while (q1.isEmpty()||q2.isEmpty()||q3.isEmpty()||q4.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void execute(){
        
        String msg;
        
        /*******************************************************************/
        /*                   Suscripcion                                   */
        /*******************************************************************/
        
        
        sendMessege(miTraductor.Suscribirse(nameMap, getAid(), nameServer));
        waitMess();
        try {
            //System.out.println("--------------------------------------");
            msg=miTraductor.autoSelectACLMessage(qservidor.Pop());
            if(msg.contains("BAD")){
                System.err.println("Error al suscribirse: "+ msg);
                return;
            }
            else{
                //System.out.println("Key: "+ msg);
                miTraductor.SetKey(msg);
            }
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        
        /*******************************************************************/
        /*                   Enviar Key a los drones                       */
        /*******************************************************************/
        sendMessege(miTraductor.CAsendKey(miTraductor.GetKey(), getAid(), NameAgentSend));
        
        /*******************************************************************/
        /*                   Esperar a los roles                           */
        /*******************************************************************/
        waitMess(NameAgentSend.length);
        try {
            //System.out.println("--------------------------------------");
            msg=miTraductor.autoSelectACLMessage(q1.Pop());
            AgentesRoles[0][1] = msg;
            AgentesRoles[0][2] = valorPaso(msg);
            msg=miTraductor.autoSelectACLMessage(q2.Pop());
            AgentesRoles[1][1] = msg;
            AgentesRoles[1][2] = valorPaso(msg);
            msg=miTraductor.autoSelectACLMessage(q3.Pop());
            AgentesRoles[2][1] = msg;
            AgentesRoles[2][2] = valorPaso(msg);
            msg=miTraductor.autoSelectACLMessage(q4.Pop());
            AgentesRoles[3][1] = msg;
            AgentesRoles[3][2] = valorPaso(msg);
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        sendMessege(miTraductor.CAsendOK(getAid(), NameAgentSend));
        
        
        /*******************************************************************/
        /*                   Primera parte busqueda del objetivo           */
        /*******************************************************************/
        boolean encontradogoal=false;
        //boolean enobjetivo = false;
        Posicion[] posicion= new Posicion[4];
        Posicion[] posgoals = new Posicion[4];
        Posicion[] posgoaltemporal = new Posicion[4];
        Posicion goal;
        Posicion[] arraypos = new Posicion[4];
        for(int cont=0; cont<4; cont++){
            arraypos[cont] = new Posicion();
            posgoals[cont] = new Posicion();
            posgoaltemporal[cont] = new Posicion();
        }
        int iteraciones =0;
        int [] pasos = new int[4];
        ArrayList <ArrayList <Integer> > baterias = new ArrayList < ArrayList <Integer> > ();
        for (int cont=0; cont < 4; cont++){
            baterias.add(new ArrayList <Integer>());
        }
        for (int cont=0; cont < 4; cont++)
            pasos[cont] = 20;
        dibujarMapa = new DibujarMapaControlador("Vista controlador", conocimiento.getMapa());
        ventanaSuper.addPanel(dibujarMapa);
        while(!TodosEnGoal()){
            waitMess(NameAgentSend.length);
            //Conseguir la posicion de los agentes
            try{
                int bateriatemporal;
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
                if(msg.contains("true")){
                    engoal[0] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[0][1]), msg), 0);
                posicion[0] = miTraductor.getGPS(msg);
                baterias.get(0).add(miTraductor.GetBateria(msg));
                if(posicion[0].isEqual(posgoals[0]))
                    enobjetivo[0]=true;
                bateriatemporal = miTraductor.GetBateriaTotal(msg);
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                
                msg=miTraductor.autoSelectACLMessage(q2.Pop());
                if(msg.contains("true")){
                    engoal[1] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[1][1]), msg), 1);
                posicion[1] = miTraductor.getGPS(msg);
                baterias.get(1).add(miTraductor.GetBateria(msg));
                if(posicion[1].isEqual(posgoals[1]))
                    enobjetivo[1]=true;
                bateriatemporal = miTraductor.GetBateriaTotal(msg);
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                
                
                msg=miTraductor.autoSelectACLMessage(q3.Pop());
                if(msg.contains("true")){
                    engoal[2] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[2][1]), msg), 2);
                posicion[2] = miTraductor.getGPS(msg);
                baterias.get(2).add(miTraductor.GetBateria(msg));
                if(posicion[2].isEqual(posgoals[2]))
                    enobjetivo[2]=true;
                bateriatemporal = miTraductor.GetBateriaTotal(msg);
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                
                
                msg=miTraductor.autoSelectACLMessage(q4.Pop());
                if(msg.contains("true")){
                    engoal[3] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[3][1]), msg), 3);
                posicion[3] = miTraductor.getGPS(msg);
                baterias.get(3).add(miTraductor.GetBateria(msg));
                if(posicion[3].isEqual(posgoals[3]))
                    enobjetivo[3]=true;
                bateriatemporal = miTraductor.GetBateriaTotal(msg);
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                dibujarMapa.setBateria(energy);
                
                
            }catch (InterruptedException ex){
                System.err.println("Error al sacar mensaje");
            }
            
            
            encontradogoal = miInteligencia.vistoelobjetivo (conocimiento.getMapa());
            if (!encontradogoal){    
                if((iteraciones%30==0) || AlgunoEnObjetivo()){
                    //Se calcula una posicion a donde ir
                    arraypos = miInteligencia.calculateGoalPos(posicion);
                    for(int cont=0; cont < 4; cont++){
                        posgoals[cont].Set(arraypos[cont]);
                        posgoaltemporal[cont].Set(arraypos[cont]);
                        enobjetivo[cont] = false;
                    }
                }
                //Pelea de valores
                boolean[] Para = miInteligencia.quienPara (posicion, posgoaltemporal, 5, AgentesRoles);
                //Se da la direccion a donde ir, si se paran tienen el sitio donde estan
                for(int cont=0;cont<Para.length; cont++){
                    if(Para[cont]/*&&!enobjetivo*/){
                        posgoaltemporal[cont].setX(posicion[cont].getX());
                        posgoaltemporal[cont].setY(posicion[cont].getY());
                        
                    }else{
                        posgoaltemporal[cont].setX(posgoals[cont].getX());
                        posgoaltemporal[cont].setY(posgoals[cont].getY());
                    }
                }
                //Envio a donde ir
                sendMessege(miTraductor.CAsendPosicion(getAid(), NameAgentSend, -1, posgoaltemporal, posicion));
            }
            else{
                if(!miInteligencia.isAsignadoATodos()){
                    //goal = miInteligencia.getPosicionGoal();
                    arraypos = miInteligencia.calcularSitioParaGoal(posicion, conocimiento.getMapa());
                    for(int cont=0; cont < 4; cont++){
                        if(!arraypos[cont].isEqual(new Posicion(-1,-1))){
                            posgoals[cont].Set(arraypos[cont]);
                            posgoaltemporal[cont].Set(arraypos[cont]);
                        }
                    }
                }
                //boolean[] Para = miInteligencia.quienPara (posicion, posgoaltemporal, 5, AgentesRoles);
                for(int cont=0;cont<posgoaltemporal.length; cont++){
                    /*if(Para[cont]&&!enobjetivo){
                        posgoaltemporal[cont].setX(posicion[cont].getX());
                        posgoaltemporal[cont].setY(posicion[cont].getY());
                    }else{
                        posgoaltemporal[cont].setX(posgoals[cont].getX());
                        posgoaltemporal[cont].setY(posgoals[cont].getY());
                    }*/
                    posgoaltemporal[cont].Set(posgoals[cont]);
                }
                sendMessege(miTraductor.CAsendPosicion(getAid(), NameAgentSend, -1, posgoaltemporal, posicion));
                
            }
            iteraciones++;
        }
        
    }
    
}
