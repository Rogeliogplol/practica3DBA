package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author SRJota Daniel Roger
 */

public class VentanaSuper  extends JFrame{
    private final String nombreventana;
    private int numPaneles;
    private boolean controlAdded;
    private JPanel dronesPanel, controlPanel;
    public static VentanaSuper laVentana = null;
    
    /**
    * Constructor de la clase DibujarMapa
    * @param _nombreventana Nombre del agente
    * @param mapa Matriz de casillas a imprimir
    * 
    * @author SRJota Daniel Roger
    * 
    */
    
    private VentanaSuper (String _nombreventana) {
        nombreventana=_nombreventana;
        numPaneles=0;
        
        dronesPanel = new JPanel ();
        dronesPanel.setLayout(new GridLayout(0,2, 0, 0));
        dronesPanel.setVisible(true);
        
        controlPanel = new JPanel ();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controlPanel.setVisible(true);
        
        // Inicializa la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(nombreventana);
        setLayout(new GridLayout(0,2, 0, 0));
        add(dronesPanel);
        add(controlPanel);
    }
    
    public static VentanaSuper getInstance () {
        if(laVentana == null) {
            laVentana = new VentanaSuper("Práctica 3");
        }
        
        return laVentana;
    }
    
    public void addPanel (DibujarMapa panel) {
        if (numPaneles != 4) {
            dronesPanel.add(panel);
            numPaneles++;
            
            checkPanelsAdded();
        }
    }
    
    public void addPanel (DibujarMapaControlador panel) {
        if (!controlAdded) {
            controlAdded=true;
            controlPanel.add(panel);
            
            checkPanelsAdded();
        }
    }
    
    private void checkPanelsAdded () {
        if ((numPaneles == 4) && (controlAdded)) {
            pack();
            setVisible(true);
        }
    }
}
