package gameoflife;

import java.util.ArrayList;

/**
 * Game board class containing information
 * @author Fredrik, made dynamic by Christine
 */
public class GameboardDynamic {
  //Constructor
  public GameboardDynamic() {
    this.initialiseBoard();
  }
  
  //Board dimenions
  protected int boardCols = 45;
  protected int boardRows = 30;
  
  //Pattern dimensions
  protected int patCols;
  protected int patRows;
 
  
  //Declare ArrayLists
  protected ArrayList<ArrayList<Cell>> pattern;
  protected ArrayList<ArrayList<Cell>> patOrigin;
  protected ArrayList<ArrayList<Cell>> board;
  
  /**
   * Receives a pattern as byte[][] array, and converts it to an object
   * oriented Cell[][] array, and sets life status of cells according to the
   * values in the input byte[][] array.
   * @param tab  Simple input array with 1's and 0's
   */
  protected void setPattern(byte[][] tab) {
    //set pattern dimensions to match parameter input array
    patCols = tab.length;
    patRows = tab[0].length;

    //Initialise ArrayLists
    pattern = new ArrayList<>();
    patOrigin = new ArrayList<>();
    
    //Initialise ArrayLists and Cell objects, copy life values        
    for (int i=0; i<patCols; i++) {
      pattern.add(new ArrayList<>());
      patOrigin.add(new ArrayList<>());
      for (int j=0; j<patRows; j++) {
        pattern.get(i).add(new Cell());
        patOrigin.get(i).add(new Cell());
        if (tab[i][j] == 1) {
          pattern.get(i).get(j).alive = true;
          patOrigin.get(i).get(j).alive = true;
        } else {
          pattern.get(i).get(j).alive = false;
          patOrigin.get(i).get(j).alive = false;
        }
      }
    }
  }
  /**
   * Method intended to receive intermediate board patterns to keep pattern
   * while changing grid size, etc.
   * @param tab Board ArrayList to be copied
   */
  protected void setPattern(ArrayList<ArrayList<Cell>> tab) {
    patCols = tab.size();
    patRows = tab.get(0).size();

    pattern = new ArrayList<>();
            
    for (int i=0; i<patCols; i++) {
      pattern.add(new ArrayList<>());
      for (int j=0; j<patRows; j++) {
        pattern.get(i).add(new Cell());
        if (tab.get(i).get(j).alive == true) {
          pattern.get(i).get(j).alive = true;
        } else {
          pattern.get(i).get(j).alive = false;
        }
      }
    }
  }
  
  /**
   * Simple method to set dimensions of board
   * @param c Board columns
   * @param r Board rows
   */
  protected void setBoardDim(int c, int r) {
    boardCols = c;
    boardRows = r;
  } 
  
  /**
   * Copies board reference
   * @param b Input ArrayList
   */
  public void setBoard(ArrayList<ArrayList<Cell>> b){
    this.board = b;
  }
  
  /**
   * Method to initialise the board ArrayList
   */
  protected void initialiseBoard() {
    board = new ArrayList<>();
      for (int i=0; i<boardCols; i++) {
        board.add(new ArrayList<>());
        for (int j=0; j<boardRows; j++) {
          board.get(i).add(new Cell());
      }
    }
  }
  
  /**
   * Method to populate board ArrayList with cells. Starts with clause that
   * removes cells that would be out of bounds because the pattern is larger
   * than the board, so grid can be resized without errors.
   */
  protected void populateBoard(){
    
    if (pattern.get(0).size() > board.get(0).size()) {
      int rowDiff = pattern.get(0).size() - board.get(0).size();
      int colDiff = pattern.size() - board.size();
      
      for (int i=0; i<colDiff; i++) {
        int tick = 0;
        if (i%2==0) {
          pattern.remove(tick);
        } else {
          pattern.remove(pattern.size()-1-tick);
          tick++;
        }
      }
      for (int i=0; i<pattern.size(); i++) {
        for (int j=0; j<rowDiff; j++) {
          int tick = 0;
          if (j%2==0) {
            pattern.get(i).remove(tick);
          } else {
            pattern.get(i).remove(pattern.get(i).size()-1-j);
            tick++;
          }
        }
      }
    }
    
    for (int i=0; i<pattern.size(); i++) {    
      for (int j=0; j<pattern.get(i).size(); j++) {
        int x = i+boardCols/2-pattern.size()/2;
        int y = j+boardRows/2-pattern.get(i).size()/2;
        if(pattern.get(i).get(j).alive){
          board.get(x).get(y).alive = true;
        }
      }
    }
  }
 
  /**
   * Method to count the number of neighbours for each cell
   */
  public void countNeighbours(){
    for (int i=0; i<boardCols;i++) {
      for (int j=0; j<boardRows;j++) {
        board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {  
            int xc = (i+m+boardCols)%boardCols;
            int yc = (j+n+boardRows)%boardRows;
            if(board.get(xc).get(yc).alive == true){
              board.get(i).get(j).neighbours++;
            }
          }   
        }
        if (board.get(i).get(j).alive == true) {
          //Remove center cell from count, if alive
          board.get(i).get(j).neighbours--;
        }
      } 
    }
  }
  
  /**
   * Method to change life status of each cell, calls the changeLife() method
   * from the Cell object
   */
  public void setLife() {
    for (int i=0; i<boardCols; i++) {
      for (int j=0; j<boardRows; j++) {
        board.get(i).get(j).changeLife(); 
      }
    }
  }
  
  /**
   * Method to calculate next generation of cells by counting neighbours and
   * changing the cells' life status
   */
  public void nextGeneration() {
   countNeighbours();
   setLife();
  } 
}
