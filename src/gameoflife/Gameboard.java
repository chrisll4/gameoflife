package gameoflife;

/**
 * Gameboard class for Game of Life. Contains variables relating to dimensions
 * of patterns and boards, as well as methods to populate boards, count
 * the neighbours of each cell, and call on the cell's method for 
 * changing its life status.
 * @author Fredrik
 */

public class Gameboard {
  
  /**
   * Number of columns on board
   */
  protected int boardCols = 45;
  
  /**
   * Number of rows on board
   */
  protected int boardRows = 30;
  
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
  protected Cell[][] patOrigin;
  
  /**
   * Receives a pattern as byte[][] array, and converts it to an object
   * oriented Cell[][] array, and sets life status of cells according to the
   * values in the input byte[][] array.
   * @param tab  Simple input array with 1's and 0's
   */
  protected void setPattern(byte[][] tab) {
    //set pattern columns and rows to match parameter array
    patCols = tab.length;
    patRows = tab[0].length;
    
    //Initialise arrays
    pattern = new Cell[patCols][patRows];
    patOrigin = new Cell[patCols][patRows];
    
    /*
    Hacky solution for "dynamic" board, that checks if the size of the pattern
    is larger than the size of the board, and if so, enlarges the board
    so the pattern fits inside
    */
    if (pattern.length > boardCols || pattern[0].length > boardRows) {
      if (pattern.length > pattern[0].length) {
        boardCols = (int) (pattern.length * 1.2);
        boardRows = (int) ((2*boardCols) / 3);
      } else if (pattern[0].length >= pattern.length) {
        boardRows = (int) (1.2 * pattern[0].length);
        boardCols = (int) (1.5 * boardRows);
      }
    }
    
    for (int i=0; i<patCols; i++) {
      for (int j=0; j<patRows; j++) {
        pattern[i][j] = new Cell();
        patOrigin[i][j] = new Cell();
        if (tab[i][j] == 1) {
          pattern[i][j].alive = true;
          patOrigin[i][j].alive = true;
        } else {
          pattern[i][j].alive = false;
          patOrigin[i][j].alive = false;
        }
      }
    }
  }
  /**
   * Receives a pattern as Cell[][] array, and copies it to another
   * Cell[][] array, and sets life status of cells according to the
   * values in the input Cell[][] array. Intended for copying intermediate 
   * board patterns without first converting to byte-array and then to
   * Cell-array
   * @param tab  Simple input array with 1's and 0's
   */
  protected void setPattern(Cell[][] tab) {
    patCols = tab.length;
    patRows = tab[0].length;
    
    pattern = new Cell[patCols][patRows];
    
    if (pattern.length > boardCols || pattern[0].length > boardRows) {
      if (pattern.length > pattern[0].length) {
        boardCols = (int) (pattern.length * 1.2);
        boardRows = (int) ((2*boardCols) / 3);
      } else if (pattern[0].length >= pattern.length) {
        boardRows = (int) (1.2 * pattern[0].length);
        boardCols = (int) (1.5 * boardRows);
      }
    }
    
    for (int i=0; i<patCols; i++) {
      for (int j=0; j<patRows; j++) {
        pattern[i][j] = new Cell();
        if (tab[i][j].alive == true) {
          pattern[i][j].alive = true;
        } else {
          pattern[i][j].alive = false;
        }
      }
    }
  }
  
  
  /**
   * Method to quickly set dimensions of board, if it's necessary to change them.
   * @param c  Number of columns in new board dimensions
   * @param r  Number of rows in new board dimensions
   * @author Fredrik
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
    /*
    Initialise board array, and initialise Cell objects within it
    */
    board = new Cell[boardCols][boardRows];
    
    for (int i=0; i<boardCols; i++) {
      for (int j=0; j<boardRows; j++) {
        board[i][j] = new Cell();
      }
    }
    
    /*
    Place Cells from pattern into board, centered by "boardCols/2-patCols/2".
    */
    float ratio = patRows/boardRows;
    
    if (ratio>1) {
      for (int i=0; i<(int)(patCols*ratio); i++) {
        for (int j=0; j<(int)(patRows*ratio); j++) {        
          if (pattern[i][j].alive == true) {
            board[i+boardCols/2-(int)(patCols*ratio)/2][j+boardRows/2-(int)(patRows*ratio)/2].alive = true;
          }
        }
      }
    } else {
      for (int i=0; i<patCols; i++) {
        for (int j=0; j<patRows; j++) {        
          if (pattern[i][j].alive == true) {
            board[i+boardCols/2-patCols/2][j+boardRows/2-patRows/2].alive = true;
          }
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
