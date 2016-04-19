package gameoflife;

/**
 * Gameboard class for Game of Life. Contains variables relating to dimensions
 * of patterns and boards, as well as methods to populate boards, count
 * the neighbours of each cell, and call on the cell's method for 
 * changing its life status.
 */

public class Gameboard {
  
  /**
   * Empty constructor, no parameter input.
   */
  public Gameboard(){
  }
  
  /**
   * Number of columns on board
   */
  protected int boardCols = 10;
  
  /**
   * Number of rows on board
   */
  protected int boardRows = 10;
  
  /**
   * Number of columns on pattern
   */
  protected int patCols;
  
  /**
   * Number of rows on pattern
   */
  protected int patRows;
  
  /**
   * Array containing cell objects for board
   */
  protected Cell[][] board;
  
  /**
   * Array containing cell objects for board
   */
  protected Cell[][] pattern;
  
  /**
   * Receives a pattern as byte[][] array, and converts it to an object
   * oriented Cell[][] array, and sets life status of cells according to the
   * values in the input byte[][] array.
   * @param tab  Simple input array with 1's and 0's
   */
  protected void setPattern(byte[][] tab) {
    patCols = tab.length;
    patRows = tab[0].length;
    
    pattern = new Cell[patCols][patRows];
            
    for (int i=0; i<patCols; i++) {
      for (int j=0; j<patRows; j++) {
        pattern[i][j] = new Cell();
        if (tab[i][j] == 1) {
          pattern[i][j].alive = true;
        } else {
          pattern[i][j].alive = false;
        }
      }
    }
  }
  
  /**
   * Method to quickly set dimenions of board, if it's necessary to change them.
   * @param c  Number of columns in new board dimensions
   * @param r  Number of rows in new board dimensions
   */
  protected void setBoardDim(int c, int r) {
    boardCols = c;
    boardRows = r;
  }
  
  
  /**
   * Places the cells in the 'pattern' array, in the 'board' array,
   * and places it near the center.
   */
  protected void populateBoard() {
    board = new Cell[boardCols][boardRows];
    for (int i=0; i<boardCols; i++) {
      for (int j=0; j<boardRows; j++) {
        board[i][j] = new Cell();
      }
    }
    for (int i=0; i<patCols; i++) {
      for (int j=0; j<patRows; j++) {        
        if (pattern[i][j].alive == true) {
          board[i+boardCols/2-patCols/2][j+boardRows/2-patRows/2].alive = true;
        }
      }
    }
  }  
  
  int totNeighbours;
  /**
   * Method for counting the neighbours of each cell. Because neighbours are
   * counted with a simple for-loop, the center cell (the cell whose
   * neighbours are being counted) is later subtracted from the count,
   * if it is alive (and as such has been counted as its own neighbour).
   * The modulus operator is used when counting neighbours, simplifying
   * the process of counting, by "folding" the board, thus being able to 
   * count all cells the same way, regardless of their placement.
   */  
  public void countNeighbours() {
    totNeighbours = 0;
    for (int i=0; i<boardCols;i++) {
      for (int j=0; j<boardRows;j++) {
        board[i][j].neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {
            if (board[(i+m+boardCols)%boardCols][(j+n+boardRows)%boardRows].alive == true) {
              board[i][j].neighbours++;
            }
          }
        }
        if (board[i][j].alive == true) {
          //Remove center cell from count, if alive
          board[i][j].neighbours--;
          totNeighbours += board[i][j].neighbours;
        }
      }
    }
  }
  /**
   * Method to run Cell.changeLife() on all Cells in the board array.
   */
  public void setLife() {
    for (int i=0; i<boardCols; i++) {
      for (int j=0; j<boardRows; j++) {
        board[i][j].changeLife(); 
      }
    }
  }
  /**
   * Simple method to run both countNeighbours() and setLife() at once.
   */
  public void nextGeneration() {
   countNeighbours();
   setLife();
  }
}
