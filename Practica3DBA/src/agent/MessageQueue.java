/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

/**
 *
 * @author SRJota
 */
import es.upv.dsic.gti_ia.core.ACLMessage;

/**
 *
 * @author SRJota
 */
import es.upv.dsic.gti_ia.core.ACLMessage;

// Cola de mensajes asíncrona
public class MessageQueue {
   private ACLMessage queue []; // Cola de mensajes
   private int size;            // Número de elementos en la cola
   private int dim;             // Tamaño máximo permitido para la cola
   private int head, tail;      // Cursores a los extremos de la cola
   
   
   public MessageQueue(int dimension)  {
       dim = dimension;
       queue = new ACLMessage [dim];
       size = 0;
       head = 0; 
       tail = dim-1;
   }
   
   // Devuelve true si la cola está vacía, false en otro caso
   public boolean isEmpty()  {
        return this.getSize()==0;
   }
   
   // Devuelve el número de elementos útiles en la cola
   public int getSize()  {
        return size;           
   }
   
   // Extrae el primer mensaje de la cola
   public ACLMessage Pop() throws InterruptedException {
       ACLMessage ret;
       synchronized (this)  {   // Cerrojo para acceso exclusivo
            if (this.getSize() == 0)
                throw new InterruptedException();
            else  {
                size --;
                ret = queue[head];
                head = (head +1) % dim;
            }
       }
       return ret;
   }
   
   // Pone un mensaje al final de la cola
   public void Push(ACLMessage msg) throws InterruptedException  {
       synchronized(this)  {        // Cerrojo para acceso exclusivo
            if (this.getSize() < dim)  {
                size ++;
                tail = (tail + 1) % dim;
                queue[tail] = msg;
            }
            else
                throw new InterruptedException();
       }
   }
}
