package gameoflife;
/**
 * Cell class, containing two variables (boolean alive, and byte neighbours).
 * Also contains method for changing life status of the cell, depending on
 * number of neighbours. Meant for use in Cell-arrays to represent cells 
 * on the game board in Game of Life. 
 */
public class Cell {
  
  boolean alive = false;
  byte neighbours = 0;
  
  /**
   * Changes the 'alive' status of a cell, depending on its number
   * of neighbours and current status. 'alive' is either true or false.
   */
  public void changeLife() {
    if (alive == true && neighbours < 2) { //Underpopulation
      alive = false;
    } else if (alive == true && neighbours > 3) { //Overpopulation
      alive = false;
    } else if (alive == false && neighbours == 3) { //Reproduction
      alive = true;
    }
  }
}
