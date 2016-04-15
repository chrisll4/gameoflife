package gameoflife;

public class Gameboard {
  
  public Gameboard(){
    
  }
  
  protected int boardCols = 120;
  protected int boardRows = 80;
  
  protected int patCols;
  protected int patRows;
  
  protected Cell[][] board = new Cell[boardCols][boardRows];
  
  protected Cell[][] pattern;
  
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
  
  protected void setBoardDim(int c, int r) {
    boardCols = c;
    boardRows = r;
  }
  
  
  
  protected void populateBoard() {
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
  
  public void countNeighbours() {
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
          board[i][j].neighbours --;
        }
      }
    }
  }
  
  public void setLife() {
    for (int i=0; i<boardCols; i++) {
      for (int j=0; j<boardRows; j++) {
        board[i][j].changeLife(); 
      }
    }
  }
  
  public void nextGeneration() {
   countNeighbours();
   setLife();
  }
}
