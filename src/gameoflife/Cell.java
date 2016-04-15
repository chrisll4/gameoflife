package gameoflife;

public class Cell {
  
  public Cell() {
  }
  
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
