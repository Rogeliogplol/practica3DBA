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
 * @author Ruben
 * @author Rogelio
 * @author Daniel
 */

public class AgenteControlador extends SingleAgent{
    private final VentanaSuper ventanaSuper;
    private DibujarMapaControlador dibujarMapa;
    private final String [][] AgentesRoles;
    private final String [] NameAgentSend;
    private final String nameServer;
    private final Traductor miTraductor;
    private final String nameMap;
    private final MessageQueue q1, q2, q3, q4, qservidor;
    private final ConocimientoControlador conocimiento;
    private final IAControlador miInteligencia;
    private final boolean [] enobjetivo;
    private final  boolean [] engoal;
    private int energy;
    
    public AgenteControlador(AgentID aid, String nameServer, String[] nameAgentSend, String nameMap) throws Exception {
        super(aid);
        ventanaSuper=VentanaSuper.getInstance();
        conocimiento = new ConocimientoControlador(100, 100);
        AgentesRoles = new String [nameAgentSend.length][3];
        this.nameMap = nameMap;
        this.nameServer = nameServer;
        enobjetivo = new boolean [nameAgentSend.length];
        NameAgentSend = new String [nameAgentSend.length];
        engoal = new boolean [nameAgentSend.length];
        for (int cont=0; cont < nameAgentSend.length; cont++){
            enobjetivo[cont] = false;
            engoal[cont] = false;
            AgentesRoles[cont][0] = nameAgentSend[cont];
            NameAgentSend[cont] = nameAgentSend[cont];
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
    
    /**
    * Al llegar al objetivo se les aumenta su valor de negociación
    * 
    * @param id Id del dron
    * 
    * @author SRJota Roger
    */
    
    public void AumentarValor(int id){
        float valor = Float.valueOf(AgentesRoles[id][2]);
        valor = (float) (valor * 1.25);
        AgentesRoles[id][2] = String.valueOf(valor);
        
    }
    
    /**
    * Por cada paso que da el dron, su valor es disminuido
    * 
    * @param id Id del dron
    * 
    * @author SRJota Daniel Roger
    */
    
    public void DisminuirValor(int id){
        float valor = Float.valueOf(AgentesRoles[id][2]);
        valor = (float) (valor * 0.01);
        AgentesRoles[id][2] = String.valueOf(valor);
        
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
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendMessege(ACLMessage msg){
        this.send(msg);
    }
    
    private void sendMessege(ACLMessage[] msg){
        for (ACLMessage msg1 : msg) {
            this.send(msg1);
        }
    }
    
    private void waitMessServer(){
        while (qservidor.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void waitMessDrons(){
        while (q1.isEmpty()||q2.isEmpty()||q3.isEmpty()||q4.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
    * Bucle de ejecución del controlador
    * 
    * @author Juan Manuel Navarrete Carrascosa
    * @author Rubén Orgaz Baena
    * @author Daniel
    */
    
    @Override
    public void execute(){
        
        String msg;
        
        /*******************************************************************/
        /*                   Suscripcion                                   */
        /*******************************************************************/
        
        
        sendMessege(miTraductor.suscribirse(nameMap, getAid(), nameServer));
        waitMessServer();
        try {
            msg=miTraductor.autoSelectACLMessage(qservidor.Pop());
            if(msg.contains("BAD")){
                System.err.println("Error al suscribirse: "+ msg);
                return;
            }
            else{
                miTraductor.setKey(msg);
            }
        } catch (InterruptedException ex) {
            System.err.println("Error al sacar mensaje");
        }
        
        
        /*******************************************************************/
        /*                   Enviar Key a los drones                       */
        /*******************************************************************/
        sendMessege(miTraductor.CAsendKey(miTraductor.getKey(), getAid(), NameAgentSend));
        
        /*******************************************************************/
        /*                   Esperar a los roles                           */
        /*******************************************************************/
        waitMessDrons();
        try {
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
        boolean encontradogoal;
        Posicion[] posicion= new Posicion[4];
        Posicion[] posgoals = new Posicion[4];
        Posicion[] posgoaltemporal = new Posicion[4];
        Posicion[] arraypos = new Posicion[4];
        for(int cont=0; cont<4; cont++){
            arraypos[cont] = new Posicion();
            posgoals[cont] = new Posicion();
            posgoaltemporal[cont] = new Posicion();
        }
        int iteraciones =0;
        ArrayList <ArrayList <Integer> > baterias = new ArrayList ();
        
        for (int cont=0; cont < 4; cont++){
            baterias.add(new ArrayList ());
        }

        dibujarMapa = new DibujarMapaControlador("Vista controlador", conocimiento.getMapa());
        ventanaSuper.addPanel(dibujarMapa);
        while(!TodosEnGoal()){
            waitMessDrons();
            //Conseguir la posicion de los agentes
            try{
                int bateriatemporal;
                msg=miTraductor.autoSelectACLMessage(q1.Pop());
                if(msg.contains("true")){
                    engoal[0] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[0][1]), msg), 0);
                posicion[0] = miTraductor.getGPS(msg);
                baterias.get(0).add(miTraductor.getBateria(msg));
                if(posicion[0].isEqual(posgoals[0]))
                    enobjetivo[0]=true;
                bateriatemporal = miTraductor.getBateriaTotal(msg);
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                
                msg=miTraductor.autoSelectACLMessage(q2.Pop());
                if(msg.contains("true")){
                    engoal[1] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[1][1]), msg), 1);
                posicion[1] = miTraductor.getGPS(msg);
                baterias.get(1).add(miTraductor.getBateria(msg));
                
                if(posicion[1].isEqual(posgoals[1]))
                    enobjetivo[1]=true;
                
                bateriatemporal = miTraductor.getBateriaTotal(msg);
                
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                msg=miTraductor.autoSelectACLMessage(q3.Pop());
                
                if(msg.contains("true")){
                    engoal[2] =true;
                }
                
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[2][1]), msg), 2);
                posicion[2] = miTraductor.getGPS(msg);
                baterias.get(2).add(miTraductor.getBateria(msg));
                
                if(posicion[2].isEqual(posgoals[2]))
                    enobjetivo[2]=true;
                
                bateriatemporal = miTraductor.getBateriaTotal(msg);
                
                if (energy > bateriatemporal){
                    energy = bateriatemporal;
                }
                
                msg=miTraductor.autoSelectACLMessage(q4.Pop());
                
                if(msg.contains("true")){
                    engoal[3] =true;
                }
                conocimiento.refreshData(miTraductor.getGPS(msg), miTraductor.getSensor(Integer.valueOf(AgentesRoles[3][1]), msg), 3);
                posicion[3] = miTraductor.getGPS(msg);
                baterias.get(3).add(miTraductor.getBateria(msg));
                
                if(posicion[3].isEqual(posgoals[3]))
                    enobjetivo[3]=true;
                
                bateriatemporal = miTraductor.getBateriaTotal(msg);
                
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
                        posgoals[cont].set(arraypos[cont]);
                        posgoaltemporal[cont].set(arraypos[cont]);
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
            } else{
                if(!miInteligencia.isAsignadoATodos()){
                    arraypos = miInteligencia.calcularSitioParaGoal(posicion, conocimiento.getMapa());
                    for(int cont=0; cont < 4; cont++){
                        if(!arraypos[cont].isEqual(new Posicion(-1,-1))){
                            posgoals[cont].set(arraypos[cont]);
                            posgoaltemporal[cont].set(arraypos[cont]);
                        }
                    }
                }

                for(int cont=0;cont<posgoaltemporal.length; cont++){
                    posgoaltemporal[cont].set(posgoals[cont]);
                }
                
                sendMessege(miTraductor.CAsendPosicion(getAid(), NameAgentSend, -1, posgoaltemporal, posicion));
            }
            iteraciones++;
        } 
    }
}