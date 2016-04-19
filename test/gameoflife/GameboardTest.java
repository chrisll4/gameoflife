package gameoflife;

import junit.framework.Assert;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

public class GameboardTest extends TestCase {
  
  public GameboardTest(String testName) {
    super(testName);
  }

  /**
   * Test of setPattern method, of class Gameboard.
   */
  public void testSetPattern() {
    System.out.println("setPattern");
    byte[][] tab = Patterns.glider;
    String txt = "";
    Gameboard instance = new Gameboard();
    instance.setPattern(tab);
    for (int i=0; i<tab.length; i++) {
      for (int j=0; j<tab[0].length; j++) {
        txt += tab[i][j];
      }
    }
    Assert.assertEquals("001101011", txt);
  }

  /**
   * Test of setBoardDim method, of class Gameboard.
   */
  public void testSetBoardDim() {
    System.out.println("setBoardDim");
    int c = 4;
    int r = 5;
    Gameboard instance = new Gameboard();
    instance.setBoardDim(c, r);
    Assert.assertEquals(4, instance.boardCols);
    Assert.assertEquals(5, instance.boardRows);
    Assert.assertFalse(instance.boardCols != 4);
    Assert.assertFalse(instance.boardRows != 5);
  }

  /**
   * Test of populateBoard method, of class Gameboard.
   */
  public void testPopulateBoard() {
    System.out.println("populateBoard");
    Gameboard instance = new Gameboard();
    instance.setPattern(Patterns.glider);
    instance.populateBoard();
    int livingCells = 0;
    for (int i=0; i<instance.board.length; i++) {
      for (int j=0; j<instance.board[0].length; j++) {
        if (instance.board[i][j].alive) {
          livingCells++;
        }
      }
    }
    assertTrue(livingCells == 5);
    assertFalse(livingCells == 10);
    assertFalse(livingCells == 4);
    assertFalse(livingCells == 6);
    
  }

  /**
   * Test of countNeighbours method, of class Gameboard.
   */
  public void testCountNeighbours() {
    System.out.println("countNeighbours");
    Gameboard instance = new Gameboard();
    instance.setPattern(Patterns.glider);
    instance.populateBoard();
    instance.countNeighbours();
    Assert.assertEquals(10, instance.totNeighbours);
    Assert.assertTrue(instance.totNeighbours == 10);
    Assert.assertFalse(instance.totNeighbours != 10);
  }
  
}
