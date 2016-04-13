package gameoflife;

public class Lompe {
  public static void main(String[] args) {
    Polse[] pls = new Polse[10];
    for (int i=0; i<pls.length; i++) {
      pls[i] = new Polse();
      if (i%2 == 0) {
        pls[i].setAlive(true);
      } else {
        pls[i].setAlive(false);
      }
      System.out.print(pls[i].getAlive());
    }
    
  }
}
