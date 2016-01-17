package gui;

import ia.CasillaControlador;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author SRJota Daniel Roger
 */

public class DibujarMapaControlador  extends JPanel{
    private CasillaControlador[][] mapa;
    private final int ancho;
    private final int alto;
    private final static int delay=500;
    private final String nombreVentana;
    private final int margenSuperior=5, margenLateral=5;
    private BufferedImage image;
    
    /**
    * Constructor de la clase DibujarMapa
    * @param _nombreventana Nombre del agente
    * @param mapa Matriz de casillas a imprimir
    * 
    * @author SRJota Daniel Roger
    * 
    */
    
    public DibujarMapaControlador (String nombreVentana, CasillaControlador[][] mapa) {
        ancho = mapa.length;
        alto = mapa[0].length;
        this.nombreVentana=nombreVentana;
        this.mapa=mapa;
        
        setLayout(null);
        setPreferredSize(new Dimension(612, 662));
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
    
    private Color getDronColor (int dron) {
        Color ret;
        
        switch (dron) {
            case 0:     ret = new Color (0,200,0);
                        break;
            case 1:     ret = new Color (0, 0,200);
                        break;
            case 2:     ret = new Color (200,0,200);
                        break;
            default:    ret = new Color (0,200,200);
                        break;
        }
        
        return ret;
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
    
    private Color ColorCelda (CasillaControlador valormapa){
        Color ret = new Color(0, 0, 0);
        int pasosDrones[]=valormapa.getDronVisitado();
        
        for (int i=0; i<4; i++) {
            int pasosDron=pasosDrones[i];
            if (valormapa.getRadar()==-1) {         // No hay muro
                ret = new Color(180,180,180);
            } else if (valormapa.getRadar()==0) {   // No hay muro
                if (pasosDron==0)                   // No visitado => Azul
                    ret = new Color(255,255,255);
                else {                              // Visitado => Verde
                    ret = getDronColor (i);
                }   
            } else if (valormapa.getRadar()==1) {   // Obstáculo
                if (pasosDron>0)                    // Choque => Amarillo
                    ret = new Color (235,255,50);
                else                                // Obstaculo => Negro
                    ret = new Color (0,0,0);
            } else {
                if (pasosDron==0)                   // Objetivo => Rojo
                    ret = new Color (255,0,0);
                else {                              // Visitado => Verde
                    ret = getDronColor (i);
                }
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
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24)); 
        g.drawRect(margenLateral, margenSuperior, 601, 601);
        g.drawRect(margenLateral, margenSuperior+610, 601, 41);
        g.drawString(nombreVentana, margenLateral+8, margenSuperior+640);
        g.drawImage(image, margenLateral+1, margenSuperior+1, 600, 600, null);
    }
}
