package gameoflife;

import java.util.ArrayList;

/**
 * Game board class containing information
 * @author Fredrik, Christine (dynamic)
 */
public class GameboardDynamic {
    
  protected int boardCols = 45;
  protected int boardRows = 30;
  
  protected int patCols;
  protected int patRows;
 
  protected ArrayList<ArrayList<Cell>> pattern;
  protected ArrayList<ArrayList<Cell>> patOrigin;
  protected ArrayList<ArrayList<Cell>> board;
 
  protected void setPattern(byte[][] tab) {
    patCols = tab.length;
    patRows = tab[0].length;

    pattern = new ArrayList<>();
    patOrigin = new ArrayList<>();
            
    for (int i=0; i<patCols; i++) {
      pattern.add(new ArrayList<>());
      patOrigin.add(new ArrayList<>());
      for (int j=0; j<patRows; j++) {
        pattern.get(i).add(new Cell());
        patOrigin.get(i).add(new Cell());
        if (tab[i][j] == 1) {
          pattern.get(i).get(j).alive = true;
          patOrigin.get(i).get(j).alive = true;
        } else {
          pattern.get(i).get(j).alive = false;
          patOrigin.get(i).get(j).alive = false;
        }
      }
    }
  }
  
  protected void setPattern(ArrayList<ArrayList<Cell>> tab) {
    patCols = tab.size();
    patRows = tab.get(0).size();

    pattern = new ArrayList<>();
            
    for (int i=0; i<patCols; i++) {
      pattern.add(new ArrayList<>());
      for (int j=0; j<patRows; j++) {
        pattern.get(i).add(new Cell());
        if (tab.get(i).get(j).alive == true) {
          pattern.get(i).get(j).alive = true;
        } else {
          pattern.get(i).get(j).alive = false;
        }
        //System.out.println("2: " + patternDyn.size());
      }
    }
     
    //boardColsDyn = patternDyn.size(); //Utvid størrelsen???
    //boardRowsDyn = patternDyn.size();
  }
  
 
  protected void setBoardDim(int c, int r) {
    boardCols = c;
    boardRows = r;
  } 

  protected void populateBoard(){
    board = new ArrayList<>();
    for (int i=0; i<boardCols; i++) {
      board.add(new ArrayList<>());
      for (int j=0; j<boardRows; j++) {
        board.get(i).add(new Cell());
      }
    }
    
    if (pattern.get(0).size() > board.get(0).size()) {
      int rowDiff = pattern.get(0).size() - board.get(0).size();
      int colDiff = pattern.size() - board.size();
      
      for (int i=0; i<colDiff; i++) {
        int tick = 0;
        if (i%2==0) {
          pattern.remove(tick);
        } else {
          pattern.remove(pattern.size()-1-tick);
          tick++;
        }
      }
      for (int i=0; i<pattern.size(); i++) {
        for (int j=0; j<rowDiff; j++) {
          int tick = 0;
          if (j%2==0) {
            pattern.get(i).remove(tick);
          } else {
            pattern.get(i).remove(pattern.get(i).size()-1-j);
            tick++;
          }
        }
      }
    }
    
    for (int i=0; i<pattern.size(); i++) {    
      for (int j=0; j<pattern.get(i).size(); j++) {
        int x = i+boardCols/2-pattern.size()/2;
        int y = j+boardRows/2-pattern.get(i).size()/2;
        if(pattern.get(i).get(j).alive){
          board.get(x).get(y).alive = true;
        }
      }
    }
  }
 
  public void countNeighbours(){
    for (int i=0; i<boardCols;i++) {
      for (int j=0; j<boardRows;j++) {
        board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {  
            int xc = (i+m+boardCols)%boardCols;
            int yc = (j+n+boardRows)%boardRows;
            if(board.get(xc).get(yc).alive == true){
              board.get(i).get(j).neighbours++;
            }
          }   
        }
        if (board.get(i).get(j).alive == true) {
          //Fjern midten hvis i live
          board.get(i).get(j).neighbours--;
        }
      } 
    }
  }
  public void setLife() {
    for (int i=0; i<boardCols; i++) {
      for (int j=0; j<boardRows; j++) {
        board.get(i).get(j).changeLife(); 
      }
    }
  }
  
  public void nextGeneration() {
   countNeighbours();
   setLife();
  } 
}
