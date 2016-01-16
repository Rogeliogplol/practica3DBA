/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3dba;

import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import ia.Posicion;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author SRJota
 */
public class Traductor {

    
    private String Key;
    
    public Traductor (){
        Key = "";
    }
    
    public String GetKey (){
        return Key;
    }
    
    public void SetKey (String _Key){
        Key = _Key;
    }
    
    
    private int[] tipoAgentes = {3,5,11};
    
    public int[][] getSensor (int _tipo, String msg){
        int tamanho = tipoAgentes[_tipo];
        int [][] sensor = new int[tamanho][tamanho];
        try{
            JSONParser parser = new JSONParser();
            //System.out.println("1");
            Object obj = parser.parse(msg);
            //System.out.println("2");
            JSONObject JObject = (JSONObject)obj;
            //System.out.println("3");
            ArrayList Radar = (ArrayList)JObject.get("sensor");
            int k = 0;
            //System.out.println("4: " + Integer.parseInt(Radar.get(k).toString()));
            
            for(int i=0;i<tamanho;i++){
                //for(int j=4;j>=0;j--){
                for(int j=0; j<tamanho; j++){
                    //System.out.println(i+" "+j);
                    sensor[i][j] = Integer.parseInt(Radar.get(k).toString());
                    k++;
                }
            }
        return sensor;
        }
        catch (Exception ex){
            System.err.println("\n\nError al cargar el radar = " + ex.getMessage());
            sensor[0][0]=-1;
            return sensor;
        }
    }
    
    public Posicion getGPS(String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            int x = Integer.valueOf(JObject.get("x").toString());
            int y = Integer.valueOf(JObject.get("y").toString());
            return new Posicion(x,y);
        }
        catch (Exception ex){
            System.err.println("\n\nError al cargar el GPS= " + ex.getMessage());
            return new Posicion(-1,-1);
        }
    } 
    
    public boolean GetGoal (String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject) obj;
            boolean goal = (boolean)JObject.get("goal");
            return goal;
        }
        catch (Exception ex){
            System.err.println("\n\nError al cargar el goal= " + ex.getMessage());
            return false;
        }
    }
    
    public int GetBateria (String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject) obj;
            int bateria = Integer.valueOf(JObject.get("battery").toString());
            return bateria;
        }
        catch (Exception ex){
            System.err.println("\n\nError al cargar la bateria= " + ex.getMessage());
            return -1;
        }
    }
    
    public int GetBateriaTotal (String msg){
        try{
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject) obj;
            int bateria = Integer.valueOf(JObject.get("energy").toString());
            return bateria;
        }
        catch (Exception ex){
            System.err.println("\n\nError al cargar la bateria total= " + ex.getMessage());
            return -1;
        }
    }
    
    //Lo que se pasa debe estar correcto
    public String TransformarJSon(String[][] msg){
        if(msg[0].length!=2){
            System.err.println("Error al transformar a JSon");
            return "";
        }
        try{
            JSONObject obj=new JSONObject();
            for (int cont=0; cont < msg.length; cont++){
                obj.put(msg[cont][0],msg[cont][1]);
            }
            System.out.println(obj.toString());
            return obj.toString();
        }catch (Exception ex){
            return "";
        }
    }
    
    public ACLMessage EnviarControladorPreparado(boolean _estado, int _X, int _Y,AgentID _sender, String _receiver){
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
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    
    public ACLMessage Suscribirse(String _map, AgentID _sender, String _receiver) {
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="world";
        msg[0][1]=_map;
        outbox.setPerformative(ACLMessage.SUBSCRIBE);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage PreguntarRol(AgentID _sender, String _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="command";
        msg[0][1]="checkin";
        msg[1][0]="key";
        msg[1][1]=Key;
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage Moverse(String _movimiento,AgentID _sender, String _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="command";
        msg[0][1]=_movimiento;
        msg[1][0]="key";
        msg[1][1]=Key;
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage Refuel(AgentID _sender, String _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="command";
        msg[0][1]="refuel";
        msg[1][0]="key";
        msg[1][1]=Key;
        outbox.setPerformative(ACLMessage.REQUEST);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage PedirInformacion(AgentID _sender, String _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="key";
        msg[0][1]=Key;
        outbox.setPerformative(ACLMessage.QUERY_REF);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage Finalizar(AgentID _sender, String _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="key";
        msg[0][1]=Key;
        outbox.setPerformative(ACLMessage.CANCEL);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
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
            if(Key==""){
                Key = Result;
            }
            return Result;
        }
        catch (Exception ex){
            System.err.println("Fallo en getResult ("+msg+") : " + ex.toString());
            return ("");
        }
    }
    
    
    
    
    
    
    // Key cuando se recibe desde el controlador
    public String KeyToControlador (String msg){
        if(!msg.contains("key"))
            return "";
        String Result;
        try{
            JSONParser parser= new JSONParser();
            Object obj;
            obj = parser.parse(msg);
            JSONObject JObject = (JSONObject)obj;
            Result = JObject.get("key").toString();
            if(Key==""){
                Key = Result;
            }
            return Result;
        }
        catch (Exception ex){
            System.err.println("Fallo en getResult ("+msg+") : " + ex.toString());
            return ("");
        }
    }
    
    
    
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
    
    public String autoSelectACLMessage (ACLMessage msg){
        if (msg.getPerformativeInt() == ACLMessage.FAILURE){
            System.err.println("Fail: "+ getResult(msg.getContent()));
            return getResult(msg.getContent());
        }
        else if (msg.getPerformativeInt()==ACLMessage.NOT_UNDERSTOOD){
            System.err.println("Not Understood: " + getResult(msg.getContent()));
            return "NotUnderstood";
        }
        else if (msg.getPerformativeInt()==ACLMessage.INFORM){
            String Result = getResult(msg.getContent());
            int Rol = getRol(msg.getContent());
            System.out.println("Inform: "+ Result + "," + Rol);
            if (Rol>=0)
                return String.valueOf(Rol);
            else
                return Result;
        }
        else if (msg.getPerformativeInt()==ACLMessage.REFUSE){
            System.err.println("Refuse: "+getResult(msg.getContent()));
            return "Fail";
        }
        else
            return "NotUnderstood";
    }
    
    
    //Agente para descifrar mensaje controlador
    public String CAautoSelectACLMessage (ACLMessage msg){
        if (msg.getPerformativeInt() == ACLMessage.INFORM){
            String _Result = getResult(msg.getContent());
            String _Key = KeyToControlador(msg.getContent());
            if("".equals(Key))
                return _Key;
            else if ("".equals(_Result))
                return String.valueOf(getRol(msg.getContent()));
            else
                return _Result;
        }
        else if (msg.getPerformativeInt()==ACLMessage.CONFIRM)
            return "OK";
        else if (msg.getPerformativeInt()==ACLMessage.REFUSE){
            return "CANCEL";
        }
        else
            return "NotUnderstood";
    }
    
    
    
    
    public ACLMessage CAsendKey(String key, AgentID _sender, String[] _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="key";
        msg[0][1]=key;      
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        for (int cont=0; cont < _receiver.length; cont++){
            outbox.addReceiver(new AgentID(_receiver[cont]));
        }
        return outbox;             
    }

    public ACLMessage ACSendRol(AgentID _sender, String _receiver, String _rol) {
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="rol";
        msg[0][1]=_rol;      
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage ACDatos(AgentID _sender, String _receiver, String _msg) {
        ACLMessage outbox = new ACLMessage();      
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(_sender);
        String msg [][] = new String [1][2];
        msg[0][0]="result";
        msg[0][1]=_msg;   
        outbox.setContent(TransformarJSon(msg));
        outbox.addReceiver(new AgentID(_receiver));
        return outbox;
    }
    
    public ACLMessage CAsendOK(AgentID _sender, String[] _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="result";
        msg[0][1]="OK";      
        outbox.setPerformative(ACLMessage.CONFIRM);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        for (String _receiver1 : _receiver) {
            outbox.addReceiver(new AgentID(_receiver1));
        }
        return outbox;             
    }
    
    public ACLMessage CAsendPosicion(AgentID _sender, String[] _receiver, int pasos, Posicion p){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [2][2];
        msg[0][0]="result";
        msg[0][1]=String.valueOf(pasos);
        msg[1][0]="posicion";
        String posicion [][] = new String [2][2];
        posicion[0][0] = "x";
        posicion[0][1] = String.valueOf(p.getX());
        posicion[1][0] = "y";
        posicion[1][1] = String.valueOf(p.getY());
        msg[1][1]=TransformarJSon(posicion);
        outbox.setPerformative(ACLMessage.INFORM);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        for (String _receiver1 : _receiver) {
            outbox.addReceiver(new AgentID(_receiver1));
        }
        return outbox;             
    }
    
    
    public ACLMessage CAsendCANCEL(AgentID _sender, String[] _receiver){
        ACLMessage outbox = new ACLMessage();
        String msg [][] = new String [1][2];
        msg[0][0]="result";
        msg[0][1]="CANCEL";      
        outbox.setPerformative(ACLMessage.CANCEL);
        outbox.setSender(_sender);
        outbox.setContent(TransformarJSon(msg));
        for (String _receiver1 : _receiver) {
            outbox.addReceiver(new AgentID(_receiver1));
        }
        return outbox;             
    }
}
