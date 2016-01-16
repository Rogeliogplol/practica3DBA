/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

/**
 *
 * @author SRJota
 */
public class IAAgentes {
    Posicion objetivo;
    public IAAgentes(){
        objetivo=new Posicion(-1,-1);
    }
    
    public void SetObjetivo(Posicion _objetivo){
        objetivo = _objetivo;
    }
    
    public Posicion GetObjetivo (){
        return objetivo;
    }
    
    public float GetRadar(int X, int Y){
        return (float) Math.sqrt((X*X)+(Y*Y));
    }
    
    
}
