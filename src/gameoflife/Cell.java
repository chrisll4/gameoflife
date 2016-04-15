package gameoflife;

public class Cell {
  
  public Cell() {
  }
  
  boolean alive = false;
  byte neighbours = 0;
  
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
