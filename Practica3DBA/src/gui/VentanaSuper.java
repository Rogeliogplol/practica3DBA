package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La ventana que guarda y organiza los paneles de los drones y del controlador
 * 
 * @author Daniel
 */

public class VentanaSuper  extends JFrame{
    private final String nombreventana;
    private int numPaneles;
    private boolean controlAdded;
    private final JPanel dronesPanel, controlPanel;
    public static VentanaSuper laVentana = null;
    
    /**
    * Constructor de la clase VentanaSuper. Es privado debido a que es una clase
    * singleton.
    * 
    * @param nombreVentana Nombre de la ventana
    * @param mapa Matriz de casillas a imprimir
    * 
    * @author Daniel
    */
    
    private VentanaSuper (String nombreVentana) {
        this.nombreventana=nombreVentana;
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
    
    /**
    * Método carácterístico de las clases singleton para obtener la única
    * instancia existente
    * 
    * @return La instancia de VentanaSuper
    * 
    * @author Daniel
    */
    
    public static VentanaSuper getInstance () {
        if(laVentana == null) {
            laVentana = new VentanaSuper("Práctica 3");
        }
        
        return laVentana;
    }
    
    /**
    * Método para añadir un panel de DibujarMapa a ala ventana
    * 
    * @param panel El panel de DibujarMapa a añadir
    * 
    * @author Daniel
    */
    
    public void addPanel (DibujarMapa panel) {
        if (numPaneles != 4) {
            dronesPanel.add(panel);
            numPaneles++;
            
            checkPanelsAdded();
        }
    }
    
    /**
    * Método para añadir el panel de DibujarMapaControlador a ala ventana
    * 
    * @param panel El panel de DibujarMapa a añadir
    * 
    * @author Daniel
    */
    
    public void addPanel (DibujarMapaControlador panel) {
        if (!controlAdded) {
            controlAdded=true;
            controlPanel.add(panel);
            
            checkPanelsAdded();
        }
    }
    
    /**
    * Método para comprobar si todos los paneles han sido añadidos, y caso
    * afirmativo hace visible la ventana
    * 
    * @author Daniel
    */
    
    private void checkPanelsAdded () {
        if ((numPaneles == 4) && (controlAdded)) {
            pack();
            setVisible(true);
        }
    }
}
