package gui;

import ia.Casilla;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;

/**
 *
 * @author SRJota Daniel Roger
 */

public class DibujarMapa  extends JPanel{
    private final Casilla[][] mapa;
    private final int ancho;
    private final int alto;
    private final static int delay=500;
    private final int margenSuperior=5, margenLateral=5;
    private BufferedImage image;
    private final String nombreDron;
    private int bateria;
    
    /**
    * Constructor de la clase DibujarMapa
    * @param _nombreventana Nombre del agente
    * @param mapa Matriz de casillas a imprimir
    * 
    * @author SRJota Daniel Roger
    * 
    */
    
    public DibujarMapa (String _nombreventana, Casilla[][] mapa, String nombreDron, int bateria) {
        this.nombreDron=nombreDron;
        this.bateria=bateria;
        this.mapa=mapa;
        ancho = mapa.length;
        alto = mapa[0].length;
        setLayout(null);
        setPreferredSize(new Dimension(312, 347));
        setVisible(true);

        // Cada "delay" milisegundos se repintará el mapa
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadMapImage();
                repaint();
            }
        };

        new Timer(delay, taskPerformer).start();
    }
    
    public void setBateria (int bateria) {
        this.bateria=bateria;
    }
    
    /**
    * Determina el color del cuadrado a pintar según los valores de la casilla
    * correspondiente en el mapa
    * @param valormapa Casilla a analizar
    * @return Devuelve el color a pintar
    * 
    * @author SRJota Daniel Roger
    * 
    */
    private Color ColorCelda (Casilla valormapa){
        Color ret;
        
        if (valormapa.getRadar()==-1) {         // No se ha visto
            ret = new Color(170,170,180);
        } else if (valormapa.getRadar()==0) {   // No hay muro
            if (valormapa.getPasos()==0)        // No visitado => Azul
                ret = new Color(255,255,255);
            else {                              // Visitado => Verde
                ret = new Color (0,200,0);
            }
        } else if (valormapa.getRadar()==1) {   // Obstáculo
            if (valormapa.getPasos()>0)         // Choque => Amarillo
                ret = new Color (235,255,50);
            else                                // Obstaculo => Negro
                ret = new Color (0,0,0);
        } else {
            if (valormapa.getPasos()==0)        // Objetivo => Rojo
                ret = new Color (255,0,0);
            else {                              // Visitado => Verde
                ret = new Color (0,200,0);
            }
        }
        
        return ret;
    }
    
    /**
    * Carga la imagen según el mapa actual
    * 
    * @author Daniel Roger
    * 
    */
    private void loadMapImage () {
        int type = BufferedImage.TYPE_INT_RGB;

        image = new BufferedImage(ancho, alto, type);

        for (int i=0; i <ancho; i++){
            for (int j=0; j<alto; j++){
                Color col = ColorCelda(mapa[i][j]);
                image.setRGB(i, j, col.getRGB());
            }
        }
    }
    
    /**
    * Pinta el mapa cargado
    * 
    * @param g
    * @author Daniel SRJota Roger
    * 
    */
    @Override
    public void paint (Graphics g){
        super.paint(g);
        g.drawRect(margenLateral, margenSuperior, 301, 301);
        g.drawRect(margenLateral, margenSuperior+310, 301, 21);
        g.drawString("Dron: "+nombreDron, margenLateral+4, margenSuperior+326);
        g.drawString("Batería: "+bateria, margenLateral+231, margenSuperior+326);
        g.drawImage(image, margenLateral+1, margenSuperior+1, 300, 300, null);
    }
}
