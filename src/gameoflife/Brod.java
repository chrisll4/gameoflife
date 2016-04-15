package gameoflife;

public class Brod {
  public static void main(String[] args) {
    Gameboard gb = new Gameboard();
    gb.setPattern(GoL.glider);
    gb.populateBoard();
    gb.countNeighbours();
    
    
  }
}
