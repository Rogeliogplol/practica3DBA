package gui;

import ia.Casilla;
import ia.Posicion;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Panel de información de un dron individual
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
    private final Posicion pos;
    private Posicion objetivo=null;
    private final String tipoDron;
    
    /**
    * Constructor de la clase DibujarMapa
    * @param mapa Matriz de casillas a imprimir
    * @param nombreDron El nombre del dron
    * @param bateria La batería inicial del dron
    * @param pos La referencia a la posición del dron
    * @param tipoDron El tipo del dron (Mosca, Pájaro o Halcón)
    * 
    * @author Daniel SRJota Roger
    */
    
    public DibujarMapa (Casilla[][] mapa, String nombreDron, int bateria, 
            Posicion pos, String tipoDron) {
        this.tipoDron=tipoDron;
        this.pos=pos;
        this.nombreDron=nombreDron;
        this.bateria=bateria;
        this.mapa=mapa;
        ancho = mapa.length;
        alto = mapa[0].length;
        setLayout(null);
        setPreferredSize(new Dimension(312, 344));
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
    
    /**
    * Actualiza el objetivo del dron a mostrar
    * 
    * @param objetivo La nueva posición objetivo del dron a mostrar
    * 
    * @author Daniel
    */
    
    public void setObjetivo (Posicion objetivo) {
        this.objetivo=objetivo;
    }
    
    /**
    * Actualiza la batería del dron a mostrar
    * 
    * @param bateria La nueva batería del dron a mostrar
    * 
    * @author Daniel
    */
    
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
    
    private Color ColorCelda (Casilla valorMapa){
        Color ret;
        
        if (valorMapa.getPasos()!=0) {          // El dron lo ha visitado
            if (valorMapa.getRadar()==1)        // Choque -> Amarillo
                ret = new Color (235,255,50);
            else {                              // Visitado -> Verde
                ret = new Color (0,200,0);
            }
        } else {
            if (valorMapa.getRadar()==-1)       // No Visitado -> Gris
                ret = new Color(170,170,180);
            else if (valorMapa.getRadar()==0)   // Visitado -> Blanco
                ret = new Color(255,255,255);
            else if (valorMapa.getRadar()==1)   // Muro -> Negro
                ret = new Color (0,0,0);
            else                                // Objetivo -> Rojo
                ret = new Color (255,0,0);
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
        g.drawImage(image, margenLateral+1, margenSuperior+1, 300, 300, null);
        g.drawRect(margenLateral, margenSuperior, 301, 301);
        g.drawRect(margenLateral, margenSuperior+302, 301, 34);
        g.setFont(new Font("TimesRoman", Font.BOLD, 16));
        g.drawString(tipoDron+": "+nombreDron, margenLateral+6, margenSuperior+318);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        g.drawString("Batería: "+bateria, margenLateral+6, margenSuperior+332);
        g.drawString("Posición: ("+pos.getX()+", "+pos.getY()+")", margenLateral+190, margenSuperior+316);
        if (objetivo!=null)
            g.drawString("Objetivo:  ("+objetivo.getX()+", "+objetivo.getY()+")", margenLateral+190, margenSuperior+332);
        else
            g.drawString("Objetivo:  -------", margenLateral+190, margenSuperior+332);
    }
}