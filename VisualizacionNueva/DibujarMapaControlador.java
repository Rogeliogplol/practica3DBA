package practica2dba;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author SRJota Daniel Roger
 */

class DibujarMapaControlador  extends JFrame{
    private static CasillaControlador[][] mapa;
    private final static int ancho=504;
    private final static int alto=504;
    private final static int delay=200;
    private final String nombreventana;
    private final JPanel contentPane;
    private final int margenSuperior=35, margenLateral=10;
    private BufferedImage image;
    
    /**
    * Constructor de la clase DibujarMapa
    * @param _nombreventana Nombre del agente
    * @param mapa Matriz de casillas a imprimir
    * 
    * @author SRJota Daniel Roger
    * 
    */
    
    public DibujarMapaControlador (String _nombreventana, CasillaControlador[][] mapa) {
        nombreventana=_nombreventana;
        DibujarMapaControlador.mapa=mapa;
        
        // Inicializa la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        setTitle(nombreventana);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        setBounds(0,0,520,600);
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
                    switch (i) {
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
            } else if (valormapa.getRadar()==1) {   // Obstáculo
                if (pasosDron>0)                    // Choque => Amarillo
                    ret = new Color (235,255,50);
                else                                // Obstaculo => Negro
                    ret = new Color (0,0,0);
            } else
                ret = new Color (255,0,0);          // Objetivo => Rojo
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
    * @author Daniel SRJota Roger
    * 
    */
    @Override
    public void paint (Graphics g){
        super.paint(g);
        g.drawImage(image, margenLateral, margenSuperior, 500, 500, null);
    }
}
