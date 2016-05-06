package gameoflife;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Christine
 */
public class GoLControllerEditor implements Initializable {

    @FXML
    Canvas canvas2 = new Canvas(720,480);
    private GraphicsContext gc2;
    GameboardDynamic gb2; // = new GameboardDynamic();
    float cellsize;
    protected ArrayList<ArrayList<Cell>> board2Dyn;

    private GoLController gol;
    
    /**
     * Sets controller and copies life values from input ArrayList
     * @param gol Controller object
     * @param gbd Gameboard object
     */
    void setGolController(GoLController gol, GameboardDynamic gbd) {
        this.gol = gol;
        for (int i=0; i<gbd.board.size(); i++) {
            for (int j=0; j<gbd.board.get(i).size(); j++) {
                if (gbd.board.get(i).get(j).alive == true) {
                    gb2.board.get(i).get(j).alive = true;
                } else {
                    gb2.board.get(i).get(j).alive = false;
                }
            }
        }
    }
    
    /**
     * Copies reference from input ArrayList
     * @param b ArrayList with cell information
     */
    public void setBoard(ArrayList<ArrayList<Cell>> b){
        gb2.setBoard(b);
    }
    
    /**
     * Initializes the controller class for the editor.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       gc2 = canvas2.getGraphicsContext2D();
       gb2 = new GameboardDynamic();
       gb2.initialiseBoard();
    }
    
    /**
     * Sets board, draws initial configuration in canvas, and calls method
     * for editing cells
     * @param b Input ArrayList
     */
    public void setEditorController(ArrayList<ArrayList<Cell>> b){
       gb2.board = b;
       board2Dyn = copyBoardDyn(gb2.board);
       drawEditor();
       paintEditor();
    }
      
    
     public ArrayList<ArrayList<Cell>> copyBoardDyn(ArrayList<ArrayList<Cell>> input) {
      ArrayList<ArrayList<Cell>> newBoardDyn = new ArrayList<ArrayList<Cell>>();
      for (int i=0; i<gb2.boardCols; i++) {
        newBoardDyn.add(new ArrayList<Cell>());
      for (int j=0; j<gb2.boardRows; j++) {
          newBoardDyn.get(i).add(new Cell());
      }
     }
      for (int i=0; i<gb2.boardCols; i++) { 
      for (int j=0; j<gb2.boardRows; j++) { 
      if(input.get(i).get(j).alive){ //gb2.boardDyn -> input
              newBoardDyn.get(i).get(j).alive = true;
      }
    }
   }
      return newBoardDyn;
  }
    /**
     * Method to draw initial configuration in editor canvas
     */
    private void drawEditor() {
      cellsize = (float) canvas2.getHeight()/gb2.boardRows;
      gc2 = canvas2.getGraphicsContext2D();
      gc2.setFill(Color.WHITE);
      gc2.fillRect(0,0,canvas2.getWidth(),canvas2.getHeight());
    
      for (int i=0; i<gb2.boardCols;i++) {
        for (int j=0; j<gb2.boardRows;j++) {
          if (board2Dyn.get(i).get(j).alive == true) {
            System.out.println("celle");
            gc2.setFill(Color.DARKSLATEGREY);
            gc2.fillRect(i*cellsize,j*cellsize,cellsize,cellsize);
          }
        }
      }
    }
    
        private void paintEditor(){ 
         canvas2.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent e) -> {
             double xCo = e.getX();
             double yCo = e.getY();
             int ixCo = (int)xCo/(int)cellsize;
             int iyCo = (int)yCo/(int)cellsize;
             
            if(board2Dyn.get(ixCo).get(iyCo).alive == true) {
                board2Dyn.get(ixCo).get(iyCo).alive = false;
                gc2.setFill(Color.WHITE);
                gc2.fillRect(ixCo*cellsize,iyCo*cellsize,cellsize,cellsize);
                
            } else {
                board2Dyn.get(ixCo).get(iyCo).alive = true;
                gc2.setFill(Color.DARKSLATEGREY);
                gc2.fillRect(ixCo*cellsize,iyCo*cellsize,cellsize,cellsize);
         }
          System.out.println("X: "+ ixCo + ", Y: " + iyCo);
        });
    }
        
    /**
     * Method to copy input board
     * @param tab Input Cell array
     * @return Copy of input array
     */    
    public Cell[][] copyBoard(Cell[][] tab) {
      Cell[][] newBoard = new Cell[tab.length][];
      for (int i=0; i <tab.length; i++) {
        newBoard[i] = Arrays.copyOf(tab[i], tab[i].length);
      }
      return newBoard;
}     
        
       
    
    
    
}
