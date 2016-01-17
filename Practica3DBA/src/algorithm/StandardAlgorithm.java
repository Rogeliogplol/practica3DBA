    /*
     * TODO: 
     *
     * - Refactoring node selection to improve code (DRY)
     *
     * - Refactoring class using getter/setter instead direct access to
     *   class atributes. 
     *
     * - Improve memory usage deleting unnecessary vars. 
     */

package algorithm;

import java.util.ArrayList;

/** 
 * Clase que implementa el algoritmo HillClimbing en su variante de Ascenso Pronunciado.
 * 
 * @author Francisco Sueza Rodriguez
 */

public class StandardAlgorithm{
    
    private final int xStartNode = 2;      // Coordenada X de la casilla de inicio
    private final int yStartNode = 2;      // Coordenada Y de la casilla de inicio
    private final int MatrixDetected  = 5; // Tamaño de matriz de percepcion
    private final int MatrixNeighbors = 3; // tamaño de matriz de vecinos
    private final int MaxSteps = 500*500;    // Numero de pasos para considerar el mapa sin solucion
    
    private final String[][] directions = {{"NW","N","NE"},{"W","GOAL","E"},{"SW","S","SE"}};   
    
    private Casilla startNode;          
    private Casilla[][] detectedNodes;  
    private Casilla[][] neighborsNodes;
    private AlgorithSeguirPared algoritmoPared;
    
    private int pasos;
    private boolean ObstacleMode;
    
    
    
    
    public StandardAlgorithm(){
        this.startNode = new Casilla();
        this.detectedNodes = new Casilla[MatrixDetected][MatrixDetected];
        this.neighborsNodes = new Casilla[MatrixNeighbors][MatrixNeighbors];
        this.pasos = 0;
        this.ObstacleMode = false;
        this.algoritmoPared = new AlgorithSeguirPared();
    }
    
    /**
     * Procesa la percepcion del agente como un conjunto de nodos y devuelve
     * el nodo mas prometedor como resultado.
     *  
     * @param sensor Matriz de tipo Casilla que representa la percepcion
     * @return Devuelve una cadena con la direccion selecionada 
     * @throws Exception Excepcion en caso de que la matriz sea nula o este formada incorrectamente
     *     
     * @author Francisco Sueza Rodriguez
     */
    
    public String process(Casilla[][] sensor) throws Exception {
        if ((sensor == null) || (sensor.length != 5))        
            throw new NullPointerException("Matriz de percepcion no valida!!");
        
        this.setDetectedNodes(sensor);
        this.setStartNode();
        this.extractNeighborsNodes();
        this.onObstacle(neighborsNodes);
   
        if (this.ObstacleMode)
            return this.algoritmoPared.process(sensor);
        else
            return this.calculateBestNode();
    }
    
    /**
     * Determina si nos encontramos delante de un obstaculo o no.
     * 
     * @param neighbors Matriz con los nodos vecinos  
     */
    
    private void onObstacle(Casilla[][] neighbors) {
        int obstacleCont = 0;
        
        for (int i=0; i<neighbors.length; i++)
            for (int j=0; j<neighbors[0].length; j++)
                if (neighbors[i][j].getRadar() == 1)
                    obstacleCont++;
        
        if (obstacleCont >= 2)
            this.ObstacleMode = true;
        else
            this.ObstacleMode = false;
    }
    /**
     * Copia a una variable interna (detectedNodes) el conjunto de nodos 
     * percibidos para trabajar con ellos
     * 
     * @param sensor Matriz con la percepcion de los sensores
     * 
     * @author Francisco Sueza Rodriguez
     */
    
    private void setDetectedNodes(Casilla[][] sensor) {
        for (int i = 0; i < 5; i++)
            System.arraycopy(sensor[i], 0, this.detectedNodes[i], 0, 5);        
    }
    
    /**
     * Extrae los nodos vecinos (neighborsNodes) del nodo de inicio desde el atributo
     * detectedNodes que es el conjunto de nodos que representa la percepcion
     * 
     * @author Francisco Sueza Rodriguez
     */
    
    private void extractNeighborsNodes(){
        for (int i = xStartNode-1; i <= xStartNode+1; i++)
            for (int j = yStartNode-1; j <= yStartNode+1; j++)
                this.neighborsNodes[i-1][j-1] = this.detectedNodes[i][j];
    }
    
      /**
     * Establece el nodo de inicio (startNode) a partir del atributo detectedNodes 
     * de la instancia
     * 
     * @author Francisco Sueza Rodriguez
     */
    
    private void setStartNode(){
        this.startNode = this.detectedNodes[xStartNode][yStartNode];
    }
    
    /**
     * Metodo que devuelve la direccion a la que debera moverse el bot 
     * segun la posicion del nodo en la matriz.
     * 
     * @return String Con la direccion en la que se encuentra el nodo
     * 
     * @author Francisco Sueza Rodriguez
     */
    
    private String selectDirection(Casilla node){
        
        for (int i = 0; i < this.neighborsNodes.length; i++)
            for (int j = 0; j < this.neighborsNodes.length; j++)
                if (this.neighborsNodes[i][j] == node){
                    return this.directions[i][j];
                }
        
        return "Fail";
    }
      
    /**
     * Aplica el algoritmo de busqueda HillClimbing con ascenso pronunciado 
     * sobre el conjunto de nodos vecinos determinando la direccion en la que 
     * debe moverse el bot.
     * 
     * @author Francisco Sueza Rodriguez
     */
    
    private String calculateBestNode(){ 
        ArrayList<Casilla> visited = new ArrayList();
        ArrayList<Casilla> notVisited = new ArrayList();
        
        Casilla tempBestNode = new Casilla();
        
        // Separamos los nodos en visitados y no visitados. 
        
        for (int i = 0; i < this.neighborsNodes.length; i++)
            for (int j = 0; j < this.neighborsNodes.length; j++)
                if (this.neighborsNodes[i][j].getRadar() == 2)
                    return this.directions[xStartNode-1][yStartNode-1];
                else {
                    if((this.neighborsNodes[i][j].getRadar() == 0)
                            && (this.neighborsNodes[i][j] != startNode)) 
                        if (this.neighborsNodes[i][j].getPasos() != 0)
                            visited.add(this.neighborsNodes[i][j]);
                        else 
                            notVisited.add(this.neighborsNodes[i][j]);
                }
        
        /*
         * Si hay nodos sin visitar, selecionamos la solucion de uno de estos.
         * Sino, la seleccionamos de los nodos visitados.
         */

        if (notVisited.isEmpty()){
            tempBestNode = visited.get(0);            
            for (Casilla tmp: visited)
                if (tempBestNode.getPasos() > tmp.getPasos())
                    tempBestNode = tmp;
        }
        else {
            tempBestNode = notVisited.get(0);        
            for (Casilla tmp: notVisited)
                if (tempBestNode.getScanner() > tmp.getScanner())
                    tempBestNode = tmp;            
        }
        
        /*
         * Comprobamos que no se sobrepase el limite de pasos        
         */
        
        if (pasos == MaxSteps)
            return "IMPOSIBLE";
        else {
            pasos++;
            return this.selectDirection(tempBestNode);        
        }
    }
}