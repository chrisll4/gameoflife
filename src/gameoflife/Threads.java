package gameoflife;

import java.util.ArrayList;

/**
 * Class used to test concurrent methods before implementing into the
 * Game of Life program itself. In the end was not implemented into the program,
 * as these threads did not present a performance increase for the
 * largest board available in our program (but did for larger boards).
 * Try setting a larger board in the main() method (gb.setBoardDim(x,y)) to 
 * see performance.
 * @author Fredrik
 */
public class Threads {  
  static GameboardDynamic gb;
  
  /*
   * The following four threads perform the same tasks as the nextGeneration()
   * method from Gameboard, though each thread only works on a 
   * quarter of the board.
   */

  static Thread thr1 = new Thread(() -> {
    for (int i=0; i<gb.boardCols/4;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        gb.board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {
            int x = (i+m+gb.boardCols)%gb.boardCols;
            int y = (j+n+gb.boardRows)%gb.boardRows;
            if (gb.board.get(x).get(y).alive == true) {
              gb.board.get(i).get(j).neighbours++;
            }
          }
        }
        if (gb.board.get(i).get(j).alive == true) {
          //Remove center cell from count, if alive
          gb.board.get(i).get(j).neighbours--;
        }
      }
    }
   
    for (int i=0; i<gb.board.size()/4; i++) {
      for (int j=0; j<gb.board.get(0).size(); j++) {
        gb.board.get(i).get(j).changeLife(); 
      }
    }
  });
    
  static Thread thr2 = new Thread(() -> {
    for (int i=gb.boardCols/4; i<2*gb.boardCols/4;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        gb.board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {
            int x = (i+m+gb.boardCols)%gb.boardCols;
            int y = (j+n+gb.boardRows)%gb.boardRows;
            if (gb.board.get(x).get(y).alive == true) {
              gb.board.get(i).get(j).neighbours++;
            }
          }
        }
        if (gb.board.get(i).get(j).alive == true) {
          //Remove center cell from count, if alive
          gb.board.get(i).get(j).neighbours--;
        }
      }
    }
  
    for (int i=gb.boardCols/4; i<2*gb.board.size()/4; i++) {
      for (int j=0; j<gb.board.get(0).size(); j++) {
        gb.board.get(i).get(j).changeLife(); 
      }
    }
  });
    
  static Thread thr3 = new Thread(() -> {
    for (int i=2*gb.boardCols/4; i<3*gb.boardCols/4;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        gb.board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {
            int x = (i+m+gb.boardCols)%gb.boardCols;
            int y = (j+n+gb.boardRows)%gb.boardRows;
            if (gb.board.get(x).get(y).alive == true) {
              gb.board.get(i).get(j).neighbours++;
            }
          }
        }
        if (gb.board.get(i).get(j).alive == true) {
          //Remove center cell from count, if alive
          gb.board.get(i).get(j).neighbours--;
        }
      }
    }
  
    for (int i=2*gb.boardCols/4; i<3*gb.board.size()/4; i++) {
      for (int j=0; j<gb.board.get(0).size(); j++) {
        gb.board.get(i).get(j).changeLife(); 
      }
    }
  });
  
  static Thread thr4 = new Thread(() -> {
    for (int i=3*gb.boardCols/4; i<gb.boardCols;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        gb.board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {
            int x = (i+m+gb.boardCols)%gb.boardCols;
            int y = (j+n+gb.boardRows)%gb.boardRows;
            if (gb.board.get(x).get(y).alive == true) {
              gb.board.get(i).get(j).neighbours++;
            }
          }
        }
        if (gb.board.get(i).get(j).alive == true) {
          //Remove center cell from count, if alive
          gb.board.get(i).get(j).neighbours--;
        }
      }
    }
  
    for (int i=3*gb.boardCols/4; i<gb.board.size(); i++) {
      for (int j=0; j<gb.board.get(0).size(); j++) {
        gb.board.get(i).get(j).changeLife(); 
      }
    }
  });
  
  /**
   * Method that performs the same tasks as the nextGeneration() method from
   * Gameboard, in a single thread.
   */
  public static void fikse() {
    for (int i=0; i<gb.boardCols;i++) {
      for (int j=0; j<gb.boardRows;j++) {
        gb.board.get(i).get(j).neighbours = 0;
        for (int m = -1; m <= 1; m++) {
          for (int n = -1; n <= 1; n++) {
            int x = (i+m+gb.boardCols)%gb.boardCols;
            int y = (j+n+gb.boardRows)%gb.boardRows;
            if (gb.board.get(x).get(y).alive == true) {
              gb.board.get(i).get(j).neighbours++;
            }
          }
        }
        if (gb.board.get(i).get(j).alive == true) {
          //Remove center cell from count, if alive
          gb.board.get(i).get(j).neighbours--;
        }
      }
    }
    
    for (int i=0; i<gb.board.size(); i++) {
      for (int j=0; j<gb.board.get(0).size(); j++) {
        gb.board.get(i).get(j).changeLife(); 
      }
    }
  }
  
  /**
   * This method runs the threads, and causes the main java process to wait
   * until the threads have finished working before proceeding.
   * @throws InterruptedException Exception if threads are interrupted
   */
  public static void fikseSamtidig() throws InterruptedException {
    thr1.start();
    thr2.start();
    thr3.start();
    thr4.start();
    thr1.join();
    thr2.join();
    thr3.join();
    thr4.join();
  }
  
  public static void main(String[] args) {
    gb = new GameboardDynamic();
    gb.board = new ArrayList<>();
    gb.setBoardDim(720, 480);
    for (int i=0; i<gb.boardCols; i++) {
      gb.board.add(new ArrayList<>());
      for (int j=0; j<gb.boardRows; j++) {
        gb.board.get(i).add(new Cell());
      }
    }
    System.out.println("Dims: " + gb.board.size() + "x" + gb.board.get(0).size());
    System.out.println("----------");
    long start = System.currentTimeMillis();
    fikse();
    long total = System.currentTimeMillis() - start;
    System.out.println("SINGLE THREAD:");
    System.out.println("Time: " + total + "ms");
    System.out.println("----------");
    long begin = System.currentTimeMillis();
    try {
    fikseSamtidig();
    } catch (InterruptedException ie) {
      System.out.println("problem");
    }
    long sum = System.currentTimeMillis() - begin;
    System.out.println("FOUR THREADS:");
    System.out.println("Time: " + sum + "ms");
  } 
}
