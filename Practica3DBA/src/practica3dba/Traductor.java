package practica3dba;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import ia.Posicion;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author SRJota
 * @author José Luís
 * @author Rubén
 */

public class Traductor {
    private final int[] tipoAgentes = {3,5,11};
    private String key;
    
    public Traductor (){
        key = "";
    }
    
    public String getKey (){
        return key;
    }
    
    public void setKey (String key){
        this.key = key;
    }
    
    /**
    * Obtiene el radar a partir de la cadena JSON
    * 
    * @param tipo El tipo de dron
    * @param msg El JSON del sensor
    * 
    * @return La información del sensor
    * 
    * @author Rubén Orgaz Baena
    */
    
    public int[][] getSensor (int tipo, String msg){
        int tamanho = tipoAgentes[tipo];
        int [][] sensor = new int[tamanho][tamanho];
        
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            ArrayList Radar = (ArrayList)JObject.get("sensor");
            int k = 0;
            
            for(int i=0;i<tamanho;i++){
                for(int j=0; j<tamanho; j++){
                    sensor[i][j] = Integer.parseInt(Radar.get(k).toString());
                    k++;
                }
            }
        } catch (Exception ex){
            System.err.println("\n\nError al cargar el radar = " + ex.getMessage());
            sensor[0][0]=-1;
        }
        
        return sensor;
    }
    
    /**
    * Obtiene los valores del gps a partir de la candena JSON
    * 
    * @param msg La cadena JSON del GPS
    * 
    * @return La posición del GPS
    * 
    * @author Rubén Orgaz Baena
    */
    
    public Posicion getGPS(String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            int x = Integer.valueOf(JObject.get("x").toString());
            int y = Integer.valueOf(JObject.get("y").toString());
            return new Posicion(x,y);
        } catch (Exception ex){
            System.err.println("\n\nError al cargar el GPS= " + ex.getMessage());
            return new Posicion(-1,-1);
        }
    } 
    
    /**
    * Obtiene la posicion de los drones a partir del mensaje JSON
    * 
    * @param msg El mensaje a enviar
    * 
    * @return Array con la posición de los drones
    * 
    * @author Rubén Orgaz Baena
    */
    
    public Posicion[] getGPSdemas(ACLMessage msg){
        Posicion [] posdemas = new Posicion[4];
        String a;
        
        for(int cont=0; cont<4; cont++){
            a="a"+cont;
            posdemas[cont] = new Posicion();
            posdemas[cont] = getGPS(getPosicionMsg(msg.getContent(), a));
        }
        
        return posdemas;
    }
    
    /**
    * Obtiene el valor de goal de la cadena JSON
    * 
    * @param msg El mensaje a enviar
    * 
    * @return Si está o no en el goal
    * 
    * @author Rubén Orgaz Baena
    */
    
    public boolean getGoal (String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject) obj;
            boolean goal = (boolean)JObject.get("goal");
            return goal;
        } catch (Exception ex){
            System.err.println("\n\nError al cargar el goal= " + ex.getMessage());
            return false;
        }
    }
    
    /**
    * Obtiene la cantidad de bateria de la cadena JSON para cada dron
    * 
    * @param msg El mensaje a enviar
    * 
    * @return La batería
    * 
    * @author Rubén Orgaz Baena
    */
    
    public int getBateria (String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject) obj;
            int bateria = Integer.valueOf(JObject.get("battery").toString());
            return bateria;
        } catch (Exception ex){
            System.err.println("\n\nError al cargar la bateria= " + ex.getMessage());
            return -1;
        }
    }
    
    /**
    * Obtiene la bateria restante del mundo de la cadena JSON
    * 
    * @param msg El mensaje a enviar
    * 
    * @return La batería total
    * 
    * @author Rubén Orgaz Baena
    */
    
    public int getBateriaTotal (String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject) obj;
            int bateria = Integer.valueOf(JObject.get("energy").toString());
            return bateria;
        } catch (Exception ex){
            System.err.println("\n\nError al cargar la bateria total= " + ex.getMessage());
            return -1;
        }
    }
    
    // Lo que se pasa debe estar correcto
    public String transformarJSon(String[][] msg){
        if(msg[0].length!=2){
            System.err.println("Error al transformar a JSon");
            return "";
        }
        
        try{
            JSONObject obj=new JSONObject();
            
            for (String[] msg1 : msg) {
                obj.put(msg1[0], msg1[1]);
            }
            
            return obj.toString();
        } catch (Exception ex) {
            return "";
        }
    }
    
    public ACLMessage enviarControladorPreparado(boolean _estado, int _X, int _Y,AgentID _sender, String _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [3][2];
        msg[0][0]="estado";
        
        if (_estado)
            msg[0][1]="OK";
        else
            msg[0][1]="ERROR";
        
        msg[1][0]="x";
        msg[1][1]=String.valueOf(_X);
        msg[2][0]="y";
        msg[2][1]=String.valueOf(_Y);
        
        outbox.setPerformative(ACLMessage.CONFIRM);
        outbox.setSender(_sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        
        return outbox;
    }
    
    /**
    * Genera el paquete de susripcion al mundo
    * 
    * @param map El nombre del mapa a suscribirse
    * @param sender El AgentID del agente que se suscribe
    * @param receiver El receptor del mensaje
    * 
    * @return El ACLMessage para suscribirse
    * 
    * @author José Luís
    */
    
    public ACLMessage suscribirse(String map, AgentID sender, String receiver) {
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="world";
        msg[0][1]=map;
        
        outbox.setPerformative(ACLMessage.SUBSCRIBE);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Genera el paquete para preguntar por el rol en el servidor
    * 
    * @param sender El AgentID del agente que va a hacer checkin
    * @param receiver El receptor del mensaje
    * 
    * @return El ACLMessage para hacer checkin
    * 
    * @author José Luís
    */
    
    public ACLMessage preguntarRol(AgentID sender, String receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="command";
        msg[0][1]="checkin";
        msg[1][0]="key";
        msg[1][1]=key;
        
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Genera el paquete para enviar una orden de movimiento al servidor
    * 
    * @param sender El AgentID del agente que va a enviar la orden de movimiento
    * @param receiver El receptor del mensaje
    * @param movimiento El movimiento a efectuar
    * 
    * @return El ACLMessage para hacer la orden de movimiento
    * 
    * @author José Luís
    */
    
    public ACLMessage moverse(AgentID sender, String receiver, String movimiento){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="command";
        msg[0][1]="move"+movimiento;
        msg[1][0]="key";
        msg[1][1]=key;
        
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Genera un paquete para pedir una recarga de bateria al servidor
    * 
    * @param sender El AgentID del agente que va a hacer el refuel
    * @param receiver El receptor del mensaje
    * 
    * @return El ACLMessage para hacer el refuel
    * 
    * @author José Luís
    */
    
    public ACLMessage refuel(AgentID sender, String receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="command";
        msg[0][1]="refuel";
        msg[1][0]="key";
        msg[1][1]=key;
        
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Genera el ACLMessage con el que se pide la información al servidor.
    * 
    * @param sender El AgentID del agente que va a pedir la información
    * @param receiver El receptor del mensaje
    * 
    * @return El ACLMessage para pedir la información
    * 
    * @author Rubén Orgaz Baena
    */
    
    public ACLMessage pedirInformacion(AgentID sender, String receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="key";
        msg[0][1]=key;
        
        outbox.setPerformative(ACLMessage.QUERY_REF);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Genera el ACLMessage para finalizar el mundo.
    * 
    * @param sender El AgentID del agente que va a comunicar la finalización de
    * la conexión
    * @param receiver El receptor del mensaje
    * 
    * @return El ACLMessage para comunicar la finalización de la conexión
    * 
    * @author Rubén Orgaz Baena
    */
    
    public ACLMessage finalizar(AgentID sender, String receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="key";
        msg[0][1]=key;
        
        outbox.setPerformative(ACLMessage.CANCEL);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    public String getPosicionMsg (String msg){
        if(!msg.contains("posicion"))
            return "";
        
        String Result;
        
        try{
            JSONParser parser= new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            Result = JObject.get("posicion").toString();
            
            if(key.equals("")){
                key = Result;
            }
            
            return Result;
        } catch (Exception ex){
            System.err.println("Fallo en getResult ("+msg+") : " + ex.toString());
            return ("");
        }
    }
    
    public String getPosicionMsg (String msg, String buscar){
        if(!msg.contains(buscar))
            return "";
        
        String Result;
        
        try{
            JSONParser parser= new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            Result = JObject.get(buscar).toString();
            
            if(key.equals("")) {
                key = Result;
            }
            
            return Result;
        } catch (Exception ex){
            System.err.println("Fallo en getResult ("+msg+") : " + ex.toString());
            return ("");
        }
    }
    
    /**
    * Obtiene el resultado de la cadena JSON
    * 
    * @param msg El mensaje del que se quiere extraer el valor de result
    * 
    * @return El valor de result
    * 
    * @author Rubén Orgaz Baena
    */
    
    public String getResult (String msg){
        if(!msg.contains("result"))
            return "";
        
        String Result;
        
        try{
            JSONParser parser= new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            Result = JObject.get("result").toString();
            
            if(key.equals("")){
                key = Result;
            }
            
            return Result;
        } catch (Exception ex){
            System.err.println("Fallo en getResult ("+msg+") : " + ex.toString());
            return ("");
        }
    }
    
    /**
    * Extrae la key del paquete JSON para el controlador
    * 
    * @param msg El mensaje enviado por el servidor al controlador que contiene la key
    * 
    * @return El valor de la key
    * 
    * @author Rubén Orgaz Baena
    */
    
    public String keyToControlador (String msg){
        if(!msg.contains("key"))
            return "";
        
        String Result;
        
        try{
            JSONParser parser= new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            Result = JObject.get("key").toString();
            
            if(key.equals("")) {
                key = Result;
            }
            
            return Result;
        }
        catch (Exception ex){
            System.err.println("Fallo en getResult ("+msg+") : " + ex.toString());
            return ("");
        }
    }
    
    /**
    * Obtiene el rol del paquete JSON
    * 
    * @param msg El mensaje que contiene el rol
    * 
    * @return El ID del rol
    * 
    * @author Rubén Orgaz Baena
    */
    
    public int getRol (String msg){
        if(!msg.contains("rol"))
            return -2;
        
        int Rol;
        
        try{
            JSONParser parser= new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            Rol = Integer.valueOf(JObject.get("rol").toString());
            
            return Rol;
        }
        catch (Exception ex){
            System.err.println("Fallo en getRol ("+msg+") : " + ex.toString());
            return (-1);
        }
    }
    
    /**
    * Descifra mensaje del servidor
    * 
    * @param msg El mensaje recibido
    * 
    * @return La perfomativa recibida
    * 
    * @author Rubén Orgaz Baena
    */
    
    public String autoSelectACLMessage (ACLMessage msg){
        if (msg.getPerformativeInt() == ACLMessage.FAILURE){
            System.err.println("Fail: "+ getResult(msg.getContent()));
            
            return getResult(msg.getContent());
        } else if (msg.getPerformativeInt()==ACLMessage.NOT_UNDERSTOOD){
            System.err.println("Not Understood: " + getResult(msg.getContent()));
            
            return "NotUnderstood";
        } else if (msg.getPerformativeInt()==ACLMessage.INFORM){
            String Result = getResult(msg.getContent());
            int Rol = getRol(msg.getContent());
            
            if (Rol>=0)
                return String.valueOf(Rol);
            else {
                if(msg.getContent().contains("posicion") && Result.contains("-1")){
                    return getPosicionMsg(msg.getContent());
                }
            }
            
            return Result;
        } else if (msg.getPerformativeInt()==ACLMessage.REFUSE){
            System.err.println("Refuse: "+getResult(msg.getContent()));
            
            return "Fail";
        } else
            return "NotUnderstood";
    }
    
    /**
    * Descifra mensaje del controlador
    * 
    * @param msg El mensaje recibido
    * 
    * @return La perfomativa recibida
    * 
    * @author José Luís
    */
    
    public String CAautoSelectACLMessage (ACLMessage msg){
        if (msg.getPerformativeInt() == ACLMessage.INFORM){
            String _Result = getResult(msg.getContent());
            String _Key = keyToControlador(msg.getContent());
            
            if("".equals(key))
                return _Key;
            else if ("".equals(_Result))
                return String.valueOf(getRol(msg.getContent()));
            else
                return _Result;
        } else if (msg.getPerformativeInt()==ACLMessage.CONFIRM)
            return "OK";
        else if (msg.getPerformativeInt()==ACLMessage.REFUSE)
            return "CANCEL";
        else
            return "NotUnderstood";
    }
    
    /**
    * Crea el ACLMessage con la key del controlador al dron
    * 
    * @param key La key
    * @param sender El controlador
    * @param receiver Los drones que reciben la key
    * 
    * @return El mensaje para enviar la key
    * 
    * @author José Luís
    */
    
    public ACLMessage CAsendKey(String key, AgentID sender, String[] receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="key";
        msg[0][1]=key;     
        
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        
        for (String receiver1 : receiver) {
            outbox.addReceiver(new AgentID(receiver1));
        }
        
        return outbox;             
    }

    /**
    * Crea el ACLMessage para enviar el rol del dron al controlador
    * 
    * @param sender El dron que envía el rol
    * @param receiver El controlador
    * @param rol El rol enviado
    * 
    * @return El mensaje para enviar el rol
    * 
    * @author José Luís
    */
    
    public ACLMessage ACSendRol(AgentID sender, String receiver, String rol) {
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="rol";
        msg[0][1]=rol;
        
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Crea el ACLMessage para enviar los datos del dron al controlador
    * 
    * @param sender El dron que envía los datos
    * @param receiver El controlador
    * 
    * @return El mensaje para enviar los datos
    * 
    * @author José Luís
    */
    
    public ACLMessage ACDatos(AgentID sender, String receiver, String msg) {
        ACLMessage outbox = new ACLMessage();      
        
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(sender);
        
        String message [][] = new String [1][2];
        message[0][0]="result";
        message[0][1]=msg;   
        
        outbox.setContent(transformarJSon(message));
        outbox.addReceiver(new AgentID(receiver));
        
        return outbox;
    }
    
    /**
    * Crea el ACLMessage para enviar un OK del controlador a un dron
    * 
    * @param sender El controlador
    * @param receiver El dron que envía el OK
    * 
    * @return El mensaje para enviar el OK
    * 
    * @author José Luís
    */
    
    public ACLMessage CAsendOK(AgentID sender, String[] receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="result";
        msg[0][1]="OK";
        
        outbox.setPerformative(ACLMessage.CONFIRM);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        
        for (String _receiver1 : receiver) {
            outbox.addReceiver(new AgentID(_receiver1));
        }
        
        return outbox;             
    }
    
    /**
    * Crea el ACLMessage para enviar las posiciones de los drones a todos los drones
    * 
    * @return Los mensajes para enviar las posiciones a los drones
    * 
    * @author José Luís
    */
    
    public ACLMessage[] CAsendPosicion(AgentID sender, String[] receiver, int pasos, Posicion[] p, Posicion[] posDemas){
        ACLMessage[] outbox = new ACLMessage[p.length];
        String msg [][] = new String [2+posDemas.length][2];
        
        for (int cont=0; cont < receiver.length; cont++) {
            outbox[cont] = new ACLMessage();
            outbox[cont].setPerformative(ACLMessage.INFORM);
            outbox[cont].setSender(sender);
            outbox[cont].addReceiver(new AgentID(receiver[cont]));
            
            msg[0][0]="result";
            msg[0][1]=String.valueOf(pasos);
            msg[1][0]="posicion";
            
            String posicion [][] = new String [2][2];
            posicion[0][0] = "x";
            posicion[0][1] = String.valueOf(p[cont].getX());
            posicion[1][0] = "y";
            posicion[1][1] = String.valueOf(p[cont].getY());
            
            msg[1][1]=transformarJSon(posicion);
            
            for(int i=0; i < posDemas.length; i++){
                msg[2+i][0]="a"+i;
                String d [][] = new String [2][2];
                d[0][0] = "x";
                d[0][1] = String.valueOf(posDemas[i].getX());
                d[1][0] = "y";
                d[1][1] = String.valueOf(posDemas[i].getY());
                msg[2+i][1]=transformarJSon(d);
            }
            
            outbox[cont].setContent(transformarJSon(msg));
        }
        
        return outbox;             
    }
    
    
    /**
    * Crea el ACLMessage para enviar un cancel del controlador a los drones
    * 
    * @param sender El controlador
    * @param receiver Los drones que recibirán el cancel
    * 
    * @return El mensaje para enviar el cancel
    * 
    * @author José Luís
    */
    
    public ACLMessage CAsendCANCEL(AgentID sender, String[] receiver){
        ACLMessage outbox = new ACLMessage();
        
        String msg [][] = new String [1][2];
        msg[0][0]="result";
        msg[0][1]="CANCEL";
        
        outbox.setPerformative(ACLMessage.CANCEL);
        outbox.setSender(sender);
        outbox.setContent(transformarJSon(msg));
        
        for (String _receiver1 : receiver) {
            outbox.addReceiver(new AgentID(_receiver1));
        }
        
        return outbox;             
    }
}