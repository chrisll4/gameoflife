package gameoflife;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Fredrik
 */
public class CellTest extends TestCase {
  
  public CellTest(String testName) {
    super(testName);
  }

  /**
   * Test of changeLife method, of class Cell.
   */
  public void testChangeLife() {
    System.out.println("changeLife");
    Gameboard gb = new Gameboard();
    gb.setPattern(Patterns.glider);
    gb.setBoardDim(10,10);
    gb.populateBoard();
    gb.countNeighbours();
    
    Assert.assertTrue(gb.board[4][5].neighbours == 3 && !gb.board[4][5].alive);
    gb.board[4][5].changeLife();
    Assert.assertTrue(gb.board[4][5].alive);
    
    Assert.assertTrue(gb.board[5][4].neighbours == 1 && gb.board[5][4].alive);
    gb.board[5][4].changeLife();
    Assert.assertTrue(!gb.board[5][4].alive);
    
    Assert.assertTrue(gb.board[6][6].neighbours == 2 && gb.board[6][6].alive);
    gb.board[6][6].changeLife();
    Assert.assertTrue(gb.board[6][6].alive);
    
  }
  
}
