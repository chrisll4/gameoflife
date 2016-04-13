package gameoflife;

public class Gameboard {
  protected static int boardCols = 45;
  protected static int boardRows = 30;
  
  protected static int glidCols = GoL.glider.length;
  protected static int glidRows = GoL.glider[0].length;
  
  protected static byte[][] board = new byte[boardCols][boardRows];
  
  protected byte[][] makeBoard(byte[][] tab) {
    boardCols = tab.length;
    boardRows = tab[0].length;
    return null;
  }
  
  protected static void populateBoard() {
    for (int i=0; i<glidCols; i++) {
      for (int j=0; j<glidRows; j++) {
        if (GoL.glider[j][i] == 1) {
          board[i+boardCols/2-glidCols/2][j+boardRows/2-glidRows/2] = 1;
        } else {
          board[i+boardCols/2-glidCols/2][j+boardRows/2-glidRows/2] = 0;
        }
      }
    }
  }
  
  
  
  
  protected static int patternCols = board.length;
  protected static int patternRows = board[0].length;
  
  
  
  public static void nextGeneration() {
    for (int i=0; i<patternCols;i++) {
      for (int j=0; j<patternRows;j++) {
        if (board[i][j] == 0) {
          board[i][j] = 1;
        } else {
          board[i][j] = 0;
        }
      }
    }
  };
}
